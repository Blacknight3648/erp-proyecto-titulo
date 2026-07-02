package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.UseCase.CalcularAvanceUseCase;
import backend.com.produccion.application.UseCase.ActualizarSeguimientoUseCase;
import backend.com.produccion.application.dto.ActualizarSeguimientoCommand;
import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.application.dto.OPResponse;
import backend.com.produccion.application.dto.SeguimientoOPDTO;
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

    @GetMapping
    public List<OPResponse> getAll() {
        return repository.findAll().stream()
                .map(OPResponse::fromDomain)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public OPResponse getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(OPResponse::fromDomain)
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
