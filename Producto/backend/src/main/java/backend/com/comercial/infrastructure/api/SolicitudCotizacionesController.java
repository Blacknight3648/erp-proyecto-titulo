package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.SolicitudCotizacionesCreateDTO;
import backend.com.comercial.application.dto.SolicitudCotizacionesDTO;
import backend.com.comercial.application.service.SolicitudCotizacionesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/solicitudes-cotizaciones")
@RequiredArgsConstructor
public class SolicitudCotizacionesController {

    private final SolicitudCotizacionesService service;

    @PostMapping
    public ResponseEntity<SolicitudCotizacionesDTO> create(@RequestBody SolicitudCotizacionesCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudCotizacionesDTO> update(@PathVariable Long id, @RequestBody SolicitudCotizacionesCreateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudCotizacionesDTO> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cotización no encontrada con ID: " + id));
    }

    @GetMapping
    public ResponseEntity<List<SolicitudCotizacionesDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
