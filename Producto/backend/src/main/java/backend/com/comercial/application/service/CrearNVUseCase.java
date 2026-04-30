package backend.com.comercial.application.service;

import backend.com.adquisiciones.application.dto.CrearSCCommand;
import backend.com.adquisiciones.application.dto.SCItemDTO;
import backend.com.adquisiciones.application.service.CrearSCUseCase;
import backend.com.produccion.application.service.CrearOrdenProduccionUseCase;
import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.application.dto.ItemNVDTO;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.ItemNVTalla;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrearNVUseCase {

    private final NotaVentaRepository nvRepository;
    private final CrearSCUseCase crearSCUseCase;
    private final CrearOrdenProduccionUseCase crearOPUseCase;

    @Transactional
    public NVResponse ejecutar(CrearNVCommand command) {
        Long numeroVal = command.getNumero();
        if (numeroVal == null) {
            numeroVal = nvRepository.findMaxNumero().orElse(0L) + 1;
        }

        NotaVenta nv = NotaVenta.crear(
                new DocumentNumber(numeroVal),
                command.getEvaluacionNegocioId(),
                command.getClienteId(),
                command.getVendedorId(),
                command.getEsKit(),
                command.getDetalleKit(),
                command.getFechaEntregaEstimada());

        List<SCItemDTO> itemsParaAdquisicion = new ArrayList<>();

        if (command.getItems() != null) {
            for (int i = 0; i < command.getItems().size(); i++) {
                ItemNVDTO dto = command.getItems().get(i);

                List<ItemNVTalla> tallas = (dto.getTallas() == null) ? null
                        : dto.getTallas().stream()
                                .map(t -> new ItemNVTalla(t.getTalla(), t.getCantidad()))
                                .collect(Collectors.toList());

                ItemNV item = new ItemNV(
                        i + 1,
                        dto.getProductoId(),
                        dto.getModelo(),
                        dto.getTela(),
                        dto.getComposicion(),
                        dto.getColor(),
                        dto.getTalla(),
                        dto.getGenero(),
                        null, // codigo (adding it as null for now if not in DTO)
                        dto.getProveedorId(),
                        dto.getLlevaLogo(),
                        dto.getItemType() != null ? dto.getItemType() : "OP",
                        dto.getGeneraOt(),
                        dto.getDetalleOt(),
                        dto.getLogoDetalle(),
                        dto.getCantidad(),
                        new Money(dto.getPrecioUnitario(), "CLP"),
                        tallas);
                nv.addItem(item);

                // Check if this item generates a Solicitud de Compra (SC o SCI)
                if ("SC".equals(dto.getItemType()) || "SCI".equals(dto.getItemType())) {
                    SCItemDTO scItem = new SCItemDTO();
                    scItem.setNroItem((long) (i + 1));
                    scItem.setProductoId(dto.getProductoId());
                    scItem.setDescripcionProducto(dto.getModelo() + " (" + dto.getColor() + ")");
                    scItem.setCantidad(dto.getCantidad());
                    scItem.setPrecioEstimadoUnitario(dto.getPrecioUnitario());
                    scItem.setMoneda("CLP");
                    scItem.setTipo(dto.getItemType());
                    itemsParaAdquisicion.add(scItem);
                }
            }
        }

        NotaVenta guardada = nvRepository.save(nv);

        // Automaticaly generate SolicitudCompra if items are marked
        if (!itemsParaAdquisicion.isEmpty()) {
            CrearSCCommand scCommand = new CrearSCCommand();
            scCommand.setNotaVentaId(guardada.getIdNV());
            scCommand.setTenantId(1L); // Standard for now
            scCommand.setItems(itemsParaAdquisicion);
            crearSCUseCase.ejecutar(scCommand);
        }

        // Automaticaly generate OrdenProduccion
        crearOPUseCase.execute(guardada);

        return NVResponse.fromDomain(guardada);
    }
}
