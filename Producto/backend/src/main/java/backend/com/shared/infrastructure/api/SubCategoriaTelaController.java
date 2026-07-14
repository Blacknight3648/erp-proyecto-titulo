package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.SubCategoriaTelaDTO;
import backend.com.shared.application.service.SubCategoriaTelaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maestros/subcategorias-tela")
@RequiredArgsConstructor
public class SubCategoriaTelaController {

    private final SubCategoriaTelaService subCategoriaTelaService;

    @PostMapping
    public ResponseEntity<SubCategoriaTelaDTO> crear(@Valid @RequestBody SubCategoriaTelaDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subCategoriaTelaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubCategoriaTelaDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody SubCategoriaTelaDTO request) {
        return ResponseEntity.ok(subCategoriaTelaService.actualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubCategoriaTelaDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(subCategoriaTelaService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<SubCategoriaTelaDTO>> listarTodas() {
        return ResponseEntity.ok(subCategoriaTelaService.listarTodas());
    }

    @GetMapping("/por-categoria-tela/{idCategoriaTela}")
    public ResponseEntity<List<SubCategoriaTelaDTO>> listarPorCategoriaTela(
            @PathVariable Integer idCategoriaTela) {
        return ResponseEntity.ok(subCategoriaTelaService.listarPorCategoriaTela(idCategoriaTela));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Integer id) {
        subCategoriaTelaService.eliminar(id);
    }
}
