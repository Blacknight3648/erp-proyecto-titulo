package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.TipoArticuloDTO;
import backend.com.shared.application.service.TipoArticuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maestros/tipos-articulo")
@RequiredArgsConstructor
public class TipoArticuloController {

    private final TipoArticuloService tipoArticuloService;

    @PostMapping
    public ResponseEntity<TipoArticuloDTO> crear(@Valid @RequestBody TipoArticuloDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoArticuloService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoArticuloDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TipoArticuloDTO request) {
        return ResponseEntity.ok(tipoArticuloService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoArticuloDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoArticuloService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoArticuloDTO>> listarTodos() {
        return ResponseEntity.ok(tipoArticuloService.listarTodos());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        tipoArticuloService.eliminar(id);
    }
}
