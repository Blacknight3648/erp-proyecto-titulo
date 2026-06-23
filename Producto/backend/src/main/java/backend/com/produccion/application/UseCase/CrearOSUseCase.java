package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.CrearOSRequest;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenServicio;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.OrdenServicioRepository;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CrearOSUseCase {

    private final OrdenProduccionRepository ordenProduccionRepository;
    private final OrdenServicioRepository ordenServicioRepository;

    @Transactional
    public OrdenServicio ejecutar(CrearOSRequest request) {
        validar(request);

        OrdenProduccion op = ordenProduccionRepository.findById(request.getOpId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Orden de Producción no encontrada: " + request.getOpId()));

        DocumentNumber numeroOS = construirNumeroOS(request);

        OrdenServicio os = OrdenServicio.emitir(
                numeroOS,
                request.getOpId(),
                request.getProveedorId(),
                request.getTipoServicio(),
                request.getFechaEntregaEstimada(),
                request.getDescripcionTrabajo(),
                request.getCantidadPactada(),
                request.getPrecioUnitario(),
                request.getObservaciones());

        return ordenServicioRepository.save(os);
    }

    private void validar(CrearOSRequest request) {
        if (request == null) {
            throw new ValidationException("El request no puede ser nulo");
        }
        if (request.getCantidadPactada() == null || request.getCantidadPactada() <= 0) {
            throw new ValidationException("La cantidad pactada debe ser positiva");
        }
    }

    private DocumentNumber construirNumeroOS(CrearOSRequest request) {
        return new DocumentNumber(
                "OS-" + System.currentTimeMillis());
    }
}
