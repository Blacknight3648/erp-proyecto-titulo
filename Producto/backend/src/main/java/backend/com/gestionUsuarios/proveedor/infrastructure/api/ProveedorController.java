package backend.com.gestionUsuarios.proveedor.infrastructure.api;

import backend.com.gestionUsuarios.proveedor.application.dto.ProveedorDTO;
import backend.com.gestionUsuarios.proveedor.application.service.ProveedorService;
import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.mapper.ProveedorMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/proveedores")
@RequiredArgsConstructor
public class ProveedorController {

    private final ProveedorService proveedorService;
    private final ProveedorMapper proveedorMapper;

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> listarTodos() {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.listarTodos()));
    }

    @GetMapping("/{proveedorId}")
    public ResponseEntity<ProveedorDTO> obtenerPorId(@PathVariable Long proveedorId) {
        return proveedorService.obtenerPorId(proveedorId)
                .map(proveedorMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/run/{runProveedor}")
    public ResponseEntity<ProveedorDTO> obtenerPorRun(@PathVariable String runProveedor) {
        return proveedorService.obtenerPorRun(runProveedor)
                .map(proveedorMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/razon-social/{razonSocial}")
    public ResponseEntity<List<ProveedorDTO>> buscarPorRazonSocial(@PathVariable String razonSocial) {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.buscarPorRazonSocial(razonSocial)));
    }

    @GetMapping("/activos")
    public ResponseEntity<List<ProveedorDTO>> obtenerActivos() {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.obtenerActivos()));
    }

    @GetMapping("/inactivos")
    public ResponseEntity<List<ProveedorDTO>> obtenerInactivos() {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.obtenerInactivos()));
    }

    @GetMapping("/giro/{giroId}")
    public ResponseEntity<List<ProveedorDTO>> obtenerPorGiroId(@PathVariable Long giroId) {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.obtenerPorGiroId(giroId)));
    }

    @GetMapping("/sigla/abreviatura/{sigla}")
    public ResponseEntity<List<ProveedorDTO>> obtenerPorSigla(@PathVariable String sigla) {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.obtenerPorSigla(sigla)));
    }

    @GetMapping("/giro/descripcion/{descripcionGiro}")
    public ResponseEntity<List<ProveedorDTO>> obtenerPorDescripcionGiro(@PathVariable String descripcionGiro) {
        return ResponseEntity
                .ok(proveedorMapper.toDTOList(proveedorService.obtenerPorDescripcionGiro(descripcionGiro)));
    }

    @GetMapping("/activos/sigla/{sigla}")
    public ResponseEntity<List<ProveedorDTO>> obtenerActivosPorSigla(@PathVariable String sigla) {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.obtenerActivosPorSigla(sigla)));
    }

    @GetMapping("/activos/giro/{giroId}")
    public ResponseEntity<List<ProveedorDTO>> obtenerActivosPorGiro(@PathVariable Long giroId) {
        return ResponseEntity.ok(proveedorMapper.toDTOList(proveedorService.obtenerActivosPorGiro(giroId)));
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> crear(@Valid @RequestBody ProveedorDTO dto) {
        Proveedor creado = proveedorService.crear(proveedorMapper.toDomain(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(proveedorMapper.toDTO(creado));
    }

    @PutMapping("/{proveedorId}")
    public ResponseEntity<ProveedorDTO> actualizar(@PathVariable Long proveedorId,
            @Valid @RequestBody ProveedorDTO dto) {
        Proveedor actualizado = proveedorService.actualizar(proveedorId, proveedorMapper.toDomain(dto));
        return ResponseEntity.ok(proveedorMapper.toDTO(actualizado));
    }

    @DeleteMapping("/{proveedorId}")
    public ResponseEntity<Void> eliminar(@PathVariable Long proveedorId) {
        proveedorService.eliminar(proveedorId);
        return ResponseEntity.noContent().build();
    }
}
