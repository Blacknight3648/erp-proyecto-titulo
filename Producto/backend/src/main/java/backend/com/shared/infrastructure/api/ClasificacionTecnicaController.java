package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.ClasificacionTecnicaDTO;
import backend.com.shared.application.service.ClasificacionTecnicaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Clasificaciones Técnicas de
 * tela.
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/clasificaciones-tecnicas}
 * </p>
 *
 * <p>
 * Una clasificación técnica describe la categoría funcional de una tela
 * (ej: Tela Funcional, Tela Técnica, Tela Deportiva).
 * El nombre de clasificación debe ser único en el sistema.
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida</li>
 * <li>{@code 404} – Clasificación no encontrada</li>
 * <li>{@code 409} – Nombre de clasificación duplicado</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/maestros/clasificaciones-tecnicas")
@RequiredArgsConstructor
public class ClasificacionTecnicaController {

    private final ClasificacionTecnicaService clasificacionService;

    /**
     * Crea una nueva clasificación técnica.
     *
     * @param request nombre de la clasificación (único en el sistema)
     * @return clasificación creada con su ID, HTTP 201
     */
    @PostMapping
    public ResponseEntity<ClasificacionTecnicaDTO> crear(
            @Valid @RequestBody ClasificacionTecnicaDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(clasificacionService.crear(request));
    }

    /**
     * Actualiza el nombre de una clasificación técnica existente.
     *
     * @param id      ID de la clasificación a actualizar
     * @param request nuevo nombre de la clasificación
     * @return clasificación actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<ClasificacionTecnicaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ClasificacionTecnicaDTO request) {
        return ResponseEntity.ok(clasificacionService.actualizar(id, request));
    }

    /**
     * Obtiene una clasificación técnica por su ID.
     *
     * @param id ID de la clasificación
     * @return datos de la clasificación, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<ClasificacionTecnicaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(clasificacionService.obtenerPorId(id));
    }

    /**
     * Lista todas las clasificaciones técnicas registradas.
     *
     * @return lista completa de clasificaciones, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<ClasificacionTecnicaDTO>> listarTodas() {
        return ResponseEntity.ok(clasificacionService.listarTodas());
    }

    /**
     * Elimina una clasificación técnica por su ID.
     *
     * @param id ID de la clasificación a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        clasificacionService.eliminar(id);
    }
}
