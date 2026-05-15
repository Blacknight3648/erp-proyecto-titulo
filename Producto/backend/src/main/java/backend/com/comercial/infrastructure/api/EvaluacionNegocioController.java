package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.application.service.AprobarEVNUseCase;
import backend.com.comercial.application.service.CrearEVNUseCase;
import backend.com.comercial.application.service.AdjudicarEVNUseCase;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comercial/evaluaciones-negocio")
@RequiredArgsConstructor
public class EvaluacionNegocioController {

    private final CrearEVNUseCase crearEVNUseCase;
    private final AdjudicarEVNUseCase adjudicarEVNUseCase;
    private final AprobarEVNUseCase aprobarEVNUseCase;
    private final EvaluacionNegocioRepository repository;
    private final HistorialEstadoService historialService;

    @GetMapping
    public ResponseEntity<java.util.List<EVNResponse>> listar() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(EVNResponse::fromDomain)
                .collect(java.util.stream.Collectors.toList()));
    }

    @PostMapping
    public ResponseEntity<EVNResponse> crear(@RequestBody CrearEVNCommand command) {
        return ResponseEntity.ok(crearEVNUseCase.ejecutar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EVNResponse> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(EVNResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/adjudicar")
    public ResponseEntity<EVNResponse> adjudicar(@PathVariable Long id,
            @RequestBody(required = false) java.util.Map<String, String> body) {
        String aprobador = body != null ? body.get("aprobador") : null;
        String observacion = body != null ? body.get("observacion") : null;
        return ResponseEntity.ok(adjudicarEVNUseCase.ejecutar(id, aprobador, observacion));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<EVNResponse> aprobar(@PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        String aprobador = body.get("aprobador");
        String observacion = body.get("observacion");
        return ResponseEntity.ok(aprobarEVNUseCase.aprobar(id, aprobador, observacion));
    }

    @PatchMapping("/{id}/rechazar")
    public ResponseEntity<EVNResponse> rechazar(@PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        String aprobador = body.get("aprobador");
        String motivo = body.get("motivo");
        return ResponseEntity.ok(aprobarEVNUseCase.rechazar(id, aprobador, motivo));
    }

    @GetMapping("/{id}/historial")
    public java.util.List<HistorialEstadoDTO> historial(@PathVariable Long id) {
        return historialService.consultar("EVN", id);
    }
}
