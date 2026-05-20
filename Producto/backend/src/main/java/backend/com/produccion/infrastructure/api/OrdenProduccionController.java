package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.application.dto.OPResponse;
import backend.com.produccion.application.service.CalcularAvanceUseCase;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
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
}
