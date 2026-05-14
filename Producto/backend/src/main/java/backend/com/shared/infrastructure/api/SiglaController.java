package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.SiglaDTO;
import backend.com.shared.application.service.SiglaService;
import backend.com.shared.domain.model.Sigla;
import backend.com.shared.infrastructure.mapper.SiglaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/siglas")
@RequiredArgsConstructor
public class SiglaController {

    private final SiglaService siglaService;
    private final SiglaMapper siglaMapper;

    @GetMapping
    public ResponseEntity<List<SiglaDTO>> obtenerTodos() {
        return ResponseEntity.ok(siglaMapper.toDTOList(siglaService.obtenerTodos()));
    }

    @GetMapping("/{siglaId}")
    public ResponseEntity<SiglaDTO> obtenerPorId(@PathVariable Long siglaId) {
        return siglaService.obtenerPorId(siglaId)
                .map(siglaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/descripcion/{descripcionSigla}")
    public ResponseEntity<List<SiglaDTO>> obtenerPorDescripcion(@PathVariable String descripcionSigla) {
        return ResponseEntity.ok(siglaMapper.toDTOList(siglaService.obtenerPorDescripcionSigla(descripcionSigla)));
    }

    @GetMapping("/abreviatura/{siglaAbreviatura}")
    public ResponseEntity<SiglaDTO> obtenerPorAbreviatura(@PathVariable String siglaAbreviatura) {
        return siglaService.obtenerPorSiglaAbreviatura(siglaAbreviatura)
                .map(siglaMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SiglaDTO> crear(@Valid @RequestBody SiglaDTO dto) {
        Sigla creada = siglaService.crear(siglaMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(siglaMapper.toDTO(creada));
    }

    @PutMapping("/{siglaId}")
    public ResponseEntity<SiglaDTO> actualizar(@PathVariable Long siglaId, @Valid @RequestBody SiglaDTO dto) {
        Sigla actualizada = siglaService.actualizar(siglaId, siglaMapper.toDomain(dto));
        return ResponseEntity.ok(siglaMapper.toDTO(actualizada));
    }

    @DeleteMapping("/{siglaId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long siglaId) {
        siglaService.eliminar(siglaId);
        return ResponseEntity.noContent().build();
    }
}
