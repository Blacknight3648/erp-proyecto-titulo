package backend.com.gestionUsuarios.proveedor.infrastructure.api;

import backend.com.gestionUsuarios.proveedor.application.dto.ProveedorDTO;
import backend.com.gestionUsuarios.proveedor.application.dto.ProveedorDtoMapper;
import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.application.service.ProveedorService;
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
    private final ProveedorDtoMapper dtoMapper;

    @GetMapping
    public ResponseEntity<List<ProveedorDTO>> getAll() {

        List<Proveedor> proveedores = proveedorService.listarProveedores();

        return ResponseEntity.ok(dtoMapper.toDtoList(proveedores));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProveedorDTO> getById(@PathVariable Long id) {

        Proveedor proveedor = proveedorService.obtenerProveedor(id);

        return ResponseEntity.ok(dtoMapper.toDto(proveedor));
    }

    @PostMapping
    public ResponseEntity<ProveedorDTO> create(
            @Valid @RequestBody ProveedorDTO proveedorDTO) {

        Proveedor proveedor = dtoMapper.toDomain(proveedorDTO);
        Proveedor creado = proveedorService.crearProveedor(proveedor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dtoMapper.toDto(creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProveedorDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorDTO proveedorDTO) {

        Proveedor proveedor = dtoMapper.toDomain(proveedorDTO);
        Proveedor actualizado = proveedorService.actualizarProveedor(id, proveedor);

        return ResponseEntity.ok(dtoMapper.toDto(actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        proveedorService.eliminarProveedor(id);

        return ResponseEntity.noContent().build();
    }
}
