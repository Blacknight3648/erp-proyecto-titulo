package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.AtributoAccesorioDefinicionDTO;
import backend.com.shared.application.service.AtributoAccesorioDefinicionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la definición de los campos/atributos dinámicos que
 * aplican a cada Tipo de Accesorio (ej. Cierre: Tipo/N°/Terminal/Carro/
 * Medida/Color). El frontend usa {@code GET /por-tipo/{idTipoAccesorio}}
 * para saber qué campos dibujar al crear un Insumo de ese subtipo.
 *
 * <p>Base URL: {@code /api/v1/maestros/atributos-accesorio-definicion}</p>
 */
@RestController
@RequestMapping("/api/v1/maestros/atributos-accesorio-definicion")
@RequiredArgsConstructor
public class AtributoAccesorioDefinicionController {

    private final AtributoAccesorioDefinicionService definicionService;

    @PostMapping
    public ResponseEntity<AtributoAccesorioDefinicionDTO> crear(
            @Valid @RequestBody AtributoAccesorioDefinicionDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(definicionService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtributoAccesorioDefinicionDTO> actualizar(
            @PathVariable Integer id, @Valid @RequestBody AtributoAccesorioDefinicionDTO request) {
        return ResponseEntity.ok(definicionService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtributoAccesorioDefinicionDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(definicionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AtributoAccesorioDefinicionDTO>> listarTodos() {
        return ResponseEntity.ok(definicionService.listarTodos());
    }

    @GetMapping("/por-tipo/{idTipoAccesorio}")
    public ResponseEntity<List<AtributoAccesorioDefinicionDTO>> listarPorTipoAccesorio(
            @PathVariable Integer idTipoAccesorio) {
        return ResponseEntity.ok(definicionService.listarPorTipoAccesorio(idTipoAccesorio));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        definicionService.eliminar(id);
    }
}
