package backend.com.gestionUsuarios.infrastructure.mapper;

import backend.com.gestionUsuarios.application.dto.AreaDTO;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.AreaJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AreaMapper {

    public AreaDTO toDTO(Area domain) {
        if (domain == null) {
            return null;
        }
        return AreaDTO.builder()
                .areaId(domain.getAreaId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .build();
    }

    public Area toDomain(AreaDTO dto) {
        if (dto == null) {
            return null;
        }
        return Area.builder()
                .areaId(dto.getAreaId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public AreaJpaEntity toJpaEntity(Area domain) {
        if (domain == null) {
            return null;
        }
        return AreaJpaEntity.builder()
                .areaId(domain.getAreaId())
                .nombre(domain.getNombre())
                .descripcion(domain.getDescripcion())
                .build();
    }

    public Area toDomain(AreaJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return Area.builder()
                .areaId(entity.getAreaId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public List<AreaDTO> toDTOList(List<Area> domains) {
        if (domains == null) return List.of();
        return domains.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
