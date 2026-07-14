package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.UseCase.CalcularAvanceUseCase;
import backend.com.produccion.application.UseCase.ActualizarSeguimientoUseCase;
import backend.com.produccion.application.dto.ActualizarSeguimientoCommand;
import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.application.dto.OPResponse;
import backend.com.produccion.application.dto.SeguimientoOPDTO;
import backend.com.produccion.application.service.DashboardOPService;
import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.SeguimientoOPRepository;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/produccion/ordenes-produccion")
@RequiredArgsConstructor
public class OrdenProduccionController {

    private final OrdenProduccionRepository repository;
    private final CalcularAvanceUseCase calcularAvanceUseCase;
    private final ActualizarSeguimientoUseCase actualizarSeguimientoUseCase;
    private final SeguimientoOPRepository seguimientoRepository;
    private final CosteoVersionRepository costeoVersionRepository;
    private final CosteoRepository costeoRepository;
    private final DashboardOPService dashboardOPService;

    @GetMapping
    public List<OPResponse> getAll() {
        return repository.findAll().stream()
                .map(this::toResponseConAlerta)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OPResponse getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(this::toResponseConAlerta)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Producción no encontrada: " + id));
    }

    /** OP(s) de una Nota de Venta, con el número/estado del costeo vinculado resuelto. */
    @GetMapping("/por-nota-venta/{notaVentaId}")
    public List<OPResponse> getByNotaVentaId(@PathVariable Long notaVentaId) {
        return repository.findByNotaVentaId(notaVentaId).stream()
                .map(this::toResponseConCosteo)
                .collect(Collectors.toList());
    }

    private OPResponse toResponseConCosteo(backend.com.produccion.domain.model.OrdenProduccion op) {
        OPResponse r = OPResponse.fromDomain(op);
        if (op.getCosteoVersionId() != null) {
            costeoVersionRepository.findById(op.getCosteoVersionId())
                    .flatMap(version -> costeoRepository.findById(version.getCosteoId()))
                    .ifPresent(costeo -> {
                        r.setNumeroCosteo(costeo.getNumeroCosteo() != null ? costeo.getNumeroCosteo().getValue() : null);
                        r.setEstadoCosteo(costeo.getEstado() != null ? costeo.getEstado().name() : null);
                    });
        }
        return r;
    }

    /**
     * Adjunta TODAS las alertas de atraso vigentes (misma regla SLA que el dashboard
     * operacional; una OP puede tener varias a la vez). Solo se evalúa para OPs en estado
     * activo, igual que {@code DashboardOPService.calcular()}, para que los conteos
     * agregados y esta alerta por-OP sean siempre consistentes.
     */
    private OPResponse toResponseConAlerta(backend.com.produccion.domain.model.OrdenProduccion op) {
        OPResponse r = OPResponse.fromDomain(op);
        if (!DashboardOPService.esEstadoActivo(op.getEstado())) {
            return r;
        }
        SeguimientoOP seguimiento = seguimientoRepository.findByOrdenProduccionId(op.getIdOP()).orElse(null);
        r.setAlertas(dashboardOPService.evaluarAlertas(seguimiento));
        return r;
    }

    @PostMapping("/recepcionar/{id}")
    public OPResponse recepcionar(@PathVariable Long id) {
        return repository.findById(id)
                .map(op -> {
                    op.recepcionar();
                    return OPResponse.fromDomain(repository.save(op));
                })
                .orElseThrow(() -> new EntityNotFoundException("Orden de Producción no encontrada: " + id));
    }

    @GetMapping("/{id}/avance")
    public ResponseEntity<AvanceOPResponse> avance(@PathVariable Long id) {
        return ResponseEntity.ok(calcularAvanceUseCase.calcular(id));
    }

    @GetMapping("/{id}/seguimiento")
    public ResponseEntity<SeguimientoOPDTO> getSeguimiento(@PathVariable Long id) {
        SeguimientoOP seg = seguimientoRepository.findByOrdenProduccionId(id)
                .orElseGet(() -> new SeguimientoOP(id));
        return ResponseEntity.ok(SeguimientoOPDTO.from(seg));
    }

    @PutMapping("/{id}/seguimiento")
    public ResponseEntity<SeguimientoOPDTO> actualizarSeguimiento(@PathVariable Long id, @RequestBody ActualizarSeguimientoCommand cmd) {
        return ResponseEntity.ok(actualizarSeguimientoUseCase.actualizar(id, cmd));
    }
}
