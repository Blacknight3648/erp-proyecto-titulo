package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import java.util.List;
import java.util.Optional;

public interface CosteoService {
    CosteoDTO save(CosteoDTO costeoDTO);
    List<CosteoDTO> findAll();
    Optional<CosteoDTO> findById(Long id);
    Optional<CosteoDTO> findBySolicitudCostosId(Long scosId);
    List<CosteoDTO> findAllBySolicitudCostosId(Long scosId);
    void deleteBySolicitudCostosId(Long scosId);
    Optional<CosteoResumenEVNDTO> obtenerResumenEVN(Long idCosteo);
    /** Costeos APROBADOS y no vinculados a ningún ítem de EVN (disponibles para vincular). */
    List<CosteoDTO> obtenerDisponiblesParaEVN();

    // --- Transiciones del ciclo de vida ---
    /** BORRADOR → COSTEADO (producción confirma los costos). */
    CosteoDTO costear(Long id);
    /** COSTEADO → APROBADO (habilita el vínculo con EVN/NV). Requiere firma y rol autorizado. */
    CosteoDTO aprobar(Long id, String usuario, String rol);
    /** → RECHAZADO; exige motivo, firma y rol autorizado; versiona el costeo rechazado. */
    CosteoDTO rechazar(Long id, String motivo, String usuario, String rol);
    /** RECHAZADO/COSTEADO → BORRADOR (reproceso). */
    CosteoDTO reabrir(Long id);
}
