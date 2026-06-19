package backend.com.comercial.application.UseCase;

import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.shared.application.service.HistorialEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdjudicarEVNUseCase {

    private final EvaluacionNegocioRepository evnRepository;
    private final HistorialEstadoService historialService;

    @Transactional
    public EVNResponse ejecutar(Long evnId) {
        return ejecutar(evnId, null, null);
    }

    @Transactional
    public EVNResponse ejecutar(Long evnId, String aprobador, String observacion) {
        EvaluacionNegocio evn = evnRepository.findById(evnId)
                .orElseThrow(() -> new IllegalArgumentException("Evaluación de Negocio no encontrada: " + evnId));

        EstadoEVN estadoAnterior = evn.getEstado();

        // 1. Cambiar estado a ADJUDICADA
        evn.adjudicar();
        evnRepository.save(evn);

        historialService.registrar("EVN", evn.getEvaluacionNegocioId(),
                estadoAnterior != null ? estadoAnterior.name() : null,
                evn.getEstado().name(),
                aprobador != null && !aprobador.isBlank() ? aprobador : "sistema",
                observacion != null && !observacion.isBlank()
                        ? observacion
                        : "Adjudicación de la Evaluación de Negocio");

        // La Nota de Venta ya NO se crea automáticamente al adjudicar: el usuario comercial
        // la genera manualmente desde el frontend usando la EVN adjudicada como plantilla.
        return EVNResponse.fromDomain(evn);
    }
}
