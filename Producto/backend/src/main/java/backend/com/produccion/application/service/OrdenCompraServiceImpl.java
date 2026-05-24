package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.HCItemOCItemLinkDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.domain.model.EstadoOC;
import backend.com.produccion.domain.model.HCItemOCItemLink;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final GenerarOCConsolidadaUseCase generarOCConsolidadaUseCase;

    @Override
    public OrdenCompraDTO generarConsolidada(GenerarOCConsolidadaRequest request) {
        return toDTO(generarOCConsolidadaUseCase.ejecutar(request));
    }

    @Override
    public OrdenCompraDTO marcarEnviada(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        oc.marcarEnviada();
        return toDTO(ordenCompraRepository.save(oc));
    }

    @Override
    public OrdenCompraDTO marcarRecepcionada(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        oc.marcarRecepcionada();
        return toDTO(ordenCompraRepository.save(oc));
    }

    @Override
    public OrdenCompraDTO cerrar(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        oc.cerrar();
        return toDTO(ordenCompraRepository.save(oc));
    }

    @Override
    public OrdenCompraDTO actualizarPrecioItem(Long idOC, Long idOCItem, BigDecimal nuevoPrecio) {
        if (nuevoPrecio == null || nuevoPrecio.signum() < 0) {
            throw new BusinessRuleException("El precio unitario debe ser no negativo");
        }
        OrdenCompra oc = cargar(idOC);
        if (oc.getEstado() != EstadoOC.EMITIDA) {
            throw new BusinessRuleException(
                    "Solo se puede modificar precios en OCs EMITIDAS (estado actual: " + oc.getEstado() + ")");
        }

        OrdenCompraItem itemOriginal = oc.getItems().stream()
                .filter(i -> idOCItem.equals(i.getIdOCItem()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "OCItem " + idOCItem + " no encontrado en la OC " + idOC));

        BigDecimal subtotal = itemOriginal.getCantidadComprada().multiply(nuevoPrecio);

        OrdenCompraItem itemActualizado = new OrdenCompraItem(
                itemOriginal.getIdOCItem(),
                itemOriginal.getOcId(),
                itemOriginal.getTipoInsumo(),
                itemOriginal.getInsumoId(),
                itemOriginal.getNombreInsumo(),
                itemOriginal.getCantidadRequerida(),
                itemOriginal.getCantidadStock(),
                itemOriginal.getCantidadComprada(),
                nuevoPrecio,
                subtotal,
                itemOriginal.getHcLinks());

        // Reconstruir la OC con el item actualizado
        List<OrdenCompraItem> nuevos = oc.getItems().stream()
                .map(i -> i.getIdOCItem().equals(idOCItem) ? itemActualizado : i)
                .collect(Collectors.toList());

        OrdenCompra ocActualizada = new OrdenCompra(
                oc.getIdOC(), oc.getNumeroOC(), oc.getProveedorId(), oc.getEstado(),
                oc.getFechaEmision(), oc.getFechaEntregaEstimada(), oc.getObservaciones(),
                BigDecimal.ZERO, nuevos);
        ocActualizada.recalcularTotal();

        return toDTO(ordenCompraRepository.save(ocActualizada));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenCompraDTO> obtenerPorId(Long idOC) {
        return ordenCompraRepository.findById(idOC).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarTodas() {
        return ordenCompraRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorEstado(EstadoOC estado) {
        return ordenCompraRepository.findAllByEstado(estado).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorProveedor(Long proveedorId) {
        return ordenCompraRepository.findAllByProveedorId(proveedorId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrdenCompraDTO> listarPorHCItem(Long hcItemId) {
        return ordenCompraRepository.findAllByHcItemId(hcItemId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private OrdenCompra cargar(Long idOC) {
        return ordenCompraRepository.findById(idOC)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Compra no encontrada: " + idOC));
    }

    private OrdenCompraDTO toDTO(OrdenCompra oc) {
        if (oc == null) return null;
        return OrdenCompraDTO.builder()
                .idOC(oc.getIdOC())
                .numeroOC(oc.getNumeroOC() != null ? oc.getNumeroOC().getValue() : null)
                .proveedorId(oc.getProveedorId())
                .estado(oc.getEstado())
                .fechaEmision(oc.getFechaEmision())
                .fechaEntregaEstimada(oc.getFechaEntregaEstimada())
                .observaciones(oc.getObservaciones())
                .totalNeto(oc.getTotalNeto())
                .items(oc.getItems() != null
                        ? oc.getItems().stream().map(this::itemToDTO).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private OrdenCompraItemDTO itemToDTO(OrdenCompraItem item) {
        return OrdenCompraItemDTO.builder()
                .idOCItem(item.getIdOCItem())
                .ocId(item.getOcId())
                .tipoInsumo(item.getTipoInsumo())
                .insumoId(item.getInsumoId())
                .nombreInsumo(item.getNombreInsumo())
                .cantidadRequerida(item.getCantidadRequerida())
                .cantidadStock(item.getCantidadStock())
                .cantidadComprada(item.getCantidadComprada())
                .precioUnitario(item.getPrecioUnitario())
                .subtotal(item.getSubtotal())
                .hcLinks(item.getHcLinks() != null
                        ? item.getHcLinks().stream().map(this::linkToDTO).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private HCItemOCItemLinkDTO linkToDTO(HCItemOCItemLink link) {
        return HCItemOCItemLinkDTO.builder()
                .hcItemId(link.getHcItemId())
                .ocItemId(link.getOcItemId())
                .cantidadAsignada(link.getCantidadAsignada())
                .build();
    }
}
