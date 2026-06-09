package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.CosteoDTO;
import java.util.List;
import java.util.Optional;

public interface CosteoService {
    CosteoDTO save(CosteoDTO costeoDTO);
    List<CosteoDTO> findAll();
    Optional<CosteoDTO> findBySolicitudCostosId(Long scosId);
    List<CosteoDTO> findAllBySolicitudCostosId(Long scosId);
}
