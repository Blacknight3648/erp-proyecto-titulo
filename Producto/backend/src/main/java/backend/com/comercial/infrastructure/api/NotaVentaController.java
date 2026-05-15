package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.application.service.CrearNVUseCase;
import backend.com.comercial.application.service.ConsultarTrazabilidadUseCase;
import backend.com.comercial.application.service.GestionarNVUseCase;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.shared.application.dto.DocumentTraceDTO;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comercial/notas-venta")
@RequiredArgsConstructor
public class NotaVentaController {

    private final CrearNVUseCase crearNVUseCase;
    private final ConsultarTrazabilidadUseCase consultarTrazabilidadUseCase;
    private final GestionarNVUseCase gestionarNVUseCase;
    private final NotaVentaRepository repository;
    private final HistorialEstadoService historialService;

    @GetMapping
    public ResponseEntity<List<NVResponse>> listar() {
        return ResponseEntity.ok(repository.findAll().stream()
                .map(NVResponse::fromDomain)
                .collect(java.util.stream.Collectors.toList()));
    }

    /**
     * Devuelve un número tentativo basado en el máximo actual.
     * NO reserva el número: el valor definitivo lo asigna el backend de forma
     * atómica al hacer POST (NumeroDocumentoService). Sirve solo para
     * vista previa en el formulario.
     */
    @GetMapping("/next-number")
    public Long getNextNumber() {
        return repository.findMaxNumero().orElse(0L) + 1;
    }

    @PostMapping
    public ResponseEntity<NVResponse> crear(@RequestBody CrearNVCommand command) {
        return ResponseEntity.ok(crearNVUseCase.ejecutar(command));
    }

    @GetMapping("/{id}")
    public ResponseEntity<NVResponse> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(NVResponse::fromDomain)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/trazabilidad")
    public ResponseEntity<List<DocumentTraceDTO>> getTrazabilidad(@PathVariable Long id) {
        return ResponseEntity.ok(consultarTrazabilidadUseCase.ejecutar(id));
    }

    @PatchMapping("/{id}/aprobar")
    public ResponseEntity<NVResponse> aprobar(@PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(gestionarNVUseCase.aprobar(id, body.get("aprobador"), body.get("observacion")));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<NVResponse> cancelar(@PathVariable Long id,
            @RequestBody java.util.Map<String, String> body) {
        return ResponseEntity.ok(gestionarNVUseCase.cancelar(id, body.get("usuario"), body.get("motivo")));
    }

    @GetMapping("/{id}/historial")
    public List<HistorialEstadoDTO> historial(@PathVariable Long id) {
        return historialService.consultar("NV", id);
    }
}
