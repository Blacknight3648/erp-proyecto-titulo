package backend.com.gestionUsuarios.infrastructure.mapper;

import backend.com.gestionUsuarios.application.dto.AreaDTO;
import backend.com.gestionUsuarios.application.dto.RoleDTO;
import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.application.dto.UserDTO;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.UserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final RoleMapper roleMapper;
    private final AreaMapper areaMapper;

    public UserDTO toUserDTO(User domain) {
        if (domain == null) return null;

        Set<RoleDTO> roles = null;
        if (domain.getRoles() != null) {
            roles = domain.getRoles().stream()
                    .map(roleMapper::toDTO)
                    .collect(Collectors.toSet());
        }

        Set<AreaDTO> areas = null;
        if (domain.getAreas() != null) {
            areas = domain.getAreas().stream()
                    .map(areaMapper::toDTO)
                    .collect(Collectors.toSet());
        }

        return UserDTO.builder()
                .usuarioId(domain.getUsuarioId())
                .usuarioRun(domain.getUsuarioRun())
                .usuarioNombre(domain.getUsuarioNombre())
                .usuarioApellidos(domain.getUsuarioApellidos())
                .usuarioEmail(domain.getUsuarioEmail())
                .telefono(domain.getTelefono())
                .enabled(domain.isEnabled())
                .roles(roles)
                .areas(areas)
                .build();
    }

    public User toUser(UserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .usuarioId(dto.getUsuarioId())
                .usuarioRun(dto.getUsuarioRun())
                .usuarioNombre(dto.getUsuarioNombre())
                .usuarioApellidos(dto.getUsuarioApellidos())
                .usuarioEmail(dto.getUsuarioEmail())
                .usuarioPassword(dto.getUsuarioPassword())
                .telefono(dto.getTelefono())
                .enabled(dto.isEnabled())
                .build();
    }

    public User toUser(CreateUserDTO dto) {
        if (dto == null) return null;

        return User.builder()
                .usuarioRun(dto.getUsuarioRun())
                .usuarioNombre(dto.getUsuarioNombre())
                .usuarioApellidos(dto.getUsuarioApellidos())
                .usuarioEmail(dto.getUsuarioEmail())
                .usuarioPassword(dto.getUsuarioPassword())
                .telefono(dto.getTelefono())
                .enabled(true)
                .build();
    }

    public UserJpaEntity toJpaEntity(User domain) {
        if (domain == null) return null;

        return UserJpaEntity.builder()
                .usuarioId(domain.getUsuarioId())
                .usuarioRun(domain.getUsuarioRun())
                .usuarioNombre(domain.getUsuarioNombre())
                .usuarioApellidos(domain.getUsuarioApellidos())
                .usuarioEmail(domain.getUsuarioEmail())
                .usuarioPassword(domain.getUsuarioPassword())
                .telefono(domain.getTelefono())
                .enabled(domain.isEnabled())
                .roles(domain.getRoles() != null ? domain.getRoles().stream().map(roleMapper::toJpaEntity).collect(Collectors.toSet()) : null)
                .areas(domain.getAreas() != null ? domain.getAreas().stream().map(areaMapper::toJpaEntity).collect(Collectors.toSet()) : null)
                .build();
    }

    public User toDomain(UserJpaEntity entity) {
        if (entity == null) return null;

        return User.builder()
                .usuarioId(entity.getUsuarioId())
                .usuarioRun(entity.getUsuarioRun())
                .usuarioNombre(entity.getUsuarioNombre())
                .usuarioApellidos(entity.getUsuarioApellidos())
                .usuarioEmail(entity.getUsuarioEmail())
                .usuarioPassword(entity.getUsuarioPassword())
                .telefono(entity.getTelefono())
                .enabled(entity.isEnabled())
                .roles(entity.getRoles() != null ? entity.getRoles().stream().map(roleMapper::toDomain).collect(Collectors.toSet()) : null)
                .areas(entity.getAreas() != null ? entity.getAreas().stream().map(areaMapper::toDomain).collect(Collectors.toSet()) : null)
                .build();
    }
}
