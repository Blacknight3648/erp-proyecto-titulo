package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.CosteoVersionInfoDTO;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.dto.TrazabilidadOPDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.application.service.OrdenServicioService;
import backend.com.produccion.application.service.RecepcionOCService;
import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.enums.EstadoCosteo;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TrazabilidadOPUseCase {

    private static final Set<String> ESTADOS_RECHAZO_COSTEO = Set.of("RECHAZADO");
    private static final Set<String> ESTADOS_RECHAZO_OC = Set.of("RECHAZADA");

    private final OrdenProduccionRepository ordenProduccionRepository;
    private final CosteoVersionRepository costeoVersionRepository;
    private final CosteoRepository costeoRepository;
    private final HojaCompraRepository hojaCompraRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenServicioService ordenServicioService;
    private final HojaCompraService hojaCompraService;
    private final RecepcionOCService recepcionOCService;
    private final HistorialEstadoService historialEstadoService;

    @Transactional(readOnly = true)
    public TrazabilidadOPDTO ejecutar(Long opId) {
        if (opId == null) {
            throw new IllegalArgumentException("opId es obligatorio");
        }

        OrdenProduccion op = ordenProduccionRepository.findById(opId)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Producción no encontrada: " + opId));

        CosteoVersionInfoDTO versionInfo = null;
        if (op.getCosteoVersionId() != null) {
            CosteoVersion version = costeoVersionRepository.findById(op.getCosteoVersionId()).orElse(null);
            if (version != null) {
                CosteoVersionInfoDTO.CosteoVersionInfoDTOBuilder versionBuilder = CosteoVersionInfoDTO.builder()
                        .idCosteoVersion(version.getIdCosteoVersion())
                        .costeoId(version.getCosteoId())
                        .numeroVersion(version.getNumeroVersion())
                        .fechaCreacion(version.getFechaCreacion())
                        .motivoCambio(version.getMotivoCambio())
                        .usuarioCreador(version.getUsuarioCreador());

                if (version.getCosteoId() != null) {
                    aplicarRechazoCosteo(versionBuilder, version.getCosteoId());
                }

                versionInfo = versionBuilder.build();
            }
        }

        HojaCompraDTO hcDTO = hojaCompraService.obtenerPorOpId(opId).orElse(null);

        // Buscar todas las OCs que consumen items de esta HC (deduplicadas)
        List<OrdenCompraDTO> ocsDTO = new ArrayList<>();
        List<RecepcionOCDTO> recepcionesDTO = new ArrayList<>();
        if (hcDTO != null) {
            Map<Long, OrdenCompra> ocsUnicas = new LinkedHashMap<>();
            HojaCompra hc = hojaCompraRepository.findByOpId(opId).orElse(null);
            if (hc != null) {
                for (HojaCompraItem hcItem : hc.getItems()) {
                    List<OrdenCompra> ocs = ordenCompraRepository.findAllByHcItemId(hcItem.getIdHCItem());
                    for (OrdenCompra oc : ocs) {
                        ocsUnicas.put(oc.getIdOC(), oc);
                    }
                }
            }
            for (OrdenCompra oc : ocsUnicas.values()) {
                ocsDTO.add(toOCDTO(oc));
                recepcionesDTO.addAll(recepcionOCService.listarPorOC(oc.getIdOC()));
            }
        }

        List<OrdenServicioDTO> ossDTO = ordenServicioService.listarPorOP(opId);

        return TrazabilidadOPDTO.builder()
                .opId(op.getIdOP())
                .numeroOP(op.getNumeroOP() != null ? op.getNumeroOP().getValue() : null)
                .notaVentaId(op.getNotaVentaId())
                .estadoOP(op.getEstado())
                .fechaEntregaProgramada(op.getFechaEntregaProgramada())
                .costeoVersion(versionInfo)
                .hojaCompra(hcDTO)
                .ordenesCompra(ocsDTO)
                .recepcionesOC(recepcionesDTO)
                .ordenesServicio(ossDTO)
                .build();
    }

    /**
     * Convierte el dominio OrdenCompra a DTO mínimo para la vista de trazabilidad
     * (sin recursividad de items, solo metadata principal).
     */
    private OrdenCompraDTO toOCDTO(OrdenCompra oc) {
        OrdenCompraDTO.OrdenCompraDTOBuilder builder = OrdenCompraDTO.builder()
                .idOC(oc.getIdOC())
                .numeroOC(oc.getNumeroOC() != null ? oc.getNumeroOC().getValue() : null)
                .proveedorId(oc.getProveedorId())
                .estado(oc.getEstado())
                .fechaEmision(oc.getFechaEmision())
                .fechaEntregaEstimada(oc.getFechaEntregaEstimada())
                .observaciones(oc.getObservaciones())
                .totalNeto(oc.getTotalNeto())
                .items(List.of())
                .motivoRechazo(oc.getMotivoRechazo());

        if (oc.getEstado() == EstadoOC.RECHAZADA) {
            Optional<HistorialEstadoDTO> transicion = historialEstadoService
                    .ultimaTransicionA("OC", oc.getIdOC(), ESTADOS_RECHAZO_OC);
            if (transicion.isPresent()) {
                HistorialEstadoDTO t = transicion.get();
                builder.motivoRechazo(t.getObservacion() != null ? t.getObservacion() : oc.getMotivoRechazo())
                        .fechaRechazo(t.getFecha());
            }
        }

        return builder.build();
    }

    /**
     * Setea motivo/fecha de rechazo del Costeo padre de una versión, priorizando
     * HistorialEstado y usando el motivoRechazo propio del Costeo como
     * safety-net si no hay registro de historial.
     */
    private void aplicarRechazoCosteo(CosteoVersionInfoDTO.CosteoVersionInfoDTOBuilder versionBuilder,
            Long costeoId) {
        Optional<HistorialEstadoDTO> transicion = historialEstadoService
                .ultimaTransicionA("COSTEO", costeoId, ESTADOS_RECHAZO_COSTEO);
        if (transicion.isPresent()) {
            HistorialEstadoDTO t = transicion.get();
            versionBuilder.motivoRechazoCosteo(t.getObservacion()).fechaRechazoCosteo(t.getFecha());
            return;
        }

        Costeo costeo = costeoRepository.findById(costeoId).orElse(null);
        if (costeo != null && costeo.getEstado() == EstadoCosteo.RECHAZADO) {
            versionBuilder.motivoRechazoCosteo(costeo.getMotivoRechazo());
        }
    }
}
