package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.UseCase.ActualizarEVNUseCase;
import backend.com.comercial.application.UseCase.AdjudicarEVNUseCase;
import backend.com.comercial.application.UseCase.AprobarEVNUseCase;
import backend.com.comercial.application.UseCase.CerrarEVNUseCase;
import backend.com.comercial.application.UseCase.CrearEVNUseCase;
import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comercial/evaluaciones-negocio")
@RequiredArgsConstructor
@Tag(name = "Comercial - Evaluaciones de Negocio", description = "Gestión de EVN: creación, aprobación, rechazo, adjudicación, cierre e historial")
public class EvaluacionNegocioController {

    private final CrearEVNUseCase crearEVNUseCase;
    private final ActualizarEVNUseCase actualizarEVNUseCase;
    private final AdjudicarEVNUseCase adjudicarEVNUseCase;
    private final AprobarEVNUseCase aprobarEVNUseCase;
    private final CerrarEVNUseCase cerrarEVNUseCase;
    private final EvaluacionNegocioRepository repository;
    private final HistorialEstadoService historialService;
    private final NumeroDocumentoService numeroDocumentoService;

    @GetMapping
    @Operation(summary = "Listar EVN", description = "Devuelve todas las evaluaciones de negocio.")
    public List<EVNResponse> listar() {
        return repository.findAll().stream()
                .map(EVNResponse::fromDomain)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve el próximo número tentativo (NO lo reserva ni incrementa el
     * contador). El definitivo lo asigna el backend de forma atómica al hacer POST.
     */
    @GetMapping("/next-number")
    public String previsualizarProximo() {
        return numeroDocumentoService.previsualizar("EVN").getValue();
    }

    @PostMapping
    @Operation(summary = "Crear EVN", description = "Crea una EVN nueva. El número se asigna atómicamente.")
    public EVNResponse crear(@Valid @RequestBody CrearEVNCommand command) {
        return crearEVNUseCase.ejecutar(command);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar EVN", description = "Actualiza los datos de una EVN existente (ítems, costos, gastos). No modifica el número ni el estado.")
    public EVNResponse actualizar(@PathVariable Long id, @Valid @RequestBody CrearEVNCommand command) {
        return actualizarEVNUseCase.ejecutar(id, command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener EVN por ID")
    public EVNResponse obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(EVNResponse::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación de Negocio no encontrada: " + id));
    }

    @PatchMapping("/{id}/adjudicar")
    @Operation(summary = "Adjudicar EVN", description = "Cambia el estado a ADJUDICADA, genera Nota de Venta y registra firma en historial.")
    public EVNResponse adjudicar(@PathVariable Long id,
            @Valid @RequestBody(required = false) FirmaAprobacionRequest body) {
        String aprobador = body != null ? body.getAprobador() : null;
        String observacion = body != null ? body.getObservacion() : null;
        return adjudicarEVNUseCase.ejecutar(id, aprobador, observacion);
    }

    @PatchMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar EVN", description = "Valida campos obligatorios y firma la aprobación.")
    public EVNResponse aprobar(@PathVariable Long id, @Valid @RequestBody FirmaAprobacionRequest body) {
        return aprobarEVNUseCase.aprobar(id, body.getAprobador(), body.getObservacion());
    }

    @PatchMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar EVN", description = "Requiere motivo. Registra la firma en historial.")
    public EVNResponse rechazar(@PathVariable Long id, @Valid @RequestBody FirmaAprobacionRequest body) {
        return aprobarEVNUseCase.rechazar(id, body.getAprobador(), body.getMotivo());
    }

    @PatchMapping("/{id}/cerrar")
    @Operation(summary = "Cerrar EVN", description = "Cierra una EVN ADJUDICADA (estado CERRADA). "
            + "Regla de negocio: solo se puede cerrar desde ADJUDICADA; una vez cerrada, la EVN "
            + "no puede usarse como plantilla ni generar nuevas Notas de Venta. Registra la firma en historial.")
    public EVNResponse cerrar(@PathVariable Long id, @Valid @RequestBody FirmaAprobacionRequest body) {
        return cerrarEVNUseCase.ejecutar(id, body.getAprobador(), body.getObservacion());
    }

    @GetMapping("/{id}/historial")
    @Operation(summary = "Historial de cambios de estado de la EVN")
    public List<HistorialEstadoDTO> historial(@PathVariable Long id) {
        return historialService.consultar("EVN", id);
    }
}
