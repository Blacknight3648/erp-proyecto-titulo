package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.GenerarOCLoteRequest;
import backend.com.produccion.application.dto.HistorialVersionOCDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.domain.enums.EstadoOC;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {

    OrdenCompraDTO generarConsolidada(GenerarOCConsolidadaRequest request);

    /**
     * Genera varias OC (una por grupo proveedor+items) en una sola transacción
     * atómica: si un grupo falla, no se persiste ninguna OC de la tanda.
     */
    List<OrdenCompraDTO> generarLote(GenerarOCLoteRequest request);

    OrdenCompraDTO marcarEnviada(Long idOC);

    OrdenCompraDTO marcarRecepcionada(Long idOC);

    OrdenCompraDTO cerrar(Long idOC);

    /**
     * Rechaza la OC (solo desde EMITIDA), exige motivo y actor con rol autorizado.
     */
    OrdenCompraDTO rechazar(Long idOC, String motivo, String usuario, String rol);

    /**
     * Reingresa una OC RECHAZADA: vuelve a EMITIDA con el mismo número de
     * documento e incrementa la versión. Exige actor con rol autorizado.
     * Permite corregir el proveedor y editar cantidad/precio de ítems en el
     * mismo paso (ambos opcionales), todo dentro de la misma transacción:
     * si la edición de algún ítem falla la validación, no se persiste nada
     * y la OC queda intacta en RECHAZADA.
     */
    OrdenCompraDTO reingresar(Long idOC, String usuario, String rol, Long nuevoProveedorId,
            List<OrdenCompraItemDTO> itemsCambiados);

    void eliminar(Long idOC);

    OrdenCompraDTO agregarItem(Long idOC, OrdenCompraItemDTO itemDTO);

    OrdenCompraDTO actualizarItem(Long idOC, Long idOCItem, OrdenCompraItemDTO itemDTO);

    OrdenCompraDTO eliminarItem(Long idOC, Long idOCItem);

    /**
     * Actualiza el precio unitario de un OCItem y recalcula subtotal + total de la OC.
     * Solo permitido en estado EMITIDA.
     */
    OrdenCompraDTO actualizarPrecioItem(Long idOC, Long idOCItem, BigDecimal nuevoPrecio);

    Optional<OrdenCompraDTO> obtenerPorId(Long idOC);

    List<OrdenCompraDTO> listarTodas();

    List<OrdenCompraDTO> listarPorEstado(EstadoOC estado);

    List<OrdenCompraDTO> listarPorProveedor(Long proveedorId);

    List<OrdenCompraDTO> listarPorHCItem(Long hcItemId);

    /**
     * Historial de versiones (snapshots congelados en cada rechazo) más el
     * estado activo actual, más reciente primero.
     */
    List<HistorialVersionOCDTO> obtenerHistorialVersiones(Long idOC);
}
