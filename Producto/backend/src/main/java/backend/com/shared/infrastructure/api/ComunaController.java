package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.ComunaRequest;
import backend.com.shared.application.dto.ComunaResponse;
import backend.com.shared.application.service.ComunaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comunas")
@RequiredArgsConstructor
public class ComunaController {

    private final ComunaService comunaService;

    @GetMapping
    public ResponseEntity<List<ComunaResponse>> listarTodos() {
        return ResponseEntity.ok(comunaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComunaResponse> obtenerPorId(@PathVariable Long id) {
        return comunaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/region/{regionId}")
    public ResponseEntity<List<ComunaResponse>> listarPorRegion(@PathVariable Long regionId) {
        return ResponseEntity.ok(comunaService.listarPorRegion(regionId));
    }

    @PostMapping
    public ResponseEntity<ComunaResponse> crear(@Valid @RequestBody ComunaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(comunaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ComunaResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody ComunaRequest request) {
        return ResponseEntity.ok(comunaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        comunaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
