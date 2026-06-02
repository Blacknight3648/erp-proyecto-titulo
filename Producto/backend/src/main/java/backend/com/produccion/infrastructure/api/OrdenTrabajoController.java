package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.UseCase.GestionarOrdenTrabajoUseCase;
import backend.com.produccion.application.UseCase.RegistrarAvanceUseCase;
import backend.com.produccion.application.dto.OTResponse;
import backend.com.produccion.application.dto.RegistrarAvanceCommand;
import backend.com.produccion.application.dto.RegistroAvanceDTO;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/produccion/ordenes-trabajo")
@RequiredArgsConstructor
@Tag(name = "Producción - Órdenes de Trabajo", description = "OTs por fase (Corte, Confección...) — registro de avances y mermas")
public class OrdenTrabajoController {

    private final GestionarOrdenTrabajoUseCase gestionarOTUseCase;
    private final RegistrarAvanceUseCase registrarAvanceUseCase;
    private final OrdenTrabajoRepository repository;

    @GetMapping("/nota-venta/{nvId}")
    @Operation(summary = "Listar OTs por Nota de Venta")
    public List<OTResponse> getByNotaVenta(@PathVariable Long nvId) {
        return gestionarOTUseCase.listarPorNotaVenta(nvId).stream()
                .map(OTResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @GetMapping("/orden-produccion/{opId}")
    @Operation(summary = "Listar OTs (todas las fases) de una Orden de Producción")
    public List<OTResponse> getByOrdenProduccion(@PathVariable Long opId) {
        return repository.findByOrdenProduccionId(opId).stream()
                .map(OTResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/iniciar")
    @Operation(summary = "Iniciar OT (cambio de estado PENDIENTE → EN_PROCESO)")
    public ResponseEntity<Void> iniciar(@PathVariable Long id) {
        gestionarOTUseCase.iniciarOT(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/finalizar")
    @Operation(summary = "Finalizar OT manualmente")
    public ResponseEntity<Void> finalizar(@PathVariable Long id) {
        gestionarOTUseCase.finalizarOT(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/avance")
    @Operation(summary = "Registrar avance / merma del día en una OT")
    public ResponseEntity<RegistroAvanceDTO> registrarAvance(@PathVariable Long id,
            @Valid @RequestBody RegistrarAvanceCommand command) {
        return ResponseEntity.ok(registrarAvanceUseCase.registrar(id, command));
    }

    @GetMapping("/{id}/avances")
    @Operation(summary = "Listar avances registrados para una OT")
    public List<RegistroAvanceDTO> listarAvances(@PathVariable Long id) {
        return registrarAvanceUseCase.listarPorOT(id);
    }
}
