package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.TipoAccesorioDTO;
import backend.com.shared.domain.model.TipoAccesorio;
import backend.com.shared.infrastructure.persistence.entity.TipoAccesorioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TipoAccesorioMapper {

    public TipoAccesorio toDomain(TipoAccesorioJpaEntity entity) {
        if (entity == null) return null;
        return TipoAccesorio.builder()
                .idTipoAccesorio(entity.getIdTipoAccesorio())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .build();
    }

    public TipoAccesorioJpaEntity toEntity(TipoAccesorio domain) {
        if (domain == null) return null;
        return TipoAccesorioJpaEntity.builder()
                .idTipoAccesorio(domain.getIdTipoAccesorio())
                .codigo(domain.getCodigo())
                .nombre(domain.getNombre())
                .build();
    }

    public TipoAccesorioDTO toDTO(TipoAccesorio domain) {
        if (domain == null) return null;
        return TipoAccesorioDTO.builder()
                .idTipoAccesorio(domain.getIdTipoAccesorio())
                .codigo(domain.getCodigo())
                .nombre(domain.getNombre())
                .build();
    }

    public TipoAccesorio toDomain(TipoAccesorioDTO dto) {
        if (dto == null) return null;
        return TipoAccesorio.builder()
                .idTipoAccesorio(dto.getIdTipoAccesorio())
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .build();
    }
}
