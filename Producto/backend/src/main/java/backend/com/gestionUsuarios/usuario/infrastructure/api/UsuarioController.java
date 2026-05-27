package backend.com.gestionUsuarios.usuario.infrastructure.api;

import backend.com.gestionUsuarios.usuario.infrastructure.mapper.UserMapper;
import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.gestionUsuarios.usuario.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.usuario.application.dto.UserDTO;
import backend.com.gestionUsuarios.usuario.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        List<UserDTO> usuarios = userService.listarUsuarios()
                .stream()
                .map(userMapper::toUserDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        User user = userService.obtenerUsuario(id);
        return ResponseEntity.ok(userMapper.toUserDTO(user));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody CreateUserDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }
        User created = userService.crearUsuario(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userMapper.toUserDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody CreateUserDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (dto == null) {
            throw new IllegalArgumentException("El DTO no puede ser nulo");
        }

        // Buscar roles y áreas e inyectarlos correctamente llamando al nuevo método del
        // servicio
        User updated = userService.actualizarUsuario(id, dto);

        return ResponseEntity.ok(userMapper.toUserDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        userService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/toggle-enabled")
    public ResponseEntity<UserDTO> toggleEnabled(@PathVariable Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        User updated = userService.toggleEnabled(id);
        return ResponseEntity.ok(userMapper.toUserDTO(updated));
    }

    // Endpoints opcionales para asignar roles o áreas individualmente
    @PutMapping("/{id}/roles")
    public ResponseEntity<UserDTO> assignRoles(@PathVariable Long id, @RequestBody Set<String> roles) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (roles == null) {
            throw new IllegalArgumentException("El set de roles no puede ser nulo");
        }
        User updated = userService.asignarRoles(id, userMapper.toRoleEntities(roles));
        return ResponseEntity.ok(userMapper.toUserDTO(updated));
    }

    @PutMapping("/{id}/areas")
    public ResponseEntity<UserDTO> assignAreas(@PathVariable Long id, @RequestBody Set<String> areas) {
        if (id == null) {
            throw new IllegalArgumentException("El id no puede ser nulo");
        }
        if (areas == null) {
            throw new IllegalArgumentException("El set de áreas no puede ser nulo");
        }
        User updated = userService.asignarAreas(id, userMapper.toAreaEntities(areas));
        return ResponseEntity.ok(userMapper.toUserDTO(updated));
    }
}
