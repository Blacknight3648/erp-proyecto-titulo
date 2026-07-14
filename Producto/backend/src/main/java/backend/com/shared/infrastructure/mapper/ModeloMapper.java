package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ModeloDTO;
import backend.com.shared.domain.model.Modelo;
import backend.com.shared.infrastructure.persistence.entity.ModeloJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ModeloMapper {

    public Modelo toDomain(ModeloJpaEntity entity) {
        if (entity == null) return null;
        return Modelo.builder()
                .idModelo(entity.getIdModelo())
                .codigoModelo(entity.getCodigoModelo())
                .nombreModelo(entity.getNombreModelo())
                .build();
    }

    public ModeloJpaEntity toEntity(Modelo domain) {
        if (domain == null) return null;
        return ModeloJpaEntity.builder()
                .idModelo(domain.getIdModelo())
                .codigoModelo(domain.getCodigoModelo())
                .nombreModelo(domain.getNombreModelo())
                .build();
    }

    public ModeloDTO toDTO(Modelo domain) {
        if (domain == null) return null;
        return ModeloDTO.builder()
                .idModelo(domain.getIdModelo())
                .codigoModelo(domain.getCodigoModelo())
                .nombreModelo(domain.getNombreModelo())
                .build();
    }

    public Modelo toDomain(ModeloDTO dto) {
        if (dto == null) return null;
        return Modelo.builder()
                .idModelo(dto.getIdModelo())
                .codigoModelo(dto.getCodigoModelo())
                .nombreModelo(dto.getNombreModelo())
                .build();
    }
}
