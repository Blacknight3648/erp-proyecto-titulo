package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.ComposicionDTO;
import backend.com.shared.application.service.ComposicionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Composiciones textiles.
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/composiciones}
 * </p>
 *
 * <p>
 * La composición describe los materiales que componen una tela
 * (ej: 100% Algodón, 50% Poliéster / 50% Algodón).
 * El código de composición debe ser único. Las composiciones pueden filtrarse
 * por clasificación: {@code NATURAL}, {@code SINTETICO}, {@code MIXTO}.
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida</li>
 * <li>{@code 404} – Composición no encontrada</li>
 * <li>{@code 409} – Código de composición duplicado</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/maestros/composiciones")
@RequiredArgsConstructor
public class ComposicionController {

    private final ComposicionService composicionService;

    /**
     * Crea una nueva composición textil.
     *
     * @param request datos de la composición (código, descripción, clasificación,
     *                uso típico)
     * @return composición creada con su ID, HTTP 201
     */
    @PostMapping
    public ResponseEntity<ComposicionDTO> crear(@Valid @RequestBody ComposicionDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(composicionService.crear(request));
    }

    /**
     * Actualiza los datos de una composición textil existente.
     *
     * @param id      ID de la composición a actualizar
     * @param request nuevos datos de la composición
     * @return composición actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<ComposicionDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ComposicionDTO request) {
        return ResponseEntity.ok(composicionService.actualizar(id, request));
    }

    /**
     * Obtiene una composición textil por su ID.
     *
     * @param id ID de la composición
     * @return datos de la composición, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<ComposicionDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(composicionService.obtenerPorId(id));
    }

    /**
     * Lista todas las composiciones textiles registradas.
     *
     * @return lista completa de composiciones, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<ComposicionDTO>> listarTodas() {
        return ResponseEntity.ok(composicionService.listarTodas());
    }

    /**
     * Filtra composiciones por su clasificación (búsqueda case-insensitive).
     * Valores típicos: {@code NATURAL}, {@code SINTETICO}, {@code MIXTO}.
     *
     * @param clasificacion tipo de clasificación a filtrar
     * @return lista de composiciones que coinciden, HTTP 200
     */
    @GetMapping("/clasificacion/{clasificacion}")
    public ResponseEntity<List<ComposicionDTO>> listarPorClasificacion(
            @PathVariable String clasificacion) {
        return ResponseEntity.ok(composicionService.listarPorClasificacion(clasificacion));
    }

    /**
     * Elimina una composición textil por su ID.
     *
     * @param id ID de la composición a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        composicionService.eliminar(id);
    }
}
