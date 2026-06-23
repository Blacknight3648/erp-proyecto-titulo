package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.TipoArticuloDTO;
import backend.com.shared.domain.model.TipoArticulo;
import backend.com.shared.infrastructure.persistence.entity.TipoArticuloJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TipoArticuloMapper {

    public TipoArticulo toDomain(TipoArticuloJpaEntity entity) {
        if (entity == null) return null;
        return TipoArticulo.builder()
                .idTipoArticulo(entity.getIdTipoArticulo())
                .codigo(entity.getCodigo() != null ? entity.getCodigo().name() : null)
                .nombre(entity.getNombre())
                .build();
    }

    public TipoArticuloJpaEntity toEntity(TipoArticulo domain) {
        if (domain == null) return null;
        backend.com.shared.domain.enums.TipoArticulo codigoEnum = domain.getCodigo() != null
                ? backend.com.shared.domain.enums.TipoArticulo.valueOf(domain.getCodigo())
                : null;
        return TipoArticuloJpaEntity.builder()
                .idTipoArticulo(domain.getIdTipoArticulo())
                .codigo(codigoEnum)
                .nombre(domain.getNombre())
                .build();
    }

    public TipoArticuloDTO toDTO(TipoArticulo domain) {
        if (domain == null) return null;
        return TipoArticuloDTO.builder()
                .idTipoArticulo(domain.getIdTipoArticulo())
                .codigo(domain.getCodigo())
                .nombre(domain.getNombre())
                .build();
    }

    public TipoArticulo toDomain(TipoArticuloDTO dto) {
        if (dto == null) return null;
        return TipoArticulo.builder()
                .idTipoArticulo(dto.getIdTipoArticulo())
                .codigo(dto.getCodigo())
                .nombre(dto.getNombre())
                .build();
    }
}
