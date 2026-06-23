package backend.com.gestionUsuarios.infrastructure.mapper;

import backend.com.gestionUsuarios.application.dto.RoleDTO;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.RoleJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleMapper {

    private final AreaMapper areaMapper;

    public RoleDTO toDTO(Role domain) {
        if (domain == null) return null;

        return RoleDTO.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .areaId(domain.getArea() != null ? domain.getArea().getAreaId() : null)
                .permisosIds(domain.getPermisos() != null
                        ? domain.getPermisos().stream().map(p -> p.getId()).collect(Collectors.toSet())
                        : Set.of())
                .build();
    }

    public Role toDomain(RoleDTO dto) {
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

    public RoleJpaEntity toJpaEntity(Role domain) {
        if (domain == null) return null;

        return RoleJpaEntity.builder()
                .id(domain.getId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .area(areaMapper.toJpaEntity(domain.getArea()))
                .permisos(domain.getPermisos())
                .build();
    }

    public Role toDomain(RoleJpaEntity entity) {
        if (entity == null) return null;

        return Role.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .area(areaMapper.toDomain(entity.getArea()))
                .permisos(entity.getPermisos())
                .build();
    }

    public List<RoleDTO> toDTOList(List<Role> domains) {
        if (domains == null) return List.of();
        return domains.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
