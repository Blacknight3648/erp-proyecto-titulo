package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.BancoDTO;
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
    public ResponseEntity<List<BancoDTO>> listarTodos() {
        return ResponseEntity.ok(bancoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BancoDTO> obtenerPorId(@PathVariable Integer id) {
        return bancoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<BancoDTO> crear(@Valid @RequestBody BancoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bancoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BancoDTO> actualizar(@PathVariable Integer id,
                                               @Valid @RequestBody BancoDTO dto) {
        return ResponseEntity.ok(bancoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        bancoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
