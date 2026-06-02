package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.UseCase.GestionarCosteoUseCase;
import backend.com.produccion.application.dto.CosteoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produccion/costeos")
@RequiredArgsConstructor
public class CosteoController {

    private final GestionarCosteoUseCase gestionarCosteoUseCase;

    @GetMapping("/scos/{scosId}")
    public ResponseEntity<CosteoDTO> getBySCOS(@PathVariable Long scosId) {
        return gestionarCosteoUseCase.obtenerPorSCOS(scosId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/scos/{scosId}/all")
    public ResponseEntity<java.util.List<CosteoDTO>> getAllBySCOS(@PathVariable Long scosId) {
        return ResponseEntity.ok(gestionarCosteoUseCase.obtenerTodosPorSCOS(scosId));
    }

    @PostMapping
    public CosteoDTO crear(@RequestBody CosteoDTO costeo) {
        return gestionarCosteoUseCase.registrarCosteo(costeo);
    }
}
