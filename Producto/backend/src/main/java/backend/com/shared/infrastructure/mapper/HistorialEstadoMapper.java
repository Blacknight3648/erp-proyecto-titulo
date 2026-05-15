package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.HistorialEstado;
import backend.com.shared.infrastructure.persistence.entity.HistorialEstadoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class HistorialEstadoMapper {

    public HistorialEstado toDomain(HistorialEstadoJpaEntity entity) {
        if (entity == null) return null;
        return new HistorialEstado(
                entity.getId(),
                entity.getTipoEntidad(),
                entity.getEntidadId(),
                entity.getEstadoAnterior(),
                entity.getEstadoNuevo(),
                entity.getUsuario(),
                entity.getObservacion(),
                entity.getFecha());
    }

    public HistorialEstadoJpaEntity toJpaEntity(HistorialEstado domain) {
        if (domain == null) return null;
        HistorialEstadoJpaEntity entity = new HistorialEstadoJpaEntity();
        entity.setId(domain.getId());
        entity.setTipoEntidad(domain.getTipoEntidad());
        entity.setEntidadId(domain.getEntidadId());
        entity.setEstadoAnterior(domain.getEstadoAnterior());
        entity.setEstadoNuevo(domain.getEstadoNuevo());
        entity.setUsuario(domain.getUsuario());
        entity.setObservacion(domain.getObservacion());
        entity.setFecha(domain.getFecha());
        return entity;
    }
}
