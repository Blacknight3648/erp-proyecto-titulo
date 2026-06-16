package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import backend.com.produccion.application.service.CosteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestionarCosteoUseCase {

    private final CosteoService costeoService;

    public CosteoDTO registrarCosteo(CosteoDTO costeoDTO) {
        if (costeoDTO == null)
            throw new IllegalArgumentException("El costeo no puede ser nulo");
        // El número correlativo propio (C-0000001) lo asigna CosteoService.save
        // de forma atómica y transaccional.
        return costeoService.save(costeoDTO);
    }

    public CosteoDTO actualizarCosteo(Long idCosteo, CosteoDTO costeoDTO) {
        if (idCosteo == null)
            throw new IllegalArgumentException("El id del costeo es obligatorio para actualizar");
        if (costeoDTO == null)
            throw new IllegalArgumentException("El costeo no puede ser nulo");
        costeoDTO.setIdCosteo(idCosteo);
        // En actualización se conserva el número existente (CosteoService.save).
        return costeoService.save(costeoDTO);
    }

    public Optional<CosteoDTO> obtenerPorSCOS(Long scosId) {
        return costeoService.findBySolicitudCostosId(scosId);
    }

    public java.util.List<CosteoDTO> obtenerTodos() {
        return costeoService.findAll();
    }

    public java.util.List<CosteoDTO> obtenerTodosPorSCOS(Long scosId) {
        return costeoService.findAllBySolicitudCostosId(scosId);
    }

    public java.util.List<CosteoDTO> obtenerCosteosDisponiblesParaEVN() {
        return costeoService.obtenerDisponiblesParaEVN();
    }

    public Optional<CosteoResumenEVNDTO> obtenerResumenEVN(Long idCosteo) {
        if (idCosteo == null)
            throw new IllegalArgumentException("El id del costeo es obligatorio");
        return costeoService.obtenerResumenEVN(idCosteo);
    }
}
