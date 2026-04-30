package backend.com.gestionUsuarios.role.infrastructure.mapper;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.gestionUsuarios.role.application.dto.RoleDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class RoleMapper {

    public RoleDTO toDTO(Role entity) {
        if (entity == null) return null;

        return RoleDTO.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .areaId(entity.getArea() != null ? entity.getArea().getAreaId() : null)
                .permisosIds(entity.getPermisos() != null
                        ? entity.getPermisos().stream().map(p -> p.getId()).collect(Collectors.toSet())
                        : Set.of())
                .build();
    }

    public Role toEntity(RoleDTO dto) {
        if (dto == null) return null;

        Role.RoleBuilder builder = Role.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion());

        if (dto.getAreaId() != null) {
            builder.area(Area.builder().areaId(dto.getAreaId()).build());
        }

        return builder.build();
    }

    public List<RoleDTO> toDTOList(List<Role> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}