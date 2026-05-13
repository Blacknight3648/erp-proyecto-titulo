package backend.com.comercial.application.service;

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

                // Buscar Costeo si existe
                if (evn.getCosteoId() != null) {
                    costeoRepository.findById(evn.getCosteoId()).ifPresent(costeo -> {

                        // Buscar SCOS si existe
                        if (costeo.getSolicitudCostosId() != null) {
                            scosRepository.findById(costeo.getSolicitudCostosId()).ifPresent(scos -> {
                                trazabilidad.add(mapToDto("Solicitud Costos", scos.getIdSCOS(),
                                        scos.getNumeroSCOS().getValue().toString(), scos.getEstado(), scos.getFecha()));
                            });
                        }

                        trazabilidad.add(
                                mapToDto("Costeo", costeo.getIdCosteo(), costeo.getNumeroCosteo().getValue().toString(),
                                        "COMPLETO", null));
                    });
                }

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


        // Buscar OTs asociadas (Personalización)
        otRepository.findByNotaVentaId(notaVentaId).forEach(ot -> {
            trazabilidad.add(mapToDto("Orden Trabajo (OT)", ot.getIdOT(), ot.getNumeroOT().getValue().toString(),
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
