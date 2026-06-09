package backend.com.produccion.application.service.impl;

import backend.com.produccion.application.UseCase.GenerarHCDesdeOPUseCase;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.HojaCompraItemDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.domain.model.EstadoHC;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.shared.exception.EntityNotFoundException;
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

    @Override
    public HojaCompraDTO generarDesdeOP(Long opId) {
        return toDTO(generarHCDesdeOPUseCase.ejecutar(opId));
    }

    @Override
    public HojaCompraDTO aprobar(Long idHC) {
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));
        hc.aprobar();
        return toDTO(hojaCompraRepository.save(hc));
    }

    @Override
    public HojaCompraDTO cerrar(Long idHC) {
        HojaCompra hc = hojaCompraRepository.findById(idHC)
                .orElseThrow(() -> new EntityNotFoundException("Hoja de Compra no encontrada: " + idHC));
        hc.cerrar();
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
                .build();
    }
}
