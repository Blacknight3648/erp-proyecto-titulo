package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.TipoDireccionRequest;
import backend.com.shared.application.dto.TipoDireccionResponse;
import backend.com.shared.application.service.TipoDireccionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-direccion")
@RequiredArgsConstructor
public class TipoDireccionController {

    private final TipoDireccionService tipoDireccionService;

    @GetMapping
    public ResponseEntity<List<TipoDireccionResponse>> listarTodos() {
        return ResponseEntity.ok(tipoDireccionService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoDireccionResponse> obtenerPorId(@PathVariable Integer id) {
        return tipoDireccionService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoDireccionResponse> crear(@Valid @RequestBody TipoDireccionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoDireccionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoDireccionResponse> actualizar(@PathVariable Integer id,
                                                             @Valid @RequestBody TipoDireccionRequest request) {
        return ResponseEntity.ok(tipoDireccionService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoDireccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
