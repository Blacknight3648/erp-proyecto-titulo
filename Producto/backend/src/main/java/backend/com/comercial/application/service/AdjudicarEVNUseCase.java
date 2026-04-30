package backend.com.comercial.application.service;

import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.application.dto.ItemNVDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdjudicarEVNUseCase {

    private final EvaluacionNegocioRepository evnRepository;
    private final CrearNVUseCase crearNVUseCase;

    @Transactional
    public EVNResponse ejecutar(Long evnId) {
        EvaluacionNegocio evn = evnRepository.findById(evnId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación de Negocio no encontrada: " + evnId));

        // 1. Cambiar estado a ADJUDICADA
        evn.adjudicar();
        evnRepository.save(evn);

        // 2. Crear Nota de Venta automáticamente
        try {
            CrearNVCommand nvCommand = new CrearNVCommand();
            nvCommand.setEvaluacionNegocioId(evn.getEvaluacionNegocioId());
            nvCommand.setClienteId(evn.getClienteId());
            nvCommand.setVendedorId(evn.getVendedorId());
            nvCommand.setEsKit(false); // Por defecto
            nvCommand.setFechaEntregaEstimada(evn.getFechaEvaluacion().plusDays(15)); // Default 15 days

            List<ItemNVDTO> nvItems = evn.getItems().stream().map(itemEvn -> {
                ItemNVDTO itemNv = new ItemNVDTO();
                itemNv.setProductoId(itemEvn.getProductoId());
                itemNv.setProveedorId(itemEvn.getProveedorId());
                itemNv.setCantidad(itemEvn.getCantidad());
                itemNv.setPrecioUnitario(itemEvn.getPrecioUnitario().getAmount());
                itemNv.setItemType(itemEvn.getTipoItem());
                
                // Mapear especificaciones técnicas desde el map
                if (itemEvn.getTechnicalSpecs() != null) {
                    itemNv.setModelo(itemEvn.getTechnicalSpecs().get("modelo"));
                    itemNv.setTela(itemEvn.getTechnicalSpecs().get("tela"));
                    itemNv.setComposicion(itemEvn.getTechnicalSpecs().get("composicion"));
                    itemNv.setColor(itemEvn.getTechnicalSpecs().get("color"));
                    itemNv.setGenero(itemEvn.getTechnicalSpecs().get("genero"));
                    itemNv.setTalla(itemEvn.getTechnicalSpecs().get("talla"));
                }
                
                itemNv.setGeneraOt(true); // Por defecto para Pre-NV
                return itemNv;
            }).collect(Collectors.toList());

            nvCommand.setItems(nvItems);

            // Ejecutar creación de NV (esto ya gatilla OP/SC internamente)
            crearNVUseCase.ejecutar(nvCommand);
            
        } catch (Exception e) {
            // Log error but don't fail adjudication? 
            // Better to fail if NV cannot be created as it is a requirement.
            throw new RuntimeException("Error al crear la Nota de Venta automática: " + e.getMessage(), e);
        }

        return EVNResponse.fromDomain(evn);
    }
}
