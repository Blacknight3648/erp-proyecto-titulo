package backend.com.produccion.infrastructure.api;

import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/produccion/ordenes-produccion")
@RequiredArgsConstructor
public class OrdenProduccionController {

    private final OrdenProduccionRepository repository;

    @GetMapping
    public List<OrdenProduccion> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdenProduccion> getById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/recepcionar/{id}")
    public ResponseEntity<OrdenProduccion> recepcionar(@PathVariable Long id) {
        return repository.findById(id)
                .map(op -> {
                    op.recepcionar();
                    return ResponseEntity.ok(repository.save(op));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
