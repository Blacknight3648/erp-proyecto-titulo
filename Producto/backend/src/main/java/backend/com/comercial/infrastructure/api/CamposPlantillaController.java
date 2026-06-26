package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.CamposPlantillaDTO;
import backend.com.comercial.application.service.CamposPlantillaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comercial/plantillas")
@RequiredArgsConstructor
public class CamposPlantillaController {

    private final CamposPlantillaService plantillaService;

    @PostMapping
    public ResponseEntity<CamposPlantillaDTO> crear(@Valid @RequestBody CamposPlantillaDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(plantillaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CamposPlantillaDTO> actualizar(@PathVariable Long id,
                                                   @Valid @RequestBody CamposPlantillaDTO request) {
        return ResponseEntity.ok(plantillaService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CamposPlantillaDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(plantillaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<CamposPlantillaDTO>> listarTodas() {
        return ResponseEntity.ok(plantillaService.listarTodas());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        plantillaService.eliminar(id);
    }
}
