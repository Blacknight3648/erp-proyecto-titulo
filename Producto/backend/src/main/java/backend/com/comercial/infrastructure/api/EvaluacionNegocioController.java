package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.application.service.AprobarEVNUseCase;
import backend.com.comercial.application.service.CrearEVNUseCase;
import backend.com.comercial.application.service.AdjudicarEVNUseCase;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
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
@Tag(name = "Comercial - Evaluaciones de Negocio",
     description = "Gestión de EVN: creación, aprobación, rechazo, adjudicación e historial")
public class EvaluacionNegocioController {

    private final CrearEVNUseCase crearEVNUseCase;
    private final AdjudicarEVNUseCase adjudicarEVNUseCase;
    private final AprobarEVNUseCase aprobarEVNUseCase;
    private final EvaluacionNegocioRepository repository;
    private final HistorialEstadoService historialService;

    @GetMapping
    @Operation(summary = "Listar EVN", description = "Devuelve todas las evaluaciones de negocio.")
    public List<EVNResponse> listar() {
        return repository.findAll().stream()
                .map(EVNResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @PostMapping
    @Operation(summary = "Crear EVN", description = "Crea una EVN nueva. El número se asigna atómicamente.")
    public EVNResponse crear(@Valid @RequestBody CrearEVNCommand command) {
        return crearEVNUseCase.ejecutar(command);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener EVN por ID")
    public EVNResponse obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(EVNResponse::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación de Negocio no encontrada: " + id));
    }

    @PatchMapping("/{id}/adjudicar")
    @Operation(summary = "Adjudicar EVN",
            description = "Cambia el estado a ADJUDICADA, genera Nota de Venta y registra firma en historial.")
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

    @GetMapping("/{id}/historial")
    @Operation(summary = "Historial de cambios de estado de la EVN")
    public List<HistorialEstadoDTO> historial(@PathVariable Long id) {
        return historialService.consultar("EVN", id);
    }
}
