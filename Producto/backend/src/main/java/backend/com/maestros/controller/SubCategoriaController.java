package backend.com.maestros.controller;

import backend.com.maestros.dto.request.SubCategoriaRequestDTO;
import backend.com.maestros.dto.response.SubCategoriaResponseDTO;
import backend.com.maestros.service.SubCategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de SubCategorías de artículos.
 *
 * <p>Base URL: {@code /api/v3/maestros/subcategorias}</p>
 *
 * <p>Una SubCategoría pertenece siempre a una {@code Categoria} padre
 * (ej: Categoría "Telas" → SubCategoría "Tela Plana", "Tela Punto").
 * El request debe incluir el {@code idCategoria} del padre.</p>
 *
 * <p>Errores posibles:</p>
 * <ul>
 *   <li>{@code 400} – Validación fallida o parámetros inválidos</li>
 *   <li>{@code 404} – SubCategoría o Categoría padre no encontrada</li>
 *   <li>{@code 409} – Código de subcategoría duplicado</li>
 *   <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/subcategorias")
@RequiredArgsConstructor
public class SubCategoriaController {

    private final SubCategoriaService subCategoriaService;

    /**
     * Crea una nueva subcategoría asociada a una categoría padre.
     *
     * @param request datos de la subcategoría (código, nombre e idCategoria padre)
     * @return subcategoría creada con datos de su categoría padre, HTTP 201
     */
    @PostMapping
    public ResponseEntity<SubCategoriaResponseDTO> crear(@Valid @RequestBody SubCategoriaRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subCategoriaService.crear(request));
    }

    /**
     * Actualiza los datos de una subcategoría existente.
     * Permite reasignarla a una categoría padre diferente.
     *
     * @param id      ID de la subcategoría a actualizar
     * @param request nuevos datos de la subcategoría
     * @return subcategoría actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<SubCategoriaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody SubCategoriaRequestDTO request) {
        return ResponseEntity.ok(subCategoriaService.actualizar(id, request));
    }

    /**
     * Obtiene una subcategoría por su ID, incluyendo datos de la categoría padre.
     *
     * @param id ID de la subcategoría
     * @return datos de la subcategoría, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<SubCategoriaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(subCategoriaService.obtenerPorId(id));
    }

    /**
     * Lista todas las subcategorías del sistema.
     *
     * @return lista completa de subcategorías, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<SubCategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(subCategoriaService.listarTodas());
    }

    /**
     * Lista todas las subcategorías que pertenecen a una categoría padre específica.
     * Útil para poblar selectores dependientes en el frontend.
     *
     * @param idCategoria ID de la categoría padre
     * @return lista filtrada de subcategorías, HTTP 200
     */
    @GetMapping("/por-categoria/{idCategoria}")
    public ResponseEntity<List<SubCategoriaResponseDTO>> listarPorCategoria(
            @PathVariable Integer idCategoria) {
        return ResponseEntity.ok(subCategoriaService.listarPorCategoria(idCategoria));
    }

    /**
     * Elimina una subcategoría por su ID.
     *
     * @param id ID de la subcategoría a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        subCategoriaService.eliminar(id);
    }
}
