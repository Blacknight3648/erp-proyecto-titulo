package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.TipoDireccion;
import org.springframework.stereotype.Component;

@Component
public class TipoDireccionMapper {

    public TipoDireccion toDomain(backend.com.shared.infrastructure.persistence.entity.TipoDireccion entity) {
        if (entity == null) return null;
        return TipoDireccion.builder()
                .tipoDireccionId(entity.getTipoDireccionId())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public backend.com.shared.infrastructure.persistence.entity.TipoDireccion toEntity(TipoDireccion domain) {
        if (domain == null) return null;
        backend.com.shared.infrastructure.persistence.entity.TipoDireccion entity =
                new backend.com.shared.infrastructure.persistence.entity.TipoDireccion();
        entity.setTipoDireccionId(domain.getTipoDireccionId());
        entity.setDescripcion(domain.getDescripcion());
        return entity;
    }
}
