package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.application.service.GiroService;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/giros")
@RequiredArgsConstructor
public class GiroController {

    private final GiroService giroService;
    private final GiroMapper giroMapper;

    @GetMapping
    public ResponseEntity<List<GiroDTO>> listarTodos() {
        List<Giro> giros = giroService.listarTodos();
        if (giros.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(giroMapper.toDTOList(giros));
    }

    @GetMapping("/{giroId}")
    public ResponseEntity<GiroDTO> obtenerPorId(@PathVariable Long giroId) {
        return giroService.obtenerPorId(giroId)
                .map(giroMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigoSii}")
    public ResponseEntity<GiroDTO> obtenerPorCodigoSii(@PathVariable String codigoSii) {
        return giroService.obtenerPorCodigoSii(codigoSii)
                .map(giroMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombreGiro}")
    public ResponseEntity<List<GiroDTO>> buscarPorNombreGiro(@PathVariable String nombreGiro) {
        return ResponseEntity.ok(giroMapper.toDTOList(giroService.buscarPorNombreGiro(nombreGiro)));
    }

    @GetMapping("/descripcion/{descripcionGiro}")
    public ResponseEntity<List<GiroDTO>> obtenerPorDescripcionGiro(@PathVariable String descripcionGiro) {
        return ResponseEntity.ok(giroMapper.toDTOList(giroService.obtenerPorDescripcionGiro(descripcionGiro)));
    }

    @GetMapping("/obtenerocrear/{nombreGiro}")
    public ResponseEntity<GiroDTO> obtenerOCrearPorNombreGiro(@PathVariable String nombreGiro) {
        return giroService.obtenerOCrearPorNombreGiro(nombreGiro)
                .map(giroMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<GiroDTO> crear(@Valid @RequestBody GiroDTO dto) {
        Giro creado = giroService.crear(giroMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(giroMapper.toDTO(creado));
    }

    @PutMapping("/{giroId}")
    public ResponseEntity<GiroDTO> actualizar(@PathVariable Long giroId, @Valid @RequestBody GiroDTO dto) {
        Giro actualizado = giroService.actualizar(giroId, giroMapper.toDomain(dto));
        return ResponseEntity.ok(giroMapper.toDTO(actualizado));
    }

    @DeleteMapping("/{giroId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long giroId) {
        giroService.eliminar(giroId);
        return ResponseEntity.noContent().build();
    }
}
