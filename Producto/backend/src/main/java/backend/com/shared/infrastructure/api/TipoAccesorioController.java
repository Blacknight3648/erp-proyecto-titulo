package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.TipoAccesorioDTO;
import backend.com.shared.application.service.TipoAccesorioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Tipos de Accesorio
 * (Cierre, Botón, Broche, Velcro, etc.).
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/tipos-accesorio}
 * </p>
 */
@RestController
@RequestMapping("/api/v1/maestros/tipos-accesorio")
@RequiredArgsConstructor
public class TipoAccesorioController {

    private final TipoAccesorioService tipoAccesorioService;

    @PostMapping
    public ResponseEntity<TipoAccesorioDTO> crear(@Valid @RequestBody TipoAccesorioDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tipoAccesorioService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoAccesorioDTO> actualizar(
            @PathVariable Integer id, @Valid @RequestBody TipoAccesorioDTO request) {
        return ResponseEntity.ok(tipoAccesorioService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoAccesorioDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoAccesorioService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<TipoAccesorioDTO>> listarTodos() {
        return ResponseEntity.ok(tipoAccesorioService.listarTodos());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        tipoAccesorioService.eliminar(id);
    }
}
