package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.TrazabilidadOPDTO;
import backend.com.produccion.application.service.TrazabilidadOPUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trazabilidad")
@RequiredArgsConstructor
public class TrazabilidadController {

    private final TrazabilidadOPUseCase trazabilidadOPUseCase;

    @GetMapping("/op/{opId}")
    public ResponseEntity<TrazabilidadOPDTO> obtenerPorOP(@PathVariable Long opId) {
        return ResponseEntity.ok(trazabilidadOPUseCase.ejecutar(opId));
    }
}
