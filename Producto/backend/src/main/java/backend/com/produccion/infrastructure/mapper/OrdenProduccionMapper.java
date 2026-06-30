package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.stream.Collectors;

@Component
public class OrdenProduccionMapper {

    @PersistenceContext
    private EntityManager em;

    private final SeguimientoOPMapper seguimientoOPMapper;

    public OrdenProduccionMapper(SeguimientoOPMapper seguimientoOPMapper) {
        this.seguimientoOPMapper = seguimientoOPMapper;
    }

    public OrdenProduccion toDomain(OrdenProduccionJpaEntity entity) {
        if (entity == null)
            return null;

        OrdenProduccion op = new OrdenProduccion(
                entity.getIdOP(),
                entity.getCosteoVersion() != null ? entity.getCosteoVersion().getIdCosteoVersion()
                        : null,
                entity.getNumeroOP() != null ? new DocumentNumber(entity.getNumeroOP()) : null,
                entity.getNotaVentaId(),
                entity.getEstado(),
                entity.getFechaInicio(),
                entity.getFechaEntregaProgramada(),
                entity.getObservaciones(),
                entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList()));
                
        op.setSeguimiento(seguimientoOPMapper.toDomain(entity.getSeguimiento()));
        return op;
    }

    public OrdenProduccionJpaEntity toJpaEntity(OrdenProduccion domain) {
        if (domain == null)
            return null;

        OrdenProduccionJpaEntity entity = new OrdenProduccionJpaEntity();
        entity.setIdOP(domain.getIdOP());
        if (domain.getCosteoVersionId() != null) {
            entity.setCosteoVersion(em.getReference(CosteoVersionJpaEntity.class, domain.getCosteoVersionId()));
        }
        if (domain.getNumeroOP() != null) {
            entity.setNumeroOP(domain.getNumeroOP().getValue());
        }
        entity.setNotaVentaId(domain.getNotaVentaId());
        entity.setEstado(domain.getEstado());
        entity.setFechaInicio(domain.getFechaInicio());
        entity.setFechaEntregaProgramada(domain.getFechaEntregaProgramada());
        entity.setObservaciones(domain.getObservaciones());

        if (domain.getItems() != null) {
            entity.setItems(domain.getItems().stream()
                    .map(item -> itemToJpaEntity(item, entity))
                    .collect(Collectors.toList()));
        }
        
        if (domain.getSeguimiento() != null) {
            entity.setSeguimiento(seguimientoOPMapper.toJpaEntity(domain.getSeguimiento(), entity));
        }

        return entity;
    }

    private OrdenProduccionItem itemToDomain(OrdenProduccionItemJpaEntity entity) {
        return new OrdenProduccionItem(
                entity.getIdOPItem(),
                entity.getArticuloId(),
                entity.getNroItem(),
                entity.getModelo(),
                entity.getTela(),
                entity.getComposicion(),
                entity.getColor(),
                entity.getTalla(),
                entity.getGenero(),
                entity.getCodigo(),
                entity.getLlevaLogo(),
                entity.getCantidad());
    }

    private OrdenProduccionItemJpaEntity itemToJpaEntity(OrdenProduccionItem domain,
            OrdenProduccionJpaEntity opEntity) {
        OrdenProduccionItemJpaEntity entity = new OrdenProduccionItemJpaEntity();
        entity.setIdOPItem(domain.getIdOPItem());
        entity.setOrdenProduccion(opEntity);
        entity.setArticuloId(domain.getArticuloId());
        entity.setNroItem(domain.getNroItem());
        entity.setModelo(domain.getModelo());
        entity.setTela(domain.getTela());
        entity.setComposicion(domain.getComposicion());
        entity.setColor(domain.getColor());
        entity.setTalla(domain.getTalla());
        entity.setGenero(domain.getGenero());
        entity.setCodigo(domain.getCodigo());
        entity.setLlevaLogo(domain.getLlevaLogo());
        entity.setCantidad(domain.getCantidad());
        return entity;
    }

}
