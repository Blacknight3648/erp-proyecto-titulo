package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.domain.model.TipoContacto;
import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TipoContactoMapper {

    public TipoContacto toDomain(TipoContactoJpaEntity entity) {
        if (entity == null)
            return null;
        return TipoContacto.builder()
                .tipoContactoId(entity.getTipoContactoId())
                .descripcionTipoContacto(entity.getDescripcionTipoContacto())
                .build();
    }

    public TipoContactoJpaEntity toEntity(TipoContacto domain) {
        if (domain == null)
            return null;
        return TipoContactoJpaEntity.builder()
                .tipoContactoId(domain.getTipoContactoId())
                .descripcionTipoContacto(domain.getDescripcionTipoContacto())
                .build();
    }

    public TipoContactoDTO toDTO(TipoContacto domain) {
        if (domain == null)
            return null;
        TipoContactoDTO dto = new TipoContactoDTO();
        dto.setIdTipoContacto(domain.getTipoContactoId());
        dto.setDescripcionTipoContacto(domain.getDescripcionTipoContacto());
        return dto;
    }

    public TipoContacto toDomain(TipoContactoDTO dto) {
        if (dto == null)
            return null;
        return TipoContacto.builder()
                .tipoContactoId(dto.getIdTipoContacto())
                .descripcionTipoContacto(dto.getDescripcionTipoContacto())
                .build();
    }

}
