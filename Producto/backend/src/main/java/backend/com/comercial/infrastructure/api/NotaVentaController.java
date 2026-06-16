package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.UseCase.ConsultarTrazabilidadUseCase;
import backend.com.comercial.application.UseCase.CrearNVUseCase;
import backend.com.comercial.application.UseCase.GestionarNVUseCase;
import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.shared.application.dto.DocumentTraceDTO;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.application.service.NumeroDocumentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/comercial/notas-venta")
@RequiredArgsConstructor
public class NotaVentaController {

    private final CrearNVUseCase crearNVUseCase;
    private final ConsultarTrazabilidadUseCase consultarTrazabilidadUseCase;
    private final GestionarNVUseCase gestionarNVUseCase;
    private final NotaVentaRepository repository;
    private final HistorialEstadoService historialService;
    private final NumeroDocumentoService numeroDocumentoService;

    @GetMapping
    public List<NVResponse> listar() {
        return repository.findAll().stream()
                .map(NVResponse::fromDomain)
                .collect(Collectors.toList());
    }

    /**
     * Devuelve un número tentativo basado en el máximo actual.
     * NO reserva el número (operación de solo lectura, no incrementa el contador):
     * el valor definitivo lo asigna el backend de forma atómica al hacer POST.
     */
    @GetMapping("/next-number")
    public String previsualizarProximo() {
        return numeroDocumentoService.previsualizar("NV").getValue();
    }

    @PostMapping
    public NVResponse crear(@Valid @RequestBody CrearNVCommand command) {
        return crearNVUseCase.ejecutar(command);
    }

    @GetMapping("/{id}")
    public NVResponse obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(NVResponse::fromDomain)
                .orElseThrow(() -> new EntityNotFoundException("Nota de Venta no encontrada: " + id));
    }

    @GetMapping("/{id}/trazabilidad")
    public List<DocumentTraceDTO> getTrazabilidad(@PathVariable Long id) {
        return consultarTrazabilidadUseCase.ejecutar(id);
    }

    @PatchMapping("/{id}/aprobar")
    public NVResponse aprobar(@PathVariable Long id, @Valid @RequestBody FirmaAprobacionRequest body) {
        return gestionarNVUseCase.aprobar(id, body.getAprobador(), body.getObservacion());
    }

    @PatchMapping("/{id}/cancelar")
    public NVResponse cancelar(@PathVariable Long id, @Valid @RequestBody FirmaAprobacionRequest body) {
        // En cancelar, el campo "usuario" lo aceptamos como "aprobador"
        return gestionarNVUseCase.cancelar(id, body.getAprobador(), body.getMotivo());
    }

    @GetMapping("/{id}/historial")
    public List<HistorialEstadoDTO> historial(@PathVariable Long id) {
        return historialService.consultar("NV", id);
    }
}
