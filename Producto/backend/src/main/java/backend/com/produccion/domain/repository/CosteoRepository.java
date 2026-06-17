package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.Costeo;
import java.util.Optional;

public interface CosteoRepository {
    Costeo save(Costeo costeo);

    java.util.List<Costeo> findAll();

    Optional<Costeo> findBySolicitudCostosId(Long solicitudCostosId);

    java.util.List<Costeo> findAllBySolicitudCostosId(Long solicitudCostosId);

    Optional<Costeo> findById(Long id);

    /** Busca el Costeo auto-creado para una NV específica. Garantiza idempotencia en la creación. */
    Optional<Costeo> findByNotaVentaId(Long notaVentaId);
}
