package backend.com.comercial.application.UseCase;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.shared.application.dto.DocumentTraceDTO;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ConsultarTrazabilidadUseCase {

    private final NotaVentaRepository nvRepository;
    private final EvaluacionNegocioRepository evnRepository;
    private final CosteoRepository costeoRepository;
    private final SolicitudCostosRepository scosRepository;
    private final OrdenProduccionRepository opRepository;
    private final OrdenTrabajoRepository otRepository;
    private final HistorialEstadoService historialEstadoService;

    @Transactional(readOnly = true)
    public List<DocumentTraceDTO> ejecutar(Long notaVentaId) {
        NotaVenta nv = nvRepository.findById(notaVentaId)
                .orElseThrow(() -> new EntityNotFoundException("Nota de Venta no encontrada: " + notaVentaId));

        List<DocumentTraceDTO> trazabilidad = new ArrayList<>();

        // 1. Reconstruir hacia atrás (Ancestros)
        if (nv.getEvaluacionNegocioId() != null) {
            evnRepository.findById(nv.getEvaluacionNegocioId()).ifPresent(evn -> {

                // Buscar Costeos vinculados a los ítems tipo OP de la EVN
                evn.getItems().stream()
                        .filter(i -> TipoItem.OP == i.getTipoItem() && i.getCosteoId() != null)
                        .map(backend.com.comercial.domain.model.ItemEVN::getCosteoId)
                        .distinct()
                        .forEach(costeoId -> {

                            costeoRepository.findById(costeoId).ifPresent(costeo -> {

                                // Buscar SCOS si existe
                                if (costeo.getSolicitudCostosId() != null) {
                                    scosRepository.findById(costeo.getSolicitudCostosId()).ifPresent(scos -> {
                                        trazabilidad.add(mapToDto("Solicitud Costos", scos.getIdSCOS(),
                                                scos.getNumeroSCOS().getValue().toString(),
                                                scos.getEstado() != null ? scos.getEstado().name() : null,
                                                scos.getFecha(), null));
                                    });
                                }

                                trazabilidad.add(
                                        mapToDto("Costeo", costeo.getIdCosteo(),
                                                costeo.getNumeroCosteo().getValue().toString(),
                                                costeo.getEstado() != null ? costeo.getEstado().name() : null,
                                                null, "COSTEO"));
                            });

                        });

                trazabilidad.add(mapToDto("Evaluación Negocio", evn.getEvaluacionNegocioId(),
                        evn.getNumeroEvn().getValue().toString(),
                        evn.getEstado().name(), evn.getFechaEvaluacion(), "EVN"));
            });
        }

        // 2. Nota de Venta (Eje Central)
        trazabilidad.add(mapToDto("Nota Venta", nv.getIdNV(), nv.getNumeroNV().toString(), nv.getEstado().name(),
                nv.getFechaEmision(), "NV"));

        // 3. Reconstruir hacia adelante (Sucesores)

        // Buscar OPs asociadas
        List<OrdenProduccion> ops = opRepository.findByNotaVentaId(notaVentaId);
        for (OrdenProduccion op : ops) {
            trazabilidad.add(mapToDto("Orden Producción", op.getIdOP(), op.getNumeroOP().getValue().toString(),
                    op.getEstado().name(), op.getFechaInicio(), null));
        }

        // Buscar OTs asociadas (Personalización). La OT es un registro sin número
        // propio: se referencia por su id y, si existe, por su fase de producción.
        otRepository.findByNotaVentaId(notaVentaId).forEach(ot -> {
            String referencia = ot.getFase() != null
                    ? "OT #" + ot.getIdOT() + " - " + ot.getFase().name()
                    : "OT #" + ot.getIdOT();
            trazabilidad.add(mapToDto("Orden Trabajo (OT)", ot.getIdOT(), referencia,
                    ot.getEstadoOT().name(), null, null));
        });

        return trazabilidad;
    }

    private static final Set<String> ESTADOS_RECHAZO_COSTEO = Set.of("RECHAZADO");
    private static final Set<String> ESTADOS_RECHAZO_EVN = Set.of("RECHAZADA");
    private static final Set<String> ESTADOS_RECHAZO_NV = Set.of("CANCELADA");

    /**
     * @param tipoEntidadHistorial tipoEntidad usado en HistorialEstado para
     *                             buscar el motivo/fecha de rechazo de este
     *                             documento, o null si no aplica (SCOS, OP, OT).
     */
    private DocumentTraceDTO mapToDto(String tipo, Long id, String numero, String estado,
            java.time.LocalDate fecha, String tipoEntidadHistorial) {
        DocumentTraceDTO.DocumentTraceDTOBuilder builder = DocumentTraceDTO.builder()
                .tipoDocumento(tipo)
                .id(id)
                .numero(numero)
                .estado(estado)
                .fecha(fecha);

        if (tipoEntidadHistorial != null) {
            Set<String> estadosDestino = switch (tipoEntidadHistorial) {
                case "COSTEO" -> ESTADOS_RECHAZO_COSTEO;
                case "EVN" -> ESTADOS_RECHAZO_EVN;
                case "NV" -> ESTADOS_RECHAZO_NV;
                default -> Set.<String>of();
            };
            if (!estadosDestino.isEmpty()) {
                Optional<HistorialEstadoDTO> transicion = historialEstadoService
                        .ultimaTransicionA(tipoEntidadHistorial, id, estadosDestino);
                if (transicion.isPresent()) {
                    builder.motivoRechazo(transicion.get().getObservacion())
                            .fechaRechazo(transicion.get().getFecha());
                }
            }
        }

        return builder.build();
    }
}
