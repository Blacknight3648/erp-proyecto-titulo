package backend.com.maestros.controller;

import backend.com.maestros.dto.request.PrecioRequestDTO;
import backend.com.maestros.dto.response.PrecioResponseDTO;
import backend.com.maestros.service.PrecioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Precios de artículos.
 *
 * <p>Base URL: {@code /api/v3/maestros/precios}</p>
 *
 * <p>Un artículo puede tener múltiples precios según el tipo
 * (COSTO, VENTA, MAYORISTA) y la moneda utilizada.
 * Cada precio referencia un {@code Articulo} y una {@code Moneda} existentes.</p>
 *
 * <p>Errores posibles:</p>
 * <ul>
 *   <li>{@code 400} – Validación fallida (valor negativo, campos obligatorios faltantes)</li>
 *   <li>{@code 404} – Precio, Artículo o Moneda no encontrados</li>
 *   <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v3/maestros/precios")
@RequiredArgsConstructor
public class PrecioController {

    private final PrecioService precioService;

    /**
     * Registra un nuevo precio para un artículo.
     *
     * @param request datos del precio (idArticulo, idMoneda, tipoPrecio, valor)
     * @return precio creado con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<PrecioResponseDTO> crear(@Valid @RequestBody PrecioRequestDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(precioService.crear(request));
    }

    /**
     * Actualiza el tipo de precio, moneda o valor de un precio existente.
     * No permite cambiar el artículo al que pertenece el precio.
     *
     * @param id      ID del precio a actualizar
     * @param request nuevos datos del precio
     * @return precio actualizado, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody PrecioRequestDTO request) {
        return ResponseEntity.ok(precioService.actualizar(id, request));
    }

    /**
     * Obtiene un precio por su ID.
     *
     * @param id ID del precio
     * @return datos del precio incluyendo artículo y moneda referenciados, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<PrecioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(precioService.obtenerPorId(id));
    }

    /**
     * Lista todos los precios registrados para un artículo específico.
     * Retorna todos los tipos de precio en todas las monedas.
     *
     * @param idArticulo ID del artículo
     * @return lista de precios del artículo (puede estar vacía), HTTP 200
     */
    @GetMapping("/articulo/{idArticulo}")
    public ResponseEntity<List<PrecioResponseDTO>> listarPorArticulo(@PathVariable Integer idArticulo) {
        return ResponseEntity.ok(precioService.listarPorArticulo(idArticulo));
    }

    /**
     * Elimina un precio específico por su ID.
     *
     * @param id ID del precio a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        precioService.eliminar(id);
    }
}
