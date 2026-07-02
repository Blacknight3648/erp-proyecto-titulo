package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.OrdenProduccion;
import java.util.List;
import java.util.Optional;

public interface OrdenProduccionRepository {
    OrdenProduccion save(OrdenProduccion op);

    Optional<OrdenProduccion> findById(Long id);

    List<OrdenProduccion> findAll();

    List<OrdenProduccion> findByNotaVentaId(Long notaVentaId);

    /** IDs de costeos que ya son usados por alguna Orden de Producción existente. */
    List<Long> findCosteoIdsEnUso();
}
