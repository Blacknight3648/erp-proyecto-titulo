package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.UnidadMedidaDTO;
import backend.com.shared.application.service.UnidadMedidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Unidades de Medida.
 *
 * <p>
 * Base URL: {@code /api/v3/maestros/unidades-medida}
 * </p>
 *
 * <p>
 * Las unidades de medida se asocian a los artículos para indicar
 * cómo se cuantifican (ej: Metro, Kilogramo, Unidad).
 * La abreviatura debe ser única en el sistema (máx. 5 caracteres).
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida</li>
 * <li>{@code 404} – Unidad de medida no encontrada</li>
 * <li>{@code 409} – Abreviatura duplicada</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/unidades-medida")
@RequiredArgsConstructor
public class UnidadMedidaController {

    private final UnidadMedidaService unidadMedidaService;

    /**
     * Crea una nueva unidad de medida.
     *
     * @param request nombre y abreviatura de la unidad
     * @return unidad creada con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<UnidadMedidaDTO> crear(@Valid @RequestBody UnidadMedidaDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(unidadMedidaService.crear(request));
    }

    /**
     * Actualiza los datos de una unidad de medida existente.
     *
     * @param id      ID de la unidad a actualizar
     * @param request nuevos datos de la unidad
     * @return unidad actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<UnidadMedidaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody UnidadMedidaDTO request) {
        return ResponseEntity.ok(unidadMedidaService.actualizar(id, request));
    }

    /**
     * Obtiene una unidad de medida por su ID.
     *
     * @param id ID de la unidad a buscar
     * @return datos de la unidad, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<UnidadMedidaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(unidadMedidaService.obtenerPorId(id));
    }

    /**
     * Lista todas las unidades de medida registradas.
     *
     * @return lista de unidades, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<UnidadMedidaDTO>> listarTodas() {
        return ResponseEntity.ok(unidadMedidaService.listarTodas());
    }

    /**
     * Elimina una unidad de medida por su ID.
     * <p>
     * <strong>Advertencia:</strong> si existen artículos vinculados a esta unidad,
     * la eliminación será rechazada por la base de datos.
     * </p>
     *
     * @param id ID de la unidad a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        unidadMedidaService.eliminar(id);
    }
}
