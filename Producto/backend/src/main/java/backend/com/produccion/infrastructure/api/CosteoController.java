package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.UseCase.GestionarCosteoUseCase;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/produccion/costeos")
@RequiredArgsConstructor
public class CosteoController {

    private final GestionarCosteoUseCase gestionarCosteoUseCase;

    @GetMapping("/scos")
    public ResponseEntity<java.util.List<CosteoDTO>> getBySCOS() {
        return ResponseEntity.ok(gestionarCosteoUseCase.obtenerTodos());
    }

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

    /** Costeos disponibles (no vinculados a ninguna EVN) para vincular a un ítem OP. */
    @GetMapping("/disponibles-evn")
    public ResponseEntity<java.util.List<CosteoDTO>> getDisponiblesParaEVN() {
        return ResponseEntity.ok(gestionarCosteoUseCase.obtenerCosteosDisponiblesParaEVN());
    }

    @PostMapping
    public CosteoDTO crear(@RequestBody CosteoDTO costeo) {
        return gestionarCosteoUseCase.registrarCosteo(costeo);
    }

    @PutMapping("/{idCosteo}")
    public ResponseEntity<CosteoDTO> actualizar(@PathVariable Long idCosteo, @RequestBody CosteoDTO costeo) {
        return ResponseEntity.ok(gestionarCosteoUseCase.actualizarCosteo(idCosteo, costeo));
    }

    // --- Transiciones del ciclo de vida (BORRADOR → COSTEADO → APROBADO / RECHAZADO) ---

    /** Producción confirma los costos: BORRADOR → COSTEADO. */
    @PatchMapping("/{idCosteo}/costear")
    public ResponseEntity<CosteoDTO> costear(@PathVariable Long idCosteo) {
        return ResponseEntity.ok(gestionarCosteoUseCase.costear(idCosteo));
    }

    /** Aprobación que habilita el vínculo con EVN/NV: COSTEADO → APROBADO. */
    @PatchMapping("/{idCosteo}/aprobar")
    public ResponseEntity<CosteoDTO> aprobar(@PathVariable Long idCosteo) {
        return ResponseEntity.ok(gestionarCosteoUseCase.aprobar(idCosteo));
    }

    /** Rechazo por diferencias: exige motivo y versiona el costeo rechazado. */
    @PatchMapping("/{idCosteo}/rechazar")
    public ResponseEntity<CosteoDTO> rechazar(@PathVariable Long idCosteo, @RequestBody RechazoRequest request) {
        return ResponseEntity.ok(gestionarCosteoUseCase.rechazar(idCosteo, request != null ? request.motivo() : null));
    }

    /** Reabre para reproceso: RECHAZADO/COSTEADO → BORRADOR. */
    @PatchMapping("/{idCosteo}/reabrir")
    public ResponseEntity<CosteoDTO> reabrir(@PathVariable Long idCosteo) {
        return ResponseEntity.ok(gestionarCosteoUseCase.reabrir(idCosteo));
    }

    /** Body del rechazo: motivo obligatorio. */
    public record RechazoRequest(String motivo) {
    }

    /** Resumen para auto-rellenar un ítem tipo OP de una EVN al vincular un costeo. */
    @GetMapping("/{idCosteo}/resumen-evn")
    public ResponseEntity<CosteoResumenEVNDTO> getResumenParaEVN(@PathVariable Long idCosteo) {
        return gestionarCosteoUseCase.obtenerResumenEVN(idCosteo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
