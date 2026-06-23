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
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultarTrazabilidadUseCase {

    private final NotaVentaRepository nvRepository;
    private final EvaluacionNegocioRepository evnRepository;
    private final CosteoRepository costeoRepository;
    private final SolicitudCostosRepository scosRepository;
    private final OrdenProduccionRepository opRepository;
    private final OrdenTrabajoRepository otRepository;

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
                                                scos.getFecha()));
                                    });
                                }

                                trazabilidad.add(
                                        mapToDto("Costeo", costeo.getIdCosteo(),
                                                costeo.getNumeroCosteo().getValue().toString(),
                                                "COMPLETO", null));
                            });

                        });

                trazabilidad.add(mapToDto("Evaluación Negocio", evn.getEvaluacionNegocioId(),
                        evn.getNumeroEvn().getValue().toString(),
                        evn.getEstado().name(), evn.getFechaEvaluacion()));
            });
        }

        // 2. Nota de Venta (Eje Central)
        trazabilidad
                .add(mapToDto("Nota Venta", nv.getIdNV(), nv.getNumeroNV().toString(), nv.getEstado().name(),
                        nv.getFechaEmision()));

        // 3. Reconstruir hacia adelante (Sucesores)

        // Buscar OPs asociadas
        List<OrdenProduccion> ops = opRepository.findByNotaVentaId(notaVentaId);
        for (OrdenProduccion op : ops) {
            trazabilidad.add(mapToDto("Orden Producción", op.getIdOP(), op.getNumeroOP().getValue().toString(),
                    op.getEstado().name(), op.getFechaInicio()));
        }

        // Buscar OTs asociadas (Personalización). La OT es un registro sin número
        // propio: se referencia por su id y, si existe, por su fase de producción.
        otRepository.findByNotaVentaId(notaVentaId).forEach(ot -> {
            String referencia = ot.getFase() != null
                    ? "OT #" + ot.getIdOT() + " - " + ot.getFase().name()
                    : "OT #" + ot.getIdOT();
            trazabilidad.add(mapToDto("Orden Trabajo (OT)", ot.getIdOT(), referencia,
                    ot.getEstadoOT().name(), null));
        });

        return trazabilidad;
    }

    private DocumentTraceDTO mapToDto(String tipo, Long id, String numero, String estado, java.time.LocalDate fecha) {
        return DocumentTraceDTO.builder()
                .tipoDocumento(tipo)
                .id(id)
                .numero(numero)
                .estado(estado)
                .fecha(fecha)
                .build();
    }
}
