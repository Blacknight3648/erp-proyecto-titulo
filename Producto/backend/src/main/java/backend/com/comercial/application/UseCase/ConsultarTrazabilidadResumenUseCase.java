package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.NVTrazabilidadResumenDTO;
import backend.com.comercial.application.dto.NVTrazabilidadResumenDTO.HCResumenDTO;
import backend.com.comercial.application.dto.NVTrazabilidadResumenDTO.OCResumenDTO;
import backend.com.comercial.application.dto.NVTrazabilidadResumenDTO.OPResumenDTO;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConsultarTrazabilidadResumenUseCase {

    private static final Set<String> ESTADOS_RECHAZO_NV = Set.of("CANCELADA");
    private static final Set<String> ESTADOS_RECHAZO_EVN = Set.of("RECHAZADA");
    private static final Set<String> ESTADOS_RECHAZO_OC = Set.of("RECHAZADA");

    private final NotaVentaRepository nvRepository;
    private final EvaluacionNegocioRepository evnRepository;
    private final OrdenProduccionRepository opRepository;
    private final HojaCompraRepository hojaCompraRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final HistorialEstadoService historialEstadoService;

    @Transactional(readOnly = true)
    public List<NVTrazabilidadResumenDTO> ejecutar() {
        return nvRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private NVTrazabilidadResumenDTO mapToDto(NotaVenta nv) {
        List<OPResumenDTO> ops = opRepository.findByNotaVentaId(nv.getIdNV()).stream()
                .map(this::mapOP)
                .collect(Collectors.toList());

        NVTrazabilidadResumenDTO.NVTrazabilidadResumenDTOBuilder builder = NVTrazabilidadResumenDTO.builder()
                .idNV(nv.getIdNV())
                .numeroNV(nv.getNumeroNV() != null ? nv.getNumeroNV().getValue() : null)
                .clienteId(nv.getClienteId())
                .clienteNombre("Cliente #" + nv.getClienteId())
                .estado(nv.getEstado() != null ? nv.getEstado().name() : null)
                .fechaEmision(nv.getFechaEmision())
                .ordenesProduccion(ops);

        if (nv.getEstado() != null && ESTADOS_RECHAZO_NV.contains(nv.getEstado().name())) {
            Optional<HistorialEstadoDTO> transicion = historialEstadoService
                    .ultimaTransicionA("NV", nv.getIdNV(), ESTADOS_RECHAZO_NV);
            transicion.ifPresent(t -> builder.motivoRechazo(t.getObservacion()).fechaRechazo(t.getFecha()));
        }

        if (nv.getEvaluacionNegocioId() != null) {
            evnRepository.findById(nv.getEvaluacionNegocioId()).ifPresent(evn -> aplicarEVN(builder, evn));
        }

        return builder.build();
    }

    private void aplicarEVN(NVTrazabilidadResumenDTO.NVTrazabilidadResumenDTOBuilder builder,
            EvaluacionNegocio evn) {
        builder.evaluacionNegocioId(evn.getEvaluacionNegocioId())
                .numeroEVN(evn.getNumeroEvn() != null ? evn.getNumeroEvn().getValue() : null)
                .estadoEVN(evn.getEstado() != null ? evn.getEstado().name() : null);

        if (evn.getEstado() != null && ESTADOS_RECHAZO_EVN.contains(evn.getEstado().name())) {
            Optional<HistorialEstadoDTO> transicion = historialEstadoService
                    .ultimaTransicionA("EVN", evn.getEvaluacionNegocioId(), ESTADOS_RECHAZO_EVN);
            transicion.ifPresent(t -> builder.motivoRechazoEVN(t.getObservacion()).fechaRechazoEVN(t.getFecha()));
        }
    }

    private OPResumenDTO mapOP(OrdenProduccion op) {
        HojaCompra hc = hojaCompraRepository.findByOpId(op.getIdOP()).orElse(null);

        HCResumenDTO hcDTO = null;
        List<OCResumenDTO> ocsDTO = List.of();
        if (hc != null) {
            hcDTO = HCResumenDTO.builder()
                    .idHC(hc.getIdHC())
                    .numeroHC(hc.getNumeroHC() != null ? hc.getNumeroHC().getValue() : null)
                    .estado(hc.getEstado() != null ? hc.getEstado().name() : null)
                    .build();

            Map<Long, OrdenCompra> ocsUnicas = new LinkedHashMap<>();
            for (HojaCompraItem hcItem : hc.getItems()) {
                for (OrdenCompra oc : ordenCompraRepository.findAllByHcItemId(hcItem.getIdHCItem())) {
                    ocsUnicas.put(oc.getIdOC(), oc);
                }
            }
            ocsDTO = ocsUnicas.values().stream()
                    .map(this::mapOC)
                    .collect(Collectors.toList());
        }

        return OPResumenDTO.builder()
                .idOP(op.getIdOP())
                .numeroOP(op.getNumeroOP() != null ? op.getNumeroOP().getValue() : null)
                .estado(op.getEstado() != null ? op.getEstado().name() : null)
                .fechaEntregaProgramada(op.getFechaEntregaProgramada())
                .hojaCompra(hcDTO)
                .ordenesCompra(ocsDTO)
                .build();
    }

    private OCResumenDTO mapOC(OrdenCompra oc) {
        OCResumenDTO.OCResumenDTOBuilder ocBuilder = OCResumenDTO.builder()
                .idOC(oc.getIdOC())
                .numeroOC(oc.getNumeroOC() != null ? oc.getNumeroOC().getValue() : null)
                .estado(oc.getEstado() != null ? oc.getEstado().name() : null)
                .motivoRechazo(oc.getMotivoRechazo());

        if (oc.getEstado() != null && ESTADOS_RECHAZO_OC.contains(oc.getEstado().name())) {
            Optional<HistorialEstadoDTO> transicion = historialEstadoService
                    .ultimaTransicionA("OC", oc.getIdOC(), ESTADOS_RECHAZO_OC);
            if (transicion.isPresent()) {
                HistorialEstadoDTO t = transicion.get();
                ocBuilder.motivoRechazo(t.getObservacion() != null ? t.getObservacion() : oc.getMotivoRechazo())
                        .fechaRechazo(t.getFecha());
            }
        }

        return ocBuilder.build();
    }
}
