package backend.com.maestros.controller;

import backend.com.maestros.domain.entity.Articulo;
import backend.com.maestros.domain.enums.TipoArticulo;
import backend.com.maestros.dto.articuloDto.ArticuloCreateRequestDTO;
import backend.com.maestros.dto.articuloDto.ArticuloUpdateRequestDTO;
import backend.com.maestros.service.ArticuloService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión maestra de Artículos.
 *
 * <p>Base URL: {@code /api/v3/maestros/articulos}</p>
 *
 * <p>El artículo es la entidad central del módulo de maestros, equivalente a
 * {@code product.template} en Odoo. Según su {@code tipoArticulo}, el artículo
 * puede tener información complementaria en tablas satélite:</p>
 * <ul>
 *   <li>{@code TELA}          → tabla {@code articulo_tela}</li>
 *   <li>{@code PRENDA_LISTA}  → tabla {@code articulo_prenda}</li>
 *   <li>{@code ACCESORIO}     → tabla {@code articulo_accesorio}</li>
 * </ul>
 *
 * <p>Errores posibles manejados por {@code GlobalExceptionHandler}:</p>
 * <ul>
 *   <li>{@code 400} – Validación fallida o parámetros inválidos</li>
 *   <li>{@code 404} – Artículo no encontrado por ID o código</li>
 *   <li>{@code 409} – Código de artículo duplicado</li>
 *   <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/articulos")
@RequiredArgsConstructor
public class ArticuloController {

    private final ArticuloService articuloService;

    /**
     * Crea un nuevo artículo en el sistema.
     *
     * <p>El campo {@code tipoArticulo} determina si se debe enviar también
     * la sección de detalle correspondiente (Tela, Prenda o Accesorio)
     * dentro del mismo request.</p>
     *
     * @param request datos completos del artículo a crear
     * @return artículo creado con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<Articulo> crear(@Valid @RequestBody ArticuloCreateRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(articuloService.crearArticulo(request));
    }

    /**
     * Actualiza los datos de un artículo existente.
     *
     * @param id      ID del artículo a modificar
     * @param request nuevos datos del artículo
     * @return artículo actualizado, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<Articulo> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ArticuloUpdateRequestDTO request) {
        return ResponseEntity.ok(articuloService.actualizarArticulo(id, request));
    }

    /**
     * Obtiene un artículo por su ID interno.
     *
     * @param id ID del artículo
     * @return datos completos del artículo incluyendo sus satélites, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<Articulo> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(articuloService.obtenerPorId(id));
    }

    /**
     * Obtiene un artículo por su código único de artículo.
     *
     * @param codigo código alfanumérico único del artículo (ej: "TEL-001")
     * @return datos completos del artículo, HTTP 200
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<Articulo> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(articuloService.obtenerPorCodigo(codigo));
    }

    /**
     * Lista todos los artículos del sistema (activos e inactivos).
     *
     * @return lista completa de artículos, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<Articulo>> listarTodos() {
        return ResponseEntity.ok(articuloService.listarTodos());
    }

    /**
     * Lista únicamente los artículos marcados como activos.
     *
     * @return lista de artículos activos, HTTP 200
     */
    @GetMapping("/activos")
    public ResponseEntity<List<Articulo>> listarActivos() {
        return ResponseEntity.ok(articuloService.listarActivos());
    }

    /**
     * Filtra artículos por tipo: {@code TELA}, {@code PRENDA_LISTA} o {@code ACCESORIO}.
     * El valor del parámetro es case-sensitive (debe estar en mayúsculas).
     *
     * @param tipo tipo de artículo a filtrar
     * @return lista de artículos del tipo indicado, HTTP 200
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Articulo>> listarPorTipo(@PathVariable TipoArticulo tipo) {
        return ResponseEntity.ok(articuloService.listarPorTipo(tipo));
    }

    /**
     * Busca artículos cuyo nombre contenga el texto indicado (búsqueda parcial, case-insensitive).
     *
     * @param nombre fragmento del nombre a buscar
     * @return lista de artículos que coinciden, HTTP 200
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<Articulo>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(articuloService.buscarPorNombre(nombre));
    }

    /**
     * Desactiva (eliminación lógica) un artículo por su ID.
     * No elimina el registro físicamente de la base de datos.
     *
     * @param id ID del artículo a desactivar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        articuloService.eliminarArticulo(id);
    }
}
