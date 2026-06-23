package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.UnidadMedidaDTO;
import backend.com.shared.domain.model.UnidadMedida;
import backend.com.shared.infrastructure.persistence.entity.UnidadMedidaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UnidadMedidaMapper {

    public UnidadMedida toDomain(UnidadMedidaJpaEntity entity) {
        if (entity == null) return null;
        return UnidadMedida.builder()
                .idUnidadMedida(entity.getIdUnidadMedida())
                .nombreUnidad(entity.getNombreUnidad())
                .abreviatura(entity.getAbreviatura())
                .build();
    }

    public UnidadMedidaJpaEntity toEntity(UnidadMedida domain) {
        if (domain == null) return null;
        return UnidadMedidaJpaEntity.builder()
                .idUnidadMedida(domain.getIdUnidadMedida())
                .nombreUnidad(domain.getNombreUnidad())
                .abreviatura(domain.getAbreviatura())
                .build();
    }

    public UnidadMedidaDTO toDTO(UnidadMedida domain) {
        if (domain == null) return null;
        return UnidadMedidaDTO.builder()
                .idUnidadMedida(domain.getIdUnidadMedida())
                .nombreUnidad(domain.getNombreUnidad())
                .abreviatura(domain.getAbreviatura())
                .build();
    }

    public UnidadMedida toDomain(UnidadMedidaDTO dto) {
        if (dto == null) return null;
        return UnidadMedida.builder()
                .idUnidadMedida(dto.getIdUnidadMedida())
                .nombreUnidad(dto.getNombreUnidad())
                .abreviatura(dto.getAbreviatura())
                .build();
    }
}