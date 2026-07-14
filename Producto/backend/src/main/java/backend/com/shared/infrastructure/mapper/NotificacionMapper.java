package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.Notificacion;
import backend.com.shared.infrastructure.persistence.entity.NotificacionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificacionMapper {

    public Notificacion toDomain(NotificacionJpaEntity entity) {
        if (entity == null) return null;
        return new Notificacion(
                entity.getId(),
                entity.getTipo(),
                entity.getMensaje(),
                entity.getCategoria(),
                entity.getPrioridad(),
                entity.isLeida(),
                entity.getFecha());
    }

    public NotificacionJpaEntity toJpaEntity(Notificacion domain) {
        if (domain == null) return null;
        NotificacionJpaEntity entity = new NotificacionJpaEntity();
        entity.setId(domain.getId());
        entity.setTipo(domain.getTipo());
        entity.setMensaje(domain.getMensaje());
        entity.setCategoria(domain.getCategoria());
        entity.setPrioridad(domain.getPrioridad());
        entity.setLeida(domain.isLeida());
        entity.setFecha(domain.getFecha());
        return entity;
    }
}
