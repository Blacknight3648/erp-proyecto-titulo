package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.RegionDTO;
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
    public ResponseEntity<List<RegionDTO>> listarTodos() {
        return ResponseEntity.ok(regionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionDTO> obtenerPorId(@PathVariable Long id) {
        return regionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/pais/{paisId}")
    public ResponseEntity<List<RegionDTO>> listarPorPais(@PathVariable Integer paisId) {
        return ResponseEntity.ok(regionService.listarPorPais(paisId));
    }

    @PostMapping
    public ResponseEntity<RegionDTO> crear(@Valid @RequestBody RegionDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(regionService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionDTO> actualizar(@PathVariable Long id,
                                                @Valid @RequestBody RegionDTO dto) {
        return ResponseEntity.ok(regionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        regionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}