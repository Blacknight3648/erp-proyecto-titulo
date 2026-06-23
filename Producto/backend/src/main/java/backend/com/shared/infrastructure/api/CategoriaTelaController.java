package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.CategoriaTelaDTO;
import backend.com.shared.application.service.CategoriaTelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/maestros/categorias-tela")
@RequiredArgsConstructor
public class CategoriaTelaController {

    private final CategoriaTelaService categoriaTelaService;

    @PostMapping
    public ResponseEntity<CategoriaTelaDTO> crear(@Valid @RequestBody CategoriaTelaDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaTelaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaTelaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaTelaDTO request) {
        return ResponseEntity.ok(categoriaTelaService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaTelaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaTelaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaTelaDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaTelaService.listarTodas());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        categoriaTelaService.eliminar(id);
    }
}
