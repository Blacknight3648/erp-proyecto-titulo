package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ClasificacionTecnicaDTO;
import backend.com.shared.domain.model.ClasificacionTecnica;
import backend.com.shared.infrastructure.persistence.entity.ClasificacionTecnicaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ClasificacionTecnicaMapper {

    public ClasificacionTecnica toDomain(ClasificacionTecnicaJpaEntity entity) {
        if (entity == null) return null;
        return ClasificacionTecnica.builder()
                .idClasificacionTecnica(entity.getIdClasificacionTecnica())
                .nombreClasificacion(entity.getNombreClasificacion())
                .build();
    }

    public ClasificacionTecnicaJpaEntity toEntity(ClasificacionTecnica domain) {
        if (domain == null) return null;
        return ClasificacionTecnicaJpaEntity.builder()
                .idClasificacionTecnica(domain.getIdClasificacionTecnica())
                .nombreClasificacion(domain.getNombreClasificacion())
                .build();
    }

    public ClasificacionTecnicaDTO toDTO(ClasificacionTecnica domain) {
        if (domain == null) return null;
        return ClasificacionTecnicaDTO.builder()
                .idClasificacionTecnica(domain.getIdClasificacionTecnica())
                .nombreClasificacion(domain.getNombreClasificacion())
                .build();
    }

    public ClasificacionTecnica toDomain(ClasificacionTecnicaDTO dto) {
        if (dto == null) return null;
        return ClasificacionTecnica.builder()
                .idClasificacionTecnica(dto.getIdClasificacionTecnica())
                .nombreClasificacion(dto.getNombreClasificacion())
                .build();
    }
}