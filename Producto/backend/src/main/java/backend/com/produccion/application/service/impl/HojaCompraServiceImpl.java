package backend.com.produccion.application.service.impl;

import backend.com.produccion.application.UseCase.GenerarHCDesdeOPUseCase;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.HojaCompraItemDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.shared.application.service.NotificacionService;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HojaCompraServiceImpl implements HojaCompraService {

    private final HojaCompraRepository hojaCompraRepository;
    private final GenerarHCDesdeOPUseCase generarHCDesdeOPUseCase;
    private final backend.com.produccion.application.UseCase.ModificarHojaCompraItemUseCase modificarHojaCompraItemUseCase;
    private final NotificacionService notificacionService;

    @Override
    public HojaCompraDTO generarDesdeOP(Long opId) {
        HojaCompraDTO dto = toDTO(generarHCDesdeOPUseCase.ejecutar(opId));
        notificacionService.crear("HC", "Hoja de Compra " + dto.getNumeroHC() + " generada para OP #" + opId, "COMPRAS", "normal");
        return dto;
    }

    @Override
    public HojaCompraDTO modificarItem(Long idHC, Long idHCItem, backend.com.produccion.application.dto.ActualizarHojaCompraItemRequest request) {
        return toDTO(modificarHojaCompraItemUseCase.ejecutar(idHC, idHCItem, request));
    }

    @Override
    public HojaCompraDTO agregarItemManual(Long idHC, HojaCompraItemDTO itemDTO) {
        if (itemDTO == null || itemDTO.getNombreInsumo() == null || itemDTO.getNombreInsumo().isBlank()) {
            throw new ValidationException("El nombre del insumo es obligatorio");
        }
        if (itemDTO.getCantidadRequerida() == null || itemDTO.getCantidadRequerida().signum() <= 0) {
            throw new ValidationException("La cantidad requerida debe ser mayor a cero");
        }
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));
        if (hc.getEstado() != EstadoHC.APROBADA) {
            throw new BusinessRuleException(
                    "Solo se pueden agregar insumos no presupuestados a una HC APROBADA (estado actual: " + hc.getEstado() + ")");
        }

        HojaCompraItem nuevo = HojaCompraItem.manual(
                idHC,
                itemDTO.getTipoInsumo(),
                itemDTO.getArticuloId(),
                itemDTO.getNombreInsumo(),
                itemDTO.getCantidadRequerida(),
                itemDTO.getPrecioUnitarioRef());
        hc.addItem(nuevo);

        HojaCompraDTO dto = toDTO(hojaCompraRepository.save(hc));
        notificacionService.crear("HC", "Insumo no presupuestado agregado a " + dto.getNumeroHC(), "COMPRAS", "normal");
        return dto;
    }

    @Override
    public HojaCompraDTO aprobar(Long idHC) {
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));
        hc.aprobar();
        HojaCompraDTO dto = toDTO(hojaCompraRepository.save(hc));
        notificacionService.crear("HC", "Hoja de Compra " + dto.getNumeroHC() + " aprobada", "COMPRAS", "normal");
        return dto;
    }

    @Override
    public HojaCompraDTO cerrar(Long idHC) {
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));
        hc.cerrar();
        return toDTO(hojaCompraRepository.save(hc));
    }

    @Override
    @Transactional
    public HojaCompraDTO reabrir(Long idHC) {
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));
        hc.reabrir();
        return toDTO(hojaCompraRepository.save(hc));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HojaCompraDTO> obtenerPorId(Long idHC) {
        return hojaCompraRepository.findById(idHC).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HojaCompraDTO> obtenerPorOpId(Long opId) {
        return hojaCompraRepository.findByOpId(opId).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HojaCompraDTO> listarTodas() {
        return hojaCompraRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<HojaCompraDTO> listarPorEstado(EstadoHC estado) {
        return hojaCompraRepository.findAllByEstado(estado).stream().map(this::toDTO).collect(Collectors.toList());
    }

    private HojaCompraDTO toDTO(HojaCompra hc) {
        if (hc == null)
            return null;
        return HojaCompraDTO.builder()
                .idHC(hc.getIdHC())
                .numeroHC(hc.getNumeroHC() != null ? hc.getNumeroHC().getValue() : null)
                .opId(hc.getOpId())
                .costeoVersionId(hc.getCosteoVersionId())
                .estado(hc.getEstado())
                .fechaGeneracion(hc.getFechaGeneracion())
                .observaciones(hc.getObservaciones())
                .items(hc.getItems() != null
                        ? hc.getItems().stream().map(this::itemToDTO).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private HojaCompraItemDTO itemToDTO(HojaCompraItem item) {
        return HojaCompraItemDTO.builder()
                .idHCItem(item.getIdHCItem())
                .hcId(item.getHcId())
                .tipoInsumo(item.getTipoInsumo())
                .articuloId(item.getArticuloId())
                .nombreInsumo(item.getNombreInsumo())
                .consumoUnitario(item.getConsumoUnitario())
                .cantidadOP(item.getCantidadOP())
                .cantidadRequerida(item.getCantidadRequerida())
                .precioUnitarioRef(item.getPrecioUnitarioRef())
                .cantidadStock(item.getCantidadStock())
                .cantidadAComprar(item.getCantidadAComprar())
                .modificado(item.getModificado())
                .justificacionModificacion(item.getJustificacionModificacion())
                .proveedorId(item.getProveedorId())
                .proveedorNombre(item.getProveedorNombre())
                .ocId(item.getOcId())
                .numeroOC(item.getNumeroOC())
                .presupuestado(item.getPresupuestado())
                .build();
    }
}
