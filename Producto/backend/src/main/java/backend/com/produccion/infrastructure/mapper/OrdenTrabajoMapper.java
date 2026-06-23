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
                entity.getItemNVId(),
                entity.getOrdenProduccionId(),
                entity.getNroItem(),
                entity.getTipoOT(),
                entity.getFase(),
                entity.getEstadoOT(),
                entity.getCantidadTotal(),
                entity.getCantidadProducida(),
                entity.getCantidadMerma(),
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
        entity.setItemNVId(domain.getItemNVId());
        entity.setOrdenProduccionId(domain.getOrdenProduccionId());
        entity.setNroItem(domain.getNroItem());
        entity.setTipoOT(domain.getTipoOT());
        entity.setFase(domain.getFase());
        entity.setEstadoOT(domain.getEstadoOT());
        entity.setCantidadTotal(domain.getCantidadTotal());
        entity.setCantidadProducida(domain.getCantidadProducida());
        entity.setCantidadMerma(domain.getCantidadMerma());
        entity.setObservaciones(domain.getObservaciones());

        return entity;
    }
}
