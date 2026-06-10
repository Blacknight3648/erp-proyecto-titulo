package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.domain.enums.EstadoNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.application.UseCase.GestionarOrdenTrabajoUseCase;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GestionarNVUseCase {

    public static final String TIPO_ENTIDAD = "NV";

    private final NotaVentaRepository repository;
    private final HistorialEstadoService historialService;
    private final GestionarOrdenTrabajoUseCase gestionarOTUseCase;

    @Transactional
    public NVResponse aprobar(Long id, String aprobador, String observacion) {
        if (aprobador == null || aprobador.isBlank()) {
            throw new ValidationException("Se requiere identificar al aprobador para firmar la NV");
        }
        NotaVenta nv = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota de Venta no encontrada: " + id));

        EstadoNV anterior = nv.getEstado();
        nv.aprobar();
        repository.save(nv);

        // Al aprobar la NV se generan las OT de modificación de los ítems que las
        // requieren (requiereOt). aprobar() solo transiciona desde BORRADOR (one-shot),
        // por lo que esto no duplica OT en reaprobaciones.
        gestionarOTUseCase.generarDesdeNotaVenta(nv);

        historialService.registrar(TIPO_ENTIDAD, nv.getIdNV(),
                anterior != null ? anterior.name() : null,
                nv.getEstado().name(),
                aprobador,
                observacion != null ? observacion : "Aprobación firmada por " + aprobador);

        return NVResponse.fromDomain(nv);
    }

    @Transactional
    public NVResponse cancelar(Long id, String usuario, String motivo) {
        if (usuario == null || usuario.isBlank()) {
            throw new ValidationException("Se requiere identificar al usuario que cancela");
        }
        if (motivo == null || motivo.isBlank()) {
            throw new ValidationException("Se requiere un motivo para cancelar");
        }
        NotaVenta nv = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nota de Venta no encontrada: " + id));

        EstadoNV anterior = nv.getEstado();
        nv.cancelar();
        repository.save(nv);

        historialService.registrar(TIPO_ENTIDAD, nv.getIdNV(),
                anterior != null ? anterior.name() : null,
                nv.getEstado().name(),
                usuario,
                motivo);

        return NVResponse.fromDomain(nv);
    }
}
