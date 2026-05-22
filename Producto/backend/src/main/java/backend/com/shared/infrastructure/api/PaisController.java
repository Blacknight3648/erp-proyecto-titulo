package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.PaisRequest;
import backend.com.shared.application.dto.PaisResponse;
import backend.com.shared.application.service.PaisService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/paises")
@RequiredArgsConstructor
public class PaisController {

    private final PaisService paisService;

    @GetMapping
    public ResponseEntity<List<PaisResponse>> listarTodos() {
        return ResponseEntity.ok(paisService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaisResponse> obtenerPorId(@PathVariable Integer id) {
        return paisService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PaisResponse> crear(@Valid @RequestBody PaisRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paisService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaisResponse> actualizar(@PathVariable Integer id,
                                                    @Valid @RequestBody PaisRequest request) {
        return ResponseEntity.ok(paisService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        paisService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
