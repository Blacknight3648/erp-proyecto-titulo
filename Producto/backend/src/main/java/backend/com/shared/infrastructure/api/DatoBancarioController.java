package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.DatoBancarioDTO;
import backend.com.shared.application.service.DatoBancarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/datos-bancarios")
@RequiredArgsConstructor
public class DatoBancarioController {

    private final DatoBancarioService datoBancarioService;

    @GetMapping
    public ResponseEntity<List<DatoBancarioDTO>> listarTodos() {
        return ResponseEntity.ok(datoBancarioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatoBancarioDTO> obtenerPorId(@PathVariable Integer id) {
        return datoBancarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/banco/{bancoId}")
    public ResponseEntity<List<DatoBancarioDTO>> listarPorBanco(@PathVariable Integer bancoId) {
        return ResponseEntity.ok(datoBancarioService.listarPorBanco(bancoId));
    }

    @PostMapping
    public ResponseEntity<DatoBancarioDTO> crear(@Valid @RequestBody DatoBancarioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(datoBancarioService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DatoBancarioDTO> actualizar(@PathVariable Integer id,
                                                      @Valid @RequestBody DatoBancarioDTO dto) {
        return ResponseEntity.ok(datoBancarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        datoBancarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}