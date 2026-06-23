package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.application.service.ArticuloService;
import backend.com.shared.domain.enums.TipoArticulo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión maestra de Artículos.
 * Base URL: {@code /api/v3/maestros/articulos}
 */
@RestController
@RequestMapping("/api/v3/maestros/articulos")
@RequiredArgsConstructor
public class ArticuloController {

    private final ArticuloService articuloService;

    @PostMapping
    public ResponseEntity<ArticuloDTO> crear(@Valid @RequestBody ArticuloDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articuloService.crearArticulo(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticuloDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ArticuloDTO request) {
        return ResponseEntity.ok(articuloService.actualizarArticulo(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticuloDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(articuloService.obtenerPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ArticuloDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(articuloService.obtenerPorCodigo(codigo));
    }

    @GetMapping
    public ResponseEntity<List<ArticuloDTO>> listarTodos() {
        return ResponseEntity.ok(articuloService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ArticuloDTO>> listarActivos() {
        return ResponseEntity.ok(articuloService.listarActivos());
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ArticuloDTO>> listarPorTipo(@PathVariable TipoArticulo tipo) {
        return ResponseEntity.ok(articuloService.listarPorTipo(tipo));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<ArticuloDTO>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(articuloService.buscarPorNombre(nombre));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        articuloService.eliminarArticulo(id);
    }
}