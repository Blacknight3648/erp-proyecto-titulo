package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.AtributoTecnicoDTO;
import backend.com.shared.application.service.AtributoTecnicoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Atributos Técnicos de tela.
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/atributos-tecnicos}
 * </p>
 *
 * <p>
 * Los atributos técnicos describen propiedades especiales de una tela
 * (ej: Transpirable, Ignífugo, Antibacterial). Cada atributo tiene un código
 * único y una clasificación que permite filtrarlos
 * (ej: {@code RESISTENCIA}, {@code ACABADO}, {@code HIGIENE}).
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida</li>
 * <li>{@code 404} – Atributo no encontrado</li>
 * <li>{@code 409} – Código de atributo duplicado</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/maestros/atributos-tecnicos")
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
    public ResponseEntity<AtributoTecnicoDTO> crear(
            @Valid @RequestBody AtributoTecnicoDTO request) {
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
    public ResponseEntity<AtributoTecnicoDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody AtributoTecnicoDTO request) {
        return ResponseEntity.ok(atributoService.actualizar(id, request));
    }

    /**
     * Obtiene un atributo técnico por su ID.
     *
     * @param id ID del atributo
     * @return datos del atributo, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<AtributoTecnicoDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(atributoService.obtenerPorId(id));
    }

    /**
     * Lista todos los atributos técnicos registrados en el sistema.
     *
     * @return lista completa de atributos, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<AtributoTecnicoDTO>> listarTodos() {
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
    public ResponseEntity<List<AtributoTecnicoDTO>> listarPorClasificacion(
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
