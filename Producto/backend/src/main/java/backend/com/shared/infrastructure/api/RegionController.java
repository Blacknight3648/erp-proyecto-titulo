package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.RegionRequest;
import backend.com.shared.application.dto.RegionResponse;
import backend.com.shared.application.service.RegionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/regiones")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping
    public ResponseEntity<List<RegionResponse>> listarTodos() {
        return ResponseEntity.ok(regionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionResponse> obtenerPorId(@PathVariable Long id) {
        return regionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pais/{paisId}")
    public ResponseEntity<List<RegionResponse>> listarPorPais(@PathVariable Integer paisId) {
        return ResponseEntity.ok(regionService.listarPorPais(paisId));
    }

    @PostMapping
    public ResponseEntity<RegionResponse> crear(@Valid @RequestBody RegionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionResponse> actualizar(@PathVariable Long id,
                                                      @Valid @RequestBody RegionRequest request) {
        return ResponseEntity.ok(regionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        regionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
