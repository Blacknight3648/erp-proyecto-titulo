package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.MonedaDTO;
import backend.com.shared.application.service.MonedaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión del catálogo de Monedas.
 *
 * <p>
 * Base URL: {@code /api/v1/maestros/monedas}
 * </p>
 *
 * <p>
 * Las monedas se asocian a los precios de los artículos.
 * El código ISO (ej: CLP, USD, EUR) debe ser único en el sistema.
 * </p>
 *
 * <p>
 * Errores posibles:
 * </p>
 * <ul>
 * <li>{@code 400} – Validación fallida</li>
 * <li>{@code 404} – Moneda no encontrada</li>
 * <li>{@code 409} – Código ISO duplicado</li>
 * <li>{@code 500} – Error interno del servidor</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/maestros/monedas")
@RequiredArgsConstructor
public class MonedaController {

    private final MonedaService monedaService;

    /**
     * Crea una nueva moneda en el catálogo.
     *
     * @param request código ISO, nombre y símbolo de la moneda
     * @return moneda creada con su ID asignado, HTTP 201
     */
    @PostMapping
    public ResponseEntity<MonedaDTO> crear(@Valid @RequestBody MonedaDTO request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(monedaService.crear(request));
    }

    /**
     * Actualiza los datos de una moneda existente.
     *
     * @param id      ID de la moneda a actualizar
     * @param request nuevos datos de la moneda
     * @return moneda actualizada, HTTP 200
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonedaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody MonedaDTO request) {
        return ResponseEntity.ok(monedaService.actualizar(id, request));
    }

    /**
     * Obtiene una moneda por su ID.
     *
     * @param id ID de la moneda
     * @return datos de la moneda, HTTP 200
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonedaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(monedaService.obtenerPorId(id));
    }

    /**
     * Lista todas las monedas registradas en el sistema.
     *
     * @return lista de monedas, HTTP 200
     */
    @GetMapping
    public ResponseEntity<List<MonedaDTO>> listarTodas() {
        return ResponseEntity.ok(monedaService.listarTodas());
    }

    /**
     * Elimina una moneda por su ID.
     * <p>
     * <strong>Advertencia:</strong> si existen precios asociados a esta moneda,
     * la eliminación será rechazada por la base de datos.
     * </p>
     *
     * @param id ID de la moneda a eliminar
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        monedaService.eliminar(id);
    }
}
