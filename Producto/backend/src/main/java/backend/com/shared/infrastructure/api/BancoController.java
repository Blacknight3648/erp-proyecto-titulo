package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.BancoRequest;
import backend.com.shared.application.dto.BancoResponse;
import backend.com.shared.application.service.BancoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bancos")
@RequiredArgsConstructor
public class BancoController {

    private final BancoService bancoService;

    @GetMapping
    public ResponseEntity<List<BancoResponse>> listarTodos() {
        return ResponseEntity.ok(bancoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoResponse> obtenerPorId(@PathVariable Integer id) {
        return bancoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BancoResponse> crear(@Valid @RequestBody BancoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bancoService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoResponse> actualizar(@PathVariable Integer id,
                                                     @Valid @RequestBody BancoRequest request) {
        return ResponseEntity.ok(bancoService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bancoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
