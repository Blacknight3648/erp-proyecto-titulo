package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.DireccionDTO;
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
    public ResponseEntity<List<DireccionDTO>> listarTodos() {
        return ResponseEntity.ok(direccionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DireccionDTO> obtenerPorId(@PathVariable Long id) {
        return direccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/comuna/{comunaId}")
    public ResponseEntity<List<DireccionDTO>> listarPorComuna(@PathVariable Long comunaId) {
        return ResponseEntity.ok(direccionService.listarPorComuna(comunaId));
    }

    @PostMapping
    public ResponseEntity<DireccionDTO> crear(@Valid @RequestBody DireccionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(direccionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DireccionDTO> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody DireccionDTO dto) {
        return ResponseEntity.ok(direccionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        direccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}