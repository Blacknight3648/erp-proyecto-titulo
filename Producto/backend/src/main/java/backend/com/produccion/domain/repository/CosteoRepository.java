package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.Costeo;
import java.util.Optional;

public interface CosteoRepository {
    Costeo save(Costeo costeo);

    Optional<Costeo> findBySolicitudCostosId(Long solicitudCostosId);

    java.util.List<Costeo> findAllBySolicitudCostosId(Long solicitudCostosId);

    Optional<Costeo> findById(Long id);
}
