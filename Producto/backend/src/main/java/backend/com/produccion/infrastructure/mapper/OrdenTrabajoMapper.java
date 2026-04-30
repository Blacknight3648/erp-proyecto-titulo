package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.infrastructure.persistence.entity.OrdenTrabajoJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import org.springframework.stereotype.Component;

@Component
public class OrdenTrabajoMapper {

    public OrdenTrabajo toDomain(OrdenTrabajoJpaEntity entity) {
        if (entity == null)
            return null;

        return new OrdenTrabajo(
                entity.getIdOT(),
                entity.getNumeroOT() != null ? new DocumentNumber(entity.getNumeroOT()) : null,
                entity.getNotaVentaId(),
                entity.getNroItem(),
                entity.getTipoOT(),
                entity.getEstadoOT(),
                entity.getObservaciones());
    }

    public OrdenTrabajoJpaEntity toJpaEntity(OrdenTrabajo domain) {
        if (domain == null)
            return null;

        OrdenTrabajoJpaEntity entity = new OrdenTrabajoJpaEntity();
        entity.setIdOT(domain.getIdOT());
        if (domain.getNumeroOT() != null) {
            entity.setNumeroOT(domain.getNumeroOT().getValue());
        }
        entity.setNotaVentaId(domain.getNotaVentaId());
        entity.setNroItem(domain.getNroItem());
        entity.setTipoOT(domain.getTipoOT());
        entity.setEstadoOT(domain.getEstadoOT());
        entity.setObservaciones(domain.getObservaciones());

        return entity;
    }
}
