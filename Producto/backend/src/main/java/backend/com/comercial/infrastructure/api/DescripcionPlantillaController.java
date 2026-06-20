package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;
import backend.com.comercial.application.service.DescripcionPlantillaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DescripcionPlantillaController {

    private final DescripcionPlantillaService descripcionService;

    

    @GetMapping("/api/v3/comercial/scos/{idSCOS}/descripciones")
    public ResponseEntity<List<DescripcionPlantillaDTO>> listarPorSCOS(@PathVariable Long idSCOS) {
        return ResponseEntity.ok(descripcionService.listarPorSCOS(idSCOS));
    }

    @PostMapping("/api/v3/comercial/scos/{idSCOS}/descripciones")
    public ResponseEntity<DescripcionPlantillaDTO> crear(@PathVariable Long idSCOS,
                                                         @Valid @RequestBody DescripcionPlantillaDTO request) {
        request.setIdSCOS(idSCOS);
        return ResponseEntity.status(HttpStatus.CREATED).body(descripcionService.crear(request));
    }

    /** Guarda en lote (atómico) todas las descripciones de una SCOS en una sola petición. */
    @PostMapping("/api/v3/comercial/descripciones-plantilla/bulk")
    public ResponseEntity<List<DescripcionPlantillaDTO>> guardarMultiples(
            @Valid @RequestBody List<DescripcionPlantillaDTO> request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(descripcionService.guardarMultiples(request));
    }

    @PutMapping("/api/v3/comercial/descripciones-plantilla/{id}")
    public ResponseEntity<DescripcionPlantillaDTO> actualizar(@PathVariable Long id,
                                                              @Valid @RequestBody DescripcionPlantillaDTO request) {
        return ResponseEntity.ok(descripcionService.actualizar(id, request));
    }

    @GetMapping("/api/v3/comercial/descripciones-plantilla/{id}")
    public ResponseEntity<DescripcionPlantillaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(descripcionService.obtenerPorId(id));
    }

    @DeleteMapping("/api/v3/comercial/descripciones-plantilla/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        descripcionService.eliminar(id);
    }


}
