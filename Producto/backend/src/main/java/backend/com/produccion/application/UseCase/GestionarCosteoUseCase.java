package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.CosteoDTO;
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
        return costeoService.save(costeoDTO);
    }

    public Optional<CosteoDTO> obtenerPorSCOS(Long scosId) {
        return costeoService.findBySolicitudCostosId(scosId);
    }

    public java.util.List<CosteoDTO> obtenerTodosPorSCOS(Long scosId) {
        return costeoService.findAllBySolicitudCostosId(scosId);
    }
}
