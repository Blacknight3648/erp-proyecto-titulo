package backend.com.gestionUsuarios.area.infrastructure.mapper;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.area.application.dto.AreaDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AreaMapper {

    public AreaDTO toDTO(Area entity) {
        if (entity == null) {
            return null;
        }
        return AreaDTO.builder()
                .areaId(entity.getAreaId())
                .nombre(entity.getNombre())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public Area toEntity(AreaDTO dto) {
        if (dto == null) {
            return null;
        }
        return Area.builder()
                .areaId(dto.getAreaId())
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .build();
    }

    public List<AreaDTO> toDTOList(List<Area> entities) {
        if (entities == null) return List.of();

        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
