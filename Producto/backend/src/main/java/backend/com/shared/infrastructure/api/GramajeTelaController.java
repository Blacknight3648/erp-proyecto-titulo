package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.GramajeTelaDTO;
import backend.com.shared.application.service.GramajeTelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Gramajes de Tela.
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/gramajes-tela}
 * </p>
 *
 * <p>
 * El gramaje indica el peso de la tela en gramos por metro cuadrado (gr/m²),
 * que es un factor clave para determinar su uso en vestuario.
 * Cada gramaje pertenece a una categoría de vestuario (ej: Camisería, Abrigo).
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida (valor negativo, código faltante)</li>
 * <li>{@code 404} – Gramaje no encontrado</li>
 * <li>{@code 409} – Código de gramaje duplicado</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/maestros/gramajes-tela")
@RequiredArgsConstructor
public class GramajeTelaController {

    private final GramajeTelaService gramajeTelaService;

    /**
     * Crea un nuevo gramaje de tela.
     *
     * @param request código, valor en gr/m² y categoría de vestuario
     * @return gramaje creado con su ID, HTTP 201
     */
    @PostMapping
    public ResponseEntity<GramajeTelaDTO> crear(@Valid @RequestBody GramajeTelaDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gramajeTelaService.crear(request));
    }

    /**
     * Actualiza los datos de un gramaje de tela existente.
     *
     * @param id      ID del gramaje a actualizar
     * @param request nuevos datos del gramaje
     * @return gramaje actualizado, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<GramajeTelaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody GramajeTelaDTO request) {
        return ResponseEntity.ok(gramajeTelaService.actualizar(id, request));
    }

    /**
     * Obtiene un gramaje de tela por su ID.
     *
     * @param id ID del gramaje
     * @return datos del gramaje, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<GramajeTelaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(gramajeTelaService.obtenerPorId(id));
    }

    /**
     * Lista todos los gramajes de tela registrados.
     *
     * @return lista completa de gramajes, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<GramajeTelaDTO>> listarTodos() {
        return ResponseEntity.ok(gramajeTelaService.listarTodos());
    }

    /**
     * Filtra gramajes por categoría de vestuario (búsqueda case-insensitive).
     * Valores típicos: {@code Camisería}, {@code Abrigo}, {@code Deportivo}.
     *
     * @param categoria nombre de la categoría de vestuario
     * @return lista de gramajes filtrados, HTTP 200
     */
    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<GramajeTelaDTO>> listarPorCategoria(
            @PathVariable String categoria) {
        return ResponseEntity.ok(gramajeTelaService.listarPorCategoriaVestuario(categoria));
    }

    /**
     * Elimina un gramaje de tela por su ID.
     *
     * @param id ID del gramaje a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        gramajeTelaService.eliminar(id);
    }
}
