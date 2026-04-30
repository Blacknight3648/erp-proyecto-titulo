package backend.com.gestionUsuarios.role.infrastructure.api;

import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.gestionUsuarios.role.application.dto.RoleDTO;
import backend.com.gestionUsuarios.role.infrastructure.mapper.RoleMapper;
import backend.com.gestionUsuarios.role.application.service.RoleService;
import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.area.domain.repository.AreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;
    private final RoleMapper roleMapper;
    private final AreaRepository areaRepository; // necesario para actualizar areaId

    @GetMapping
    public ResponseEntity<List<RoleDTO>> listarRoles() {
        return ResponseEntity.ok(roleMapper.toDTOList(roleService.listarRoles()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(roleMapper.toDTO(roleService.obtenerRole(id)));
    }

    @PostMapping
    public ResponseEntity<RoleDTO> crear(@RequestBody RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
        Role nueva = roleService.crearRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(roleMapper.toDTO(nueva));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> actualizar(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        Role role = roleMapper.toEntity(roleDTO);
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
        // actualizar permisos si vienen en el DTO
        if (roleDTO.getPermisosIds() != null) {
            Set<Long> ids = roleDTO.getPermisosIds();
            roleExistente.setPermisos(
                    roleExistente.getPermisos().stream()
                            .filter(p -> ids.contains(p.getId()))
                            .collect(Collectors.toSet()));
        }

        Role actualizada = roleService.actualizarRole(id, roleExistente);
        return ResponseEntity.ok(roleMapper.toDTO(actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        roleService.eliminarRole(id);
        return ResponseEntity.noContent().build();
    }
}