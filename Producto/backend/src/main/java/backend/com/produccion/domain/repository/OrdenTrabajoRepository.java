package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.OrdenTrabajo;
import java.util.List;
import java.util.Optional;

public interface OrdenTrabajoRepository {
    OrdenTrabajo save(OrdenTrabajo ot);

    Optional<OrdenTrabajo> findById(Long id);

    List<OrdenTrabajo> findByNotaVentaId(Long notaVentaId);

    List<OrdenTrabajo> findByOrdenProduccionId(Long ordenProduccionId);
}
