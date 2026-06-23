package backend.com.produccion.application.service.impl;

import backend.com.produccion.application.UseCase.GenerarOCConsolidadaUseCase;
import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.HCItemOCItemLinkDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.application.service.OrdenCompraService;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.model.HCItemOCItemLink;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final GenerarOCConsolidadaUseCase generarOCConsolidadaUseCase;
    private final HojaCompraRepository hojaCompraRepository;

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
                itemOriginal.getArticuloId(),
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
        if (oc == null)
            return null;
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
                .articuloId(item.getArticuloId())
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

    @Override
    public void eliminar(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        if (oc.getEstado() != EstadoOC.EMITIDA) {
            throw new BusinessRuleException(
                    "Solo se puede eliminar una OC en estado EMITIDA (estado actual: " + oc.getEstado() + ")");
        }
        ordenCompraRepository.deleteById(idOC);
    }

    @Override
    public OrdenCompraDTO agregarItem(Long idOC, OrdenCompraItemDTO itemDTO) {
        OrdenCompra oc = cargar(idOC);
        if (oc.getEstado() != EstadoOC.EMITIDA) {
            throw new BusinessRuleException(
                    "Solo se pueden agregar ítems a OCs EMITIDAS (estado actual: " + oc.getEstado() + ")");
        }

        BigDecimal precioUnitario = itemDTO.getPrecioUnitario() != null ? itemDTO.getPrecioUnitario() : BigDecimal.ZERO;
        if (precioUnitario.signum() < 0) {
            throw new BusinessRuleException("El precio unitario debe ser no negativo");
        }

        BigDecimal cantidadStock = itemDTO.getCantidadStock() != null ? itemDTO.getCantidadStock() : BigDecimal.ZERO;
        BigDecimal cantidadComprada = itemDTO.getCantidadComprada();
        if (cantidadComprada == null) {
            BigDecimal req = itemDTO.getCantidadRequerida() != null ? itemDTO.getCantidadRequerida() : BigDecimal.ZERO;
            cantidadComprada = req.subtract(cantidadStock);
        }
        if (cantidadComprada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La cantidad comprada debe ser mayor que cero");
        }

        BigDecimal subtotal = cantidadComprada.multiply(precioUnitario);

        List<HCItemOCItemLink> links = new ArrayList<>();
        if (itemDTO.getHcLinks() != null && !itemDTO.getHcLinks().isEmpty()) {
            List<Long> hcItemIds = itemDTO.getHcLinks().stream()
                    .map(HCItemOCItemLinkDTO::getHcItemId)
                    .collect(Collectors.toList());

            // Validar límites de precio costeado (referencia)
            validarLimitesPrecio(precioUnitario, hcItemIds);

            for (HCItemOCItemLinkDTO linkDto : itemDTO.getHcLinks()) {
                links.add(new HCItemOCItemLink(
                        linkDto.getHcItemId(),
                        null,
                        linkDto.getCantidadAsignada() != null ? linkDto.getCantidadAsignada() : cantidadComprada
                ));
            }
        }

        OrdenCompraItem nuevoItem = new OrdenCompraItem(
                null,
                idOC,
                itemDTO.getTipoInsumo(),
                itemDTO.getArticuloId(),
                itemDTO.getNombreInsumo(),
                itemDTO.getCantidadRequerida() != null ? itemDTO.getCantidadRequerida() : cantidadComprada,
                cantidadStock,
                cantidadComprada,
                precioUnitario,
                subtotal,
                links
        );

        List<OrdenCompraItem> nuevos = new ArrayList<>(oc.getItems());
        nuevos.add(nuevoItem);

        OrdenCompra ocActualizada = new OrdenCompra(
                oc.getIdOC(), oc.getNumeroOC(), oc.getProveedorId(), oc.getEstado(),
                oc.getFechaEmision(), oc.getFechaEntregaEstimada(), oc.getObservaciones(),
                BigDecimal.ZERO, nuevos);
        ocActualizada.recalcularTotal();

        return toDTO(ordenCompraRepository.save(ocActualizada));
    }

    @Override
    public OrdenCompraDTO actualizarItem(Long idOC, Long idOCItem, OrdenCompraItemDTO itemDTO) {
        OrdenCompra oc = cargar(idOC);
        if (oc.getEstado() != EstadoOC.EMITIDA) {
            throw new BusinessRuleException(
                    "Solo se pueden modificar ítems en OCs EMITIDAS (estado actual: " + oc.getEstado() + ")");
        }

        OrdenCompraItem itemOriginal = oc.getItems().stream()
                .filter(i -> idOCItem.equals(i.getIdOCItem()))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "OCItem " + idOCItem + " no encontrado en la OC " + idOC));

        BigDecimal precioUnitario = itemDTO.getPrecioUnitario() != null ? itemDTO.getPrecioUnitario() : itemOriginal.getPrecioUnitario();
        if (precioUnitario.signum() < 0) {
            throw new BusinessRuleException("El precio unitario debe ser no negativo");
        }

        BigDecimal cantidadStock = itemDTO.getCantidadStock() != null ? itemDTO.getCantidadStock() : itemOriginal.getCantidadStock();
        BigDecimal cantidadComprada = itemDTO.getCantidadComprada();
        if (cantidadComprada == null) {
            BigDecimal req = itemDTO.getCantidadRequerida() != null ? itemDTO.getCantidadRequerida() : itemOriginal.getCantidadRequerida();
            cantidadComprada = req.subtract(cantidadStock);
        }
        if (cantidadComprada.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessRuleException("La cantidad comprada debe ser mayor que cero");
        }

        BigDecimal subtotal = cantidadComprada.multiply(precioUnitario);

        List<HCItemOCItemLink> links = new ArrayList<>();
        if (itemDTO.getHcLinks() != null) {
            if (!itemDTO.getHcLinks().isEmpty()) {
                List<Long> hcItemIds = itemDTO.getHcLinks().stream()
                        .map(HCItemOCItemLinkDTO::getHcItemId)
                        .collect(Collectors.toList());

                validarLimitesPrecio(precioUnitario, hcItemIds);

                for (HCItemOCItemLinkDTO linkDto : itemDTO.getHcLinks()) {
                    links.add(new HCItemOCItemLink(
                            linkDto.getHcItemId(),
                            idOCItem,
                            linkDto.getCantidadAsignada() != null ? linkDto.getCantidadAsignada() : cantidadComprada
                    ));
                }
            }
        } else {
            if (itemOriginal.getHcLinks() != null && !itemOriginal.getHcLinks().isEmpty()) {
                List<Long> hcItemIds = itemOriginal.getHcLinks().stream()
                        .map(HCItemOCItemLink::getHcItemId)
                        .collect(Collectors.toList());

                validarLimitesPrecio(precioUnitario, hcItemIds);
                links.addAll(itemOriginal.getHcLinks());
            }
        }

        OrdenCompraItem itemActualizado = new OrdenCompraItem(
                itemOriginal.getIdOCItem(),
                itemOriginal.getOcId(),
                itemDTO.getTipoInsumo() != null ? itemDTO.getTipoInsumo() : itemOriginal.getTipoInsumo(),
                itemDTO.getArticuloId() != null ? itemDTO.getArticuloId() : itemOriginal.getArticuloId(),
                itemDTO.getNombreInsumo() != null ? itemDTO.getNombreInsumo() : itemOriginal.getNombreInsumo(),
                itemDTO.getCantidadRequerida() != null ? itemDTO.getCantidadRequerida() : itemOriginal.getCantidadRequerida(),
                cantidadStock,
                cantidadComprada,
                precioUnitario,
                subtotal,
                links
        );

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
    public OrdenCompraDTO eliminarItem(Long idOC, Long idOCItem) {
        OrdenCompra oc = cargar(idOC);
        if (oc.getEstado() != EstadoOC.EMITIDA) {
            throw new BusinessRuleException(
                    "Solo se pueden eliminar ítems en OCs EMITIDAS (estado actual: " + oc.getEstado() + ")");
        }

        boolean existe = oc.getItems().stream().anyMatch(i -> idOCItem.equals(i.getIdOCItem()));
        if (!existe) {
            throw new EntityNotFoundException(
                    "OCItem " + idOCItem + " no encontrado en la OC " + idOC);
        }

        List<OrdenCompraItem> nuevos = oc.getItems().stream()
                .filter(i -> !idOCItem.equals(i.getIdOCItem()))
                .collect(Collectors.toList());

        OrdenCompra ocActualizada = new OrdenCompra(
                oc.getIdOC(), oc.getNumeroOC(), oc.getProveedorId(), oc.getEstado(),
                oc.getFechaEmision(), oc.getFechaEntregaEstimada(), oc.getObservaciones(),
                BigDecimal.ZERO, nuevos);
        ocActualizada.recalcularTotal();

        return toDTO(ordenCompraRepository.save(ocActualizada));
    }

    private void validarLimitesPrecio(BigDecimal precioUnitario, List<Long> hcItemIds) {
        if (hcItemIds == null || hcItemIds.isEmpty()) return;
        List<backend.com.produccion.domain.model.HojaCompra> hcs = hojaCompraRepository.findAllByItemIds(hcItemIds);
        for (Long hcItemId : hcItemIds) {
            backend.com.produccion.domain.model.HojaCompraItem hcItem = hcs.stream()
                    .flatMap(hc -> hc.getItems().stream())
                    .filter(item -> hcItemId.equals(item.getIdHCItem()))
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("No se encontró el item de Hoja de Compra con id " + hcItemId));

            if (hcItem.getPrecioUnitarioRef() != null && precioUnitario.compareTo(hcItem.getPrecioUnitarioRef()) > 0) {
                throw new BusinessRuleException(String.format(
                        "El precio de compra unitario (%s) excede el precio de referencia costeado (%s) para el insumo '%s'",
                        precioUnitario, hcItem.getPrecioUnitarioRef(), hcItem.getNombreInsumo()
                ));
            }
        }
    }
}
