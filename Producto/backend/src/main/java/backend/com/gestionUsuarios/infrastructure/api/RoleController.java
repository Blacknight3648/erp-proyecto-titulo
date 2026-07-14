package backend.com.gestionUsuarios.infrastructure.api;

import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.gestionUsuarios.application.dto.RoleDTO;
import backend.com.gestionUsuarios.infrastructure.mapper.RoleMapper;
import backend.com.gestionUsuarios.application.service.RoleService;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.shared.domain.model.Permiso;
import backend.com.shared.infrastructure.persistence.repository.PermisoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final AreaRepository areaRepository;
    private final PermisoRepository permisoRepository;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> listarRoles() {
        return ResponseEntity.ok(roleMapper.toDTOList(roleService.listarRoles()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.obtenerRole(id)));
    }

    @GetMapping("/{id}/permisos")
    public ResponseEntity<Set<Permiso>> obtenerPermisosPorRol(@PathVariable Long id) {
        Role role = roleService.obtenerRole(id);
        return ResponseEntity.ok(role.getPermisos());
    }

    @PostMapping
    public ResponseEntity<RoleDTO> crear(@RequestBody RoleDTO roleDTO) {
        Role role = roleMapper.toDomain(roleDTO);
        Role nueva = roleService.crearRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toDTO(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> actualizar(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        Role role = roleMapper.toDomain(roleDTO);
        Role actualizada = roleService.actualizarRole(id, role);
        return ResponseEntity.ok(roleMapper.toDTO(actualizada));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<RoleDTO> actualizarParcial(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        Role roleExistente = roleService.obtenerRole(id);

        if (roleDTO.getNombre() != null) {
            roleExistente.setNombre(roleDTO.getNombre());
        }
        if (roleDTO.getDescripcion() != null) {
            roleExistente.setDescripcion(roleDTO.getDescripcion());
        }
        if (roleDTO.getAreaId() != null) {
            Long areaId = java.util.Objects.requireNonNull(roleDTO.getAreaId());
            Area area = areaRepository.findById(areaId)
                    .orElseThrow(() -> new RuntimeException("Área no encontrada"));
            roleExistente.setArea(area);
        }

        Role actualizada = roleService.actualizarRole(id, roleExistente);

        // Reemplazo completo (no un filtro sobre los permisos actuales) para que la
        // operación pueda tanto agregar como quitar permisos del rol.
        if (roleDTO.getPermisosIds() != null) {
            Set<Permiso> nuevosPermisos = new HashSet<>(permisoRepository.findAllById(roleDTO.getPermisosIds()));
            actualizada = roleService.actualizarPermisos(id, nuevosPermisos);
        }

        return ResponseEntity.ok(roleMapper.toDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        roleService.eliminarRole(id);
        return ResponseEntity.noContent().build();
    }
}
