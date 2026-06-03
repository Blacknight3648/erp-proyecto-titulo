package backend.com.maestros.controller;

import backend.com.maestros.dto.request.ColorTelaRequestDTO;
import backend.com.maestros.dto.response.ColorTelaResponseDTO;
import backend.com.maestros.service.ColorTelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Colores de Tela.
 *
 * <p>Base URL: {@code /api/v3/maestros/colores-tela}</p>
 *
 * <p>Los colores de tela se asocian a artículos tipo TELA mediante una relación N:M.
 * Cada color tiene un código único y puede marcarse como referencia Pantone.</p>
 *
 * <p>Errores posibles:</p>
 * <ul>
 *   <li>{@code 400} – Validación fallida</li>
 *   <li>{@code 404} – Color no encontrado</li>
 *   <li>{@code 409} – Código de color duplicado</li>
 *   <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/colores-tela")
@RequiredArgsConstructor
public class ColorTelaController {

    private final ColorTelaService colorTelaService;

    /**
     * Crea un nuevo color de tela en el catálogo.
     *
     * @param request código, descripción e indicador Pantone del color
     * @return color creado con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<ColorTelaResponseDTO> crear(@Valid @RequestBody ColorTelaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(colorTelaService.crear(request));
    }

    /**
     * Actualiza los datos de un color de tela existente.
     *
     * @param id      ID del color a modificar
     * @param request nuevos datos del color
     * @return color actualizado, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColorTelaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ColorTelaRequestDTO request) {
        return ResponseEntity.ok(colorTelaService.actualizar(id, request));
    }

    /**
     * Obtiene un color de tela por su ID.
     *
     * @param id ID del color
     * @return datos del color, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColorTelaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(colorTelaService.obtenerPorId(id));
    }

    /**
     * Lista todos los colores de tela registrados.
     *
     * @return lista completa de colores, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<ColorTelaResponseDTO>> listarTodos() {
        return ResponseEntity.ok(colorTelaService.listarTodos());
    }

    /**
     * Busca colores cuya descripción contenga el texto indicado (case-insensitive).
     *
     * @param descripcion fragmento de texto a buscar en la descripción del color
     * @return lista de colores que coinciden, HTTP 200
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<ColorTelaResponseDTO>> buscarPorDescripcion(
            @RequestParam String descripcion) {
        return ResponseEntity.ok(colorTelaService.buscarPorDescripcion(descripcion));
    }

    /**
     * Lista únicamente los colores que corresponden a referencias de color Pantone.
     *
     * @return lista de colores Pantone, HTTP 200
     */
    @GetMapping("/pantone")
    public ResponseEntity<List<ColorTelaResponseDTO>> listarPantone() {
        return ResponseEntity.ok(colorTelaService.listarPantone());
    }

    /**
     * Elimina un color de tela por su ID.
     *
     * @param id ID del color a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        colorTelaService.eliminar(id);
    }
}
