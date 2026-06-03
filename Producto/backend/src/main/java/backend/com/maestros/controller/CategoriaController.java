package backend.com.maestros.controller;

import backend.com.maestros.dto.request.CategoriaRequestDTO;
import backend.com.maestros.dto.response.CategoriaResponseDTO;
import backend.com.maestros.service.CategoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Categorías de artículos.
 *
 * <p>Base URL: {@code /api/v3/maestros/categorias}</p>
 *
 * <p>Las categorías agrupan a los artículos en conjuntos lógicos
 * (ej: Telas, Accesorios, Prendas Listas). Cada categoría tiene un código
 * único y un nombre único en el sistema.</p>
 *
 * <p>Errores posibles manejados por {@code GlobalExceptionHandler}:</p>
 * <ul>
 *   <li>{@code 400} – Cuerpo del request inválido (validación Bean Validation)</li>
 *   <li>{@code 404} – Categoría no encontrada por el ID solicitado</li>
 *   <li>{@code 409} – Ya existe una categoría con el mismo código o nombre</li>
 *   <li>{@code 500} – Error interno inesperado del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    /**
     * Crea una nueva categoría de artículo.
     *
     * @param request datos de la categoría a crear (código y nombre obligatorios)
     * @return la categoría creada con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crear(@Valid @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoriaService.crear(request));
    }

    /**
     * Actualiza los datos de una categoría existente.
     *
     * @param id      ID de la categoría a actualizar
     * @param request nuevos datos de la categoría
     * @return la categoría actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequestDTO request) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id ID de la categoría a buscar
     * @return datos de la categoría, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    /**
     * Lista todas las categorías registradas en el sistema.
     *
     * @return lista de categorías (puede estar vacía), HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.listarTodas());
    }

    /**
     * Elimina una categoría por su ID.
     * <p><strong>Advertencia:</strong> si existen artículos o subcategorías vinculadas,
     * la base de datos rechazará la operación.</p>
     *
     * @param id ID de la categoría a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
    }
}
