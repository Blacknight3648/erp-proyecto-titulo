package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.application.dto.SolicitudCostosDTO;
import backend.com.comercial.application.service.SolicitudCostosService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/solicitudes-costos")
@RequiredArgsConstructor
public class SolicitudCostosController {

    private final SolicitudCostosService solicitudCostosService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudCostosDTO create(@Valid @RequestBody SolicitudCostosCreateDTO dto) {
        return solicitudCostosService.create(dto);
    }

    @PutMapping("/{id}")
    public SolicitudCostosDTO update(@PathVariable Long id, @Valid @RequestBody SolicitudCostosCreateDTO dto) {
        return solicitudCostosService.update(id, dto);
    }

    @GetMapping("/{id}")
    public SolicitudCostosDTO findById(@PathVariable Long id) {
        return solicitudCostosService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Solicitud no encontrada"));
    }

    @GetMapping
    public List<SolicitudCostosDTO> findAll() {
        return solicitudCostosService.findAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        solicitudCostosService.delete(id);
    }
}
