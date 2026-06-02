package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.domain.model.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EVNBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AprobarEVNUseCase {

    public static final String TIPO_ENTIDAD = "EVN";

    private final EvaluacionNegocioRepository evnRepository;
    private final HistorialEstadoService historialService;

    @Transactional
    public EVNResponse aprobar(Long id, String aprobador, String observacion) {
        if (aprobador == null || aprobador.isBlank()) {
            throw new EVNBusinessException("Se requiere identificar al aprobador para firmar la EVN");
        }

        EvaluacionNegocio evn = evnRepository.findById(id)
                .orElseThrow(() -> new EVNBusinessException("Evaluación de Negocio no encontrada: " + id));

        validarCamposObligatorios(evn);

        EstadoEVN estadoAnterior = evn.getEstado();
        evn.aprobar();
        evnRepository.save(evn);

        historialService.registrar(TIPO_ENTIDAD, evn.getEvaluacionNegocioId(),
                estadoAnterior != null ? estadoAnterior.name() : null,
                evn.getEstado().name(),
                aprobador,
                observacion != null ? observacion : "Aprobación firmada por " + aprobador);

        return EVNResponse.fromDomain(evn);
    }

    @Transactional
    public EVNResponse rechazar(Long id, String aprobador, String motivo) {
        if (aprobador == null || aprobador.isBlank()) {
            throw new EVNBusinessException("Se requiere identificar al usuario que rechaza la EVN");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new EVNBusinessException("Se requiere un motivo para rechazar la EVN");
        }

        EvaluacionNegocio evn = evnRepository.findById(id)
                .orElseThrow(() -> new EVNBusinessException("Evaluación de Negocio no encontrada: " + id));

        EstadoEVN estadoAnterior = evn.getEstado();
        evn.rechazar();
        evnRepository.save(evn);

        historialService.registrar(TIPO_ENTIDAD, evn.getEvaluacionNegocioId(),
                estadoAnterior != null ? estadoAnterior.name() : null,
                evn.getEstado().name(),
                aprobador,
                motivo);

        return EVNResponse.fromDomain(evn);
    }

    private void validarCamposObligatorios(EvaluacionNegocio evn) {
        if (evn.getClienteId() == null) {
            throw new EVNBusinessException("El cliente es obligatorio para aprobar");
        }
        if (evn.getVendedorId() == null) {
            throw new EVNBusinessException("El vendedor es obligatorio para aprobar");
        }
        if (evn.getItems() == null || evn.getItems().isEmpty()) {
            throw new EVNBusinessException("La EVN debe tener al menos un ítem para ser aprobada");
        }
        for (ItemEVN item : evn.getItems()) {
            if (item.getCantidad() == null || item.getCantidad() <= 0) {
                throw new EVNBusinessException("Todos los ítems deben tener cantidad mayor a cero");
            }
            if (item.getPrecioUnitario() == null) {
                throw new EVNBusinessException("Todos los ítems deben tener precio unitario");
            }
            if (item.getTechnicalSpecs() == null || item.getTechnicalSpecs().isEmpty()) {
                throw new EVNBusinessException(
                        "Cada ítem debe tener su ficha técnica completa antes de aprobar");
            }
        }
    }
}
