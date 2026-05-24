package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.domain.model.EstadoOC;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface OrdenCompraService {

    OrdenCompraDTO generarConsolidada(GenerarOCConsolidadaRequest request);

    OrdenCompraDTO marcarEnviada(Long idOC);

    OrdenCompraDTO marcarRecepcionada(Long idOC);

    OrdenCompraDTO cerrar(Long idOC);

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
