package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.TipoCuentaBancariaDTO;
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
    public ResponseEntity<List<TipoCuentaBancariaDTO>> listarTodos() {
        return ResponseEntity.ok(tipoCuentaBancariaService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoCuentaBancariaDTO> obtenerPorId(@PathVariable Integer id) {
        return tipoCuentaBancariaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TipoCuentaBancariaDTO> crear(
            @Valid @RequestBody TipoCuentaBancariaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(tipoCuentaBancariaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoCuentaBancariaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TipoCuentaBancariaDTO dto) {
        return ResponseEntity.ok(tipoCuentaBancariaService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoCuentaBancariaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}