package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.ModeloDTO;
import backend.com.shared.application.service.ModeloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maestros/modelos")
@RequiredArgsConstructor
public class ModeloController {

    private final ModeloService modeloService;

    @PostMapping
    public ResponseEntity<ModeloDTO> crear(@Valid @RequestBody ModeloDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modeloService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ModeloDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ModeloDTO request) {
        return ResponseEntity.ok(modeloService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModeloDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(modeloService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ModeloDTO>> listarTodos() {
        return ResponseEntity.ok(modeloService.listarTodos());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        modeloService.eliminar(id);
    }
}
