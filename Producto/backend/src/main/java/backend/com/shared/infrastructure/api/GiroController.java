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
        if (giros.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(giroMapper.toDTOList(giros));
    }

    @GetMapping("/{giroId}")
    public ResponseEntity<GiroDTO> obtenerPorId(@PathVariable Long giroId) {
        return giroService.obtenerPorId(giroId)
                .map(giroMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigoActividad}")
    public ResponseEntity<GiroDTO> obtenerPorCodigoActividad(@PathVariable String codigoActividad) {
        return giroService.obtenerPorCodigoActividad(codigoActividad)
                .map(giroMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/descripcion/{descripcionGiro}")
    public ResponseEntity<List<GiroDTO>> buscarPorDescripcion(@PathVariable String descripcionGiro) {
        return ResponseEntity.ok(giroMapper.toDTOList(giroService.buscarPorDescripcion(descripcionGiro)));
    }

    @GetMapping("/tipo/{tipoActividad}")
    public ResponseEntity<List<GiroDTO>> obtenerPorTipoActividad(@PathVariable String tipoActividad) {
        return ResponseEntity.ok(giroMapper.toDTOList(giroService.obtenerPorTipoActividad(tipoActividad)));
    }

    @GetMapping("/categoria/{categoriaTributaria}")
    public ResponseEntity<List<GiroDTO>> obtenerPorCategoriaTributaria(@PathVariable String categoriaTributaria) {
        return ResponseEntity.ok(giroMapper.toDTOList(giroService.obtenerPorCategoriaTributaria(categoriaTributaria)));
    }

    @GetMapping("/regimen/{regimenTributario}")
    public ResponseEntity<List<GiroDTO>> obtenerPorRegimenTributario(@PathVariable String regimenTributario) {
        return ResponseEntity.ok(giroMapper.toDTOList(giroService.obtenerPorRegimenTributario(regimenTributario)));
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
