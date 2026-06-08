package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.CosteoDTO;
import java.util.Optional;

public interface CosteoService {
    CosteoDTO save(CosteoDTO costeoDTO);
    java.util.List<CosteoDTO> findAll();
    Optional<CosteoDTO> findBySolicitudCostosId(Long scosId);
    java.util.List<CosteoDTO> findAllBySolicitudCostosId(Long scosId);
}
