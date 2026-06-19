package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.ArticuloCamposPlantillaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v3/comercial/modelos-plantilla")
@RequiredArgsConstructor
public class ArticuloCamposPlantillaController {

    private final ArticuloCamposPlantillaService modeloPlantillaService;

    @PostMapping
    public ResponseEntity<ArticuloCamposPlantillaDTO> crear(@Valid @RequestBody ArticuloCamposPlantillaDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modeloPlantillaService.crear(request));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ArticuloCamposPlantillaDTO>> guardarCampos(
            @Valid @RequestBody List<ArticuloCamposPlantillaDTO> request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(modeloPlantillaService.guardarCampos(request));
    }

    @GetMapping("/nombre-articulo/{nombreArticulo}")
    public ResponseEntity<List<ArticuloCamposPlantillaDTO>> listarPorNombreArticulo(@PathVariable String nombreArticulo) {
        return ResponseEntity.ok(modeloPlantillaService.listarPorNombreArticulo(nombreArticulo));
    }

    @GetMapping("/articulo/{idArticulo}")
    public ResponseEntity<List<ArticuloCamposPlantillaDTO>> listarPorArticulo(@PathVariable Integer idArticulo) {
        return ResponseEntity.ok(modeloPlantillaService.listarPorArticulo(idArticulo));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        modeloPlantillaService.eliminar(id);
    }
}
