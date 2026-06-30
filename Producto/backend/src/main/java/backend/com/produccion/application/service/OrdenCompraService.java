package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.GenerarOCLoteRequest;
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
}
