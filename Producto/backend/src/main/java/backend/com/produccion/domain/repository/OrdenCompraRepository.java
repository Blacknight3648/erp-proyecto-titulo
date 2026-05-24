package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.EstadoOC;
import backend.com.produccion.domain.model.OrdenCompra;

import java.util.List;
import java.util.Optional;

public interface OrdenCompraRepository {

    OrdenCompra save(OrdenCompra ordenCompra);

    Optional<OrdenCompra> findById(Long idOC);

    List<OrdenCompra> findAll();

    List<OrdenCompra> findAllByEstado(EstadoOC estado);

    List<OrdenCompra> findAllByProveedorId(Long proveedorId);

    /**
     * Recupera todas las OCs que consumen al menos un OCItem ligado al HCItem dado.
     * Útil para conocer en qué OCs se está comprando un HCItem específico.
     */
    List<OrdenCompra> findAllByHcItemId(Long hcItemId);
}
