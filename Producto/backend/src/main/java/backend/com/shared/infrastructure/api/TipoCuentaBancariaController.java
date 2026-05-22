package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.TipoCuentaBancariaRequest;
import backend.com.shared.application.dto.TipoCuentaBancariaResponse;
import backend.com.shared.application.service.TipoCuentaBancariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-cuenta-bancaria")
@RequiredArgsConstructor
public class TipoCuentaBancariaController {

    private final TipoCuentaBancariaService tipoCuentaBancariaService;

    @GetMapping
    public ResponseEntity<List<TipoCuentaBancariaResponse>> listarTodos() {
        return ResponseEntity.ok(tipoCuentaBancariaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCuentaBancariaResponse> obtenerPorId(@PathVariable Integer id) {
        return tipoCuentaBancariaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoCuentaBancariaResponse> crear(
            @Valid @RequestBody TipoCuentaBancariaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoCuentaBancariaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCuentaBancariaResponse> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TipoCuentaBancariaRequest request) {
        return ResponseEntity.ok(tipoCuentaBancariaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoCuentaBancariaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
