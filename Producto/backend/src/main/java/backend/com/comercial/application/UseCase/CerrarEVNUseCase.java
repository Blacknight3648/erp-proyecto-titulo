package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EVNBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Cierra una Evaluación de Negocio. Una vez CERRADA, la EVN deja de poder usarse
 * como plantilla y no admite la generación de nuevas Notas de Venta.
 * Solo se permite cerrar desde el estado ADJUDICADA.
 */
@Service
@RequiredArgsConstructor
public class CerrarEVNUseCase {

    public static final String TIPO_ENTIDAD = "EVN";

    private final EvaluacionNegocioRepository evnRepository;
    private final HistorialEstadoService historialService;

    @Transactional
    public EVNResponse ejecutar(Long id, String aprobador, String observacion) {
        if (aprobador == null || aprobador.isBlank()) {
            throw new EVNBusinessException("Se requiere identificar al usuario que cierra la EVN");
        }

        EvaluacionNegocio evn = evnRepository.findById(id)
                .orElseThrow(() -> new EVNBusinessException("Evaluación de Negocio no encontrada: " + id));

        EstadoEVN estadoAnterior = evn.getEstado();
        evn.cerrar();
        evnRepository.save(evn);

        historialService.registrar(TIPO_ENTIDAD, evn.getEvaluacionNegocioId(),
                estadoAnterior != null ? estadoAnterior.name() : null,
                evn.getEstado().name(),
                aprobador,
                observacion != null && !observacion.isBlank()
                        ? observacion
                        : "Cierre de la Evaluación de Negocio");

        return EVNResponse.fromDomain(evn);
    }
}
