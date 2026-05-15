package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.RegistrarAvanceCommand;
import backend.com.produccion.application.dto.RegistroAvanceDTO;
import backend.com.produccion.application.service.GestionarOrdenTrabajoUseCase;
import backend.com.produccion.application.service.RegistrarAvanceUseCase;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produccion/ordenes-trabajo")
@RequiredArgsConstructor
public class OrdenTrabajoController {

    private final GestionarOrdenTrabajoUseCase gestionarOTUseCase;
    private final RegistrarAvanceUseCase registrarAvanceUseCase;
    private final OrdenTrabajoRepository repository;

    @GetMapping("/nota-venta/{nvId}")
    public List<OrdenTrabajo> getByNotaVenta(@PathVariable Long nvId) {
        return gestionarOTUseCase.listarPorNotaVenta(nvId);
    }

    @GetMapping("/orden-produccion/{opId}")
    public List<OrdenTrabajo> getByOrdenProduccion(@PathVariable Long opId) {
        return repository.findByOrdenProduccionId(opId);
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

    @PostMapping("/{id}/avance")
    public ResponseEntity<RegistroAvanceDTO> registrarAvance(@PathVariable Long id,
            @RequestBody RegistrarAvanceCommand command) {
        return ResponseEntity.ok(registrarAvanceUseCase.registrar(id, command));
    }

    @GetMapping("/{id}/avances")
    public List<RegistroAvanceDTO> listarAvances(@PathVariable Long id) {
        return registrarAvanceUseCase.listarPorOT(id);
    }
}
