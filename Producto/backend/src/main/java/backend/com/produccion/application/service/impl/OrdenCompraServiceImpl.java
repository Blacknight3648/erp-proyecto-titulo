package backend.com.produccion.application.service.impl;

import backend.com.produccion.application.UseCase.CrearVersionOCUseCase;
import backend.com.produccion.application.UseCase.GenerarOCConsolidadaUseCase;
import backend.com.produccion.application.UseCase.GenerarOCLoteUseCase;
import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.GenerarOCLoteRequest;
import backend.com.produccion.application.dto.HCItemOCItemLinkDTO;
import backend.com.produccion.application.dto.HistorialVersionOCDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.application.service.OrdenCompraService;
import backend.com.produccion.domain.enums.AccionOC;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.model.HCItemOCItemLink;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.domain.model.OrdenCompraVersion;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraVersionRepository;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.application.service.NotificacionService;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdenCompraServiceImpl implements OrdenCompraService {

    private final OrdenCompraRepository ordenCompraRepository;
    private final GenerarOCConsolidadaUseCase generarOCConsolidadaUseCase;
    private final GenerarOCLoteUseCase generarOCLoteUseCase;
    private final HojaCompraRepository hojaCompraRepository;
    private final NotificacionService notificacionService;
    private final HistorialEstadoService historialEstadoService;
    private final CrearVersionOCUseCase crearVersionOCUseCase;
    private final OrdenCompraVersionRepository ordenCompraVersionRepository;

    /** Tipo de entidad para el registro de historial de estado de la OC. */
    private static final String TIPO_ENTIDAD = "OC";

    @Override
    public OrdenCompraDTO generarConsolidada(GenerarOCConsolidadaRequest request) {
        OrdenCompraDTO dto = toDTO(generarOCConsolidadaUseCase.ejecutar(request));
        notificacionService.crear("OC", "Orden de Compra " + dto.getNumeroOC() + " generada", "COMPRAS", "normal");
        return dto;
    }

    @Override
    public List<OrdenCompraDTO> generarLote(GenerarOCLoteRequest request) {
        List<OrdenCompraDTO> ocs = generarOCLoteUseCase.ejecutar(request).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        ocs.forEach(dto -> notificacionService.crear(
                "OC", "Orden de Compra " + dto.getNumeroOC() + " generada", "COMPRAS", "normal"));
        return ocs;
    }

    @Override
    public OrdenCompraDTO marcarEnviada(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        oc.marcarEnviada();
        OrdenCompraDTO dto = toDTO(ordenCompraRepository.save(oc));
        notificacionService.crear("OC", "Orden de Compra " + dto.getNumeroOC() + " enviada al proveedor", "COMPRAS", "normal");
        return dto;
    }

    @Override
    public OrdenCompraDTO marcarRecepcionada(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        oc.marcarRecepcionada();
        OrdenCompraDTO dto = toDTO(ordenCompraRepository.save(oc));
        notificacionService.crear("OC", "Orden de Compra " + dto.getNumeroOC() + " recepcionada", "COMPRAS", "normal");
        return dto;
    }

    @Override
    public OrdenCompraDTO cerrar(Long idOC) {
        OrdenCompra oc = cargar(idOC);
        oc.cerrar();
        OrdenCompraDTO dto = toDTO(ordenCompraRepository.save(oc));
        notificacionService.crear("OC", "Orden de Compra " + dto.getNumeroOC() + " cerrada", "COMPRAS", "normal");
        return dto;
    }

    @Override
    public OrdenCompraDTO rechazar(Long idOC, String motivo, String usuario, String rol) {
        String actor = exigirActor(usuario);
        AccionOC.RECHAZAR.validarRol(rol);
        OrdenCompra oc = cargar(idOC);
        EstadoOC estadoAnterior = oc.getEstado();

        // Snapshot del estado justo antes del rechazo: preserva proveedor/cantidades/
        // precios previos para que un reingreso posterior no los pierda al editarlos.
        crearVersionOCUseCase.ejecutar(idOC, "Rechazo v" + oc.getVersion() + ": " + motivo, actor);

        oc.rechazar(motivo);
        OrdenCompra guardada = ordenCompraRepository.save(oc);
        registrarHistorial(guardada, estadoAnterior, actor, "OC v" + guardada.getVersion() + " rechazada: " + motivo);
        return toDTO(guardada);
    }

    @Override
    public OrdenCompraDTO reingresar(Long idOC, String usuario, String rol, Long nuevoProveedorId,
            List<OrdenCompraItemDTO> itemsCambiados) {
        String actor = exigirActor(usuario);
        AccionOC.REINGRESAR.validarRol(rol);
        OrdenCompra oc = cargar(idOC);
        EstadoOC estadoAnterior = oc.getEstado();
        boolean cambioProveedor = nuevoProveedorId != null && !nuevoProveedorId.equals(oc.getProveedorId());
        oc.reingresar(nuevoProveedorId); // vuelve a EMITIDA con el mismo numeroOC, incrementa version

        List<OrdenCompraItem> items = new ArrayList<>(oc.getItems());
        if (itemsCambiados != null) {
            // Todas las ediciones se validan/aplican en memoria antes de guardar: si
            // alguna falla (p. ej. precio excede la referencia costeada), se lanza la
            // excepción sin haber persistido nada, dejando la OC intacta en RECHAZADA.
            for (OrdenCompraItemDTO cambio : itemsCambiados) {
                Long idOCItem = cambio.getIdOCItem();
                OrdenCompraItem original = items.stream()
                        .filter(i -> idOCItem.equals(i.getIdOCItem()))
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException(
                                "OCItem " + idOCItem + " no encontrado en la OC " + idOC));
                OrdenCompraItem actualizado = construirItemActualizado(original, cambio);
                items = items.stream()
                        .map(i -> i.getIdOCItem().equals(idOCItem) ? actualizado : i)
                        .collect(Collectors.toList());
            }
        }

        OrdenCompra ocFinal = new OrdenCompra(
                oc.getIdOC(), oc.getNumeroOC(), oc.getProveedorId(), oc.getEstado(),
                oc.getFechaEmision(), oc.getFechaEntregaEstimada(), oc.getObservaciones(),
                BigDecimal.ZERO, items, oc.getMotivoRechazo(), oc.getVersion());
        ocFinal.recalcularTotal();

        OrdenCompra guardada = ordenCompraRepository.save(ocFinal);
        String detalle = "OC reingresada como v" + guardada.getVersion()
                + (cambioProveedor ? " (proveedor actualizado a #" + guardada.getProveedorId() + ")" : "");
        registrarHistorial(guardada, estadoAnterior, actor, detalle);
        return toDTO(guardada);
    }

    /** Exige la firma del actor; devuelve el usuario validado. */
    private String exigirActor(String usuario) {
        if (usuario == null || usuario.isBlank()) {
            throw new ValidationException("Se requiere identificar al usuario que firma la acción");
        }
        return usuario.trim();
    }

    private void registrarHistorial(OrdenCompra oc, EstadoOC estadoAnterior, String usuario, String observacion) {
        historialEstadoService.registrar(
                TIPO_ENTIDAD,
                oc.getIdOC(),
                estadoAnterior != null ? estadoAnterior.name() : null,
                oc.getEstado() != null ? oc.getEstado().name() : null,
                usuario,
                observacion);
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
                BigDecimal.ZERO, nuevos, oc.getMotivoRechazo(), oc.getVersion());
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

    @Override
    @Transactional(readOnly = true)
    public List<HistorialVersionOCDTO> obtenerHistorialVersiones(Long idOC) {
        List<HistorialVersionOCDTO> historial = new ArrayList<>();

        // 1. Snapshots congelados (uno por cada rechazo sufrido)
        List<OrdenCompraVersion> snapshots = ordenCompraVersionRepository.findAllByOcId(idOC);

        BigDecimal totalAnterior = null;
        for (OrdenCompraVersion snapshot : snapshots) {
            historial.add(HistorialVersionOCDTO.builder()
                    .id("v" + snapshot.getNumeroVersion())
                    .version(snapshot.getNumeroVersion())
                    .accion("RECHAZO")
                    .fechaFormateada(snapshot.getFechaCreacion() != null
                            ? java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
                                    .format(snapshot.getFechaCreacion())
                            : null)
                    .totalNeto(snapshot.getTotalNeto())
                    .totalAnterior(totalAnterior)
                    .proveedorId(snapshot.getProveedorId())
                    .usuario(snapshot.getUsuarioCreador())
                    .motivo(snapshot.getMotivoCambio())
                    .build());
            totalAnterior = snapshot.getTotalNeto();
        }

        // 2. Estado activo actual
        final BigDecimal totalAnteriorFinal = totalAnterior;
        ordenCompraRepository.findById(idOC).ifPresent(oc -> {
            String accion = switch (oc.getEstado()) {
                case RECHAZADA -> "RECHAZO";
                default -> snapshots.isEmpty() ? "EMISION" : "REINGRESO";
            };
            historial.add(HistorialVersionOCDTO.builder()
                    .id("active")
                    .version(oc.getVersion())
                    .accion(accion)
                    .fechaFormateada("Actual")
                    .totalNeto(oc.getTotalNeto())
                    .totalAnterior(totalAnteriorFinal)
                    .proveedorId(oc.getProveedorId())
                    .usuario("Sistema")
                    .motivo(oc.getMotivoRechazo())
                    .build());
        });

        Collections.reverse(historial);
        return historial;
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
                .motivoRechazo(oc.getMotivoRechazo())
                .version(oc.getVersion())
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
                BigDecimal.ZERO, nuevos, oc.getMotivoRechazo(), oc.getVersion());
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

        OrdenCompraItem itemActualizado = construirItemActualizado(itemOriginal, itemDTO);

        List<OrdenCompraItem> nuevos = oc.getItems().stream()
                .map(i -> i.getIdOCItem().equals(idOCItem) ? itemActualizado : i)
                .collect(Collectors.toList());

        OrdenCompra ocActualizada = new OrdenCompra(
                oc.getIdOC(), oc.getNumeroOC(), oc.getProveedorId(), oc.getEstado(),
                oc.getFechaEmision(), oc.getFechaEntregaEstimada(), oc.getObservaciones(),
                BigDecimal.ZERO, nuevos, oc.getMotivoRechazo(), oc.getVersion());
        ocActualizada.recalcularTotal();

        return toDTO(ordenCompraRepository.save(ocActualizada));
    }

    /**
     * Construye un OrdenCompraItem actualizado a partir del original + overrides
     * del DTO (campos null conservan el valor original), validando precio/cantidad
     * y el límite de precio de referencia costeado. Reutilizado por
     * {@link #actualizarItem} y por {@link #reingresar} (edición de ítems en el
     * mismo paso del reingreso, dentro de la misma transacción).
     */
    private OrdenCompraItem construirItemActualizado(OrdenCompraItem itemOriginal, OrdenCompraItemDTO itemDTO) {
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
                            itemOriginal.getIdOCItem(),
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

        return new OrdenCompraItem(
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
                BigDecimal.ZERO, nuevos, oc.getMotivoRechazo(), oc.getVersion());
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
