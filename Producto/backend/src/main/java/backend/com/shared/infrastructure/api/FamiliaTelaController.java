package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.FamiliaTelaDTO;
import backend.com.shared.application.service.FamiliaTelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Familias de Tela.
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/familias-tela}
 * </p>
 *
 * <p>
 * Una familia de tela agrupa tipos de tela por su origen o proceso de
 * fabricación
 * (ej: Tejido Plano, Tejido Punto, No Tejido). El código de familia debe ser
 * único.
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida</li>
 * <li>{@code 404} – Familia no encontrada</li>
 * <li>{@code 409} – Código de familia duplicado</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/maestros/familias-tela")
@RequiredArgsConstructor
public class FamiliaTelaController {

    private final FamiliaTelaService familiaTelaService;

    /**
     * Crea una nueva familia de tela.
     *
     * @param request código y nombre de la familia
     * @return familia creada con su ID, HTTP 201
     */
    @PostMapping
    public ResponseEntity<FamiliaTelaDTO> crear(@Valid @RequestBody FamiliaTelaDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(familiaTelaService.crear(request));
    }

    /**
     * Actualiza los datos de una familia de tela existente.
     *
     * @param id      ID de la familia a actualizar
     * @param request nuevos datos
     * @return familia actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<FamiliaTelaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody FamiliaTelaDTO request) {
        return ResponseEntity.ok(familiaTelaService.actualizar(id, request));
    }

    /**
     * Obtiene una familia de tela por su ID.
     *
     * @param id ID de la familia
     * @return datos de la familia, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<FamiliaTelaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(familiaTelaService.obtenerPorId(id));
    }

    /**
     * Lista todas las familias de tela registradas.
     *
     * @return lista completa de familias, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<FamiliaTelaDTO>> listarTodas() {
        return ResponseEntity.ok(familiaTelaService.listarTodas());
    }

    /**
     * Elimina una familia de tela por su ID.
     *
     * @param id ID de la familia a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        familiaTelaService.eliminar(id);
    }
}
