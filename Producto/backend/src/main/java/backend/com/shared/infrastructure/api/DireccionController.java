package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.DireccionRequest;
import backend.com.shared.application.dto.DireccionResponse;
import backend.com.shared.application.service.DireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/direcciones")
@RequiredArgsConstructor
public class DireccionController {

    private final DireccionService direccionService;

    @GetMapping
    public ResponseEntity<List<DireccionResponse>> listarTodos() {
        return ResponseEntity.ok(direccionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionResponse> obtenerPorId(@PathVariable Long id) {
        return direccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/comuna/{comunaId}")
    public ResponseEntity<List<DireccionResponse>> listarPorComuna(@PathVariable Long comunaId) {
        return ResponseEntity.ok(direccionService.listarPorComuna(comunaId));
    }

    @PostMapping
    public ResponseEntity<DireccionResponse> crear(@Valid @RequestBody DireccionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionResponse> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody DireccionRequest request) {
        return ResponseEntity.ok(direccionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        direccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
