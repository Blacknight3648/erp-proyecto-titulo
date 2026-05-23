package backend.com.produccion.domain.repository;

import backend.com.produccion.domain.model.CosteoVersion;

import java.util.List;
import java.util.Optional;

public interface CosteoVersionRepository {

    CosteoVersion save(CosteoVersion costeoVersion);

    Optional<CosteoVersion> findById(Long idCosteoVersion);

    Optional<CosteoVersion> findUltimaPorCosteoId(Long costeoId);

    List<CosteoVersion> findAllByCosteoId(Long costeoId);

    Integer siguienteNumeroVersion(Long costeoId);
}
