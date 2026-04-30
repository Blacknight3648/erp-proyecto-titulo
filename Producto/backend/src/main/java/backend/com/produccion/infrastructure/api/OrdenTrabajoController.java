package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.service.GestionarOrdenTrabajoUseCase;
import backend.com.produccion.domain.model.OrdenTrabajo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produccion/ordenes-trabajo")
@RequiredArgsConstructor
public class OrdenTrabajoController {

    private final GestionarOrdenTrabajoUseCase gestionarOTUseCase;

    @GetMapping("/nota-venta/{nvId}")
    public List<OrdenTrabajo> getByNotaVenta(@PathVariable Long nvId) {
        return gestionarOTUseCase.listarPorNotaVenta(nvId);
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Void> iniciar(@PathVariable Long id) {
        gestionarOTUseCase.iniciarOT(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Void> finalizar(@PathVariable Long id) {
        gestionarOTUseCase.finalizarOT(id);
        return ResponseEntity.ok().build();
    }
}
