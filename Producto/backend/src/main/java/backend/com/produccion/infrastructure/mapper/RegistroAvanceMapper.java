package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.RegistroAvance;
import backend.com.produccion.infrastructure.persistence.entity.RegistroAvanceJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RegistroAvanceMapper {

    public RegistroAvance toDomain(RegistroAvanceJpaEntity e) {
        if (e == null) return null;
        return new RegistroAvance(
                e.getId(),
                e.getOrdenTrabajoId(),
                e.getCantidadProducida(),
                e.getCantidadMerma(),
                e.getMotivoMerma(),
                e.getUsuario(),
                e.getObservacion(),
                e.getFecha());
    }

    public RegistroAvanceJpaEntity toJpaEntity(RegistroAvance r) {
        if (r == null) return null;
        RegistroAvanceJpaEntity e = new RegistroAvanceJpaEntity();
        e.setId(r.getId());
        e.setOrdenTrabajoId(r.getOrdenTrabajoId());
        e.setCantidadProducida(r.getCantidadProducida());
        e.setCantidadMerma(r.getCantidadMerma());
        e.setMotivoMerma(r.getMotivoMerma());
        e.setUsuario(r.getUsuario());
        e.setObservacion(r.getObservacion());
        e.setFecha(r.getFecha());
        return e;
    }
}
