package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historial-estado")
@RequiredArgsConstructor
public class HistorialEstadoController {

    private final HistorialEstadoService service;

    @GetMapping("/{tipoEntidad}/{entidadId}")
    public List<HistorialEstadoDTO> consultar(@PathVariable String tipoEntidad,
            @PathVariable Long entidadId) {
        return service.consultar(tipoEntidad, entidadId);
    }
}
