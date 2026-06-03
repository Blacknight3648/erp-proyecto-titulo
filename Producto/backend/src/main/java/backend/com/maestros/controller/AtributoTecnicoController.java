package backend.com.maestros.controller;

import backend.com.maestros.dto.request.AtributoTecnicoRequestDTO;
import backend.com.maestros.dto.response.AtributoTecnicoResponseDTO;
import backend.com.maestros.service.AtributoTecnicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Atributos Técnicos de tela.
 *
 * <p>Base URL: {@code /api/v3/maestros/atributos-tecnicos}</p>
 *
 * <p>Los atributos técnicos describen propiedades especiales de una tela
 * (ej: Transpirable, Ignífugo, Antibacterial). Cada atributo tiene un código
 * único y una clasificación que permite filtrarlos
 * (ej: {@code RESISTENCIA}, {@code ACABADO}, {@code HIGIENE}).</p>
 *
 * <p>Errores posibles:</p>
 * <ul>
 *   <li>{@code 400} – Validación fallida</li>
 *   <li>{@code 404} – Atributo no encontrado</li>
 *   <li>{@code 409} – Código de atributo duplicado</li>
 *   <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/atributos-tecnicos")
@RequiredArgsConstructor
public class AtributoTecnicoController {

    private final AtributoTecnicoService atributoService;

    /**
     * Crea un nuevo atributo técnico en el catálogo.
     *
     * @param request código, clasificación, descripción técnica e impacto ERP
     * @return atributo creado con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<AtributoTecnicoResponseDTO> crear(
            @Valid @RequestBody AtributoTecnicoRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(atributoService.crear(request));
    }

    /**
     * Actualiza los datos de un atributo técnico existente.
     *
     * @param id      ID del atributo a modificar
     * @param request nuevos datos del atributo
     * @return atributo actualizado, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<AtributoTecnicoResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody AtributoTecnicoRequestDTO request) {
        return ResponseEntity.ok(atributoService.actualizar(id, request));
    }

    /**
     * Obtiene un atributo técnico por su ID.
     *
     * @param id ID del atributo
     * @return datos del atributo, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtributoTecnicoResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atributoService.obtenerPorId(id));
    }

    /**
     * Lista todos los atributos técnicos registrados en el sistema.
     *
     * @return lista completa de atributos, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<AtributoTecnicoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(atributoService.listarTodos());
    }

    /**
     * Filtra atributos técnicos por su clasificación (búsqueda case-insensitive).
     * Valores típicos: {@code RESISTENCIA}, {@code ACABADO}, {@code HIGIENE},
     * {@code TERMICA}, {@code ECOLOGICA}.
     *
     * @param clasificacion categoría de clasificación del atributo
     * @return lista de atributos que coinciden, HTTP 200
     */
    @GetMapping("/clasificacion/{clasificacion}")
    public ResponseEntity<List<AtributoTecnicoResponseDTO>> listarPorClasificacion(
            @PathVariable String clasificacion) {
        return ResponseEntity.ok(atributoService.listarPorClasificacion(clasificacion));
    }

    /**
     * Elimina un atributo técnico por su ID.
     *
     * @param id ID del atributo a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        atributoService.eliminar(id);
    }
}
