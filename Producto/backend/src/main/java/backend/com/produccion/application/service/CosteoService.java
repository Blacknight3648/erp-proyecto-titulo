package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import java.util.List;
import java.util.Optional;

public interface CosteoService {
    CosteoDTO save(CosteoDTO costeoDTO);
    List<CosteoDTO> findAll();
    Optional<CosteoDTO> findBySolicitudCostosId(Long scosId);
    List<CosteoDTO> findAllBySolicitudCostosId(Long scosId);
    Optional<CosteoResumenEVNDTO> obtenerResumenEVN(Long idCosteo);
    /** Costeos no vinculados a ningún ítem de EVN (disponibles para vincular). */
    List<CosteoDTO> obtenerDisponiblesParaEVN();
}
