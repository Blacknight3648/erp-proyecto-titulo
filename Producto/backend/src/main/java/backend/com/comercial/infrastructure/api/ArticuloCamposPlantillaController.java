package backend.com.comercial.infrastructure.api;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.ArticuloCamposPlantillaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/comercial/modelos-plantilla")
@RequiredArgsConstructor
public class ArticuloCamposPlantillaController {

    private final ArticuloCamposPlantillaService modeloPlantillaService;

    /** Crea o actualiza (upsert) los campos de plantilla de un artículo. */
    @PostMapping("/crear-plantilla")
    public ResponseEntity<ArticuloCamposPlantillaDTO> guardar(
            @Valid @RequestBody ArticuloCamposPlantillaDTO request) {
        return ResponseEntity.ok(modeloPlantillaService.guardar(request));
    }
    

    /** Devuelve la configuración del artículo: { idArticulo, nombreArticulo, camposPlantilla:[...] }. */
    @GetMapping("/articulo/{idArticulo}")
    public ResponseEntity<ArticuloCamposPlantillaDTO> obtenerPorArticulo(@PathVariable Integer idArticulo) {
        return modeloPlantillaService.obtenerPorArticulo(idArticulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/articulo/{idArticulo}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminarPorArticulo(@PathVariable Integer idArticulo) {
        modeloPlantillaService.eliminarPorArticulo(idArticulo);
    }
}
