package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.RecepcionOC;
import backend.com.produccion.domain.model.RecepcionOCItem;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.RecepcionOCItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.RecepcionOCJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecepcionOCMapper {

    @PersistenceContext
    private EntityManager em;

    public RecepcionOC toDomain(RecepcionOCJpaEntity entity) {
        if (entity == null) return null;

        List<RecepcionOCItem> items = entity.getItems() != null
                ? entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new RecepcionOC(
                entity.getIdRecepcion(),
                entity.getOrdenCompra() != null ? entity.getOrdenCompra().getIdOC() : null,
                entity.getFechaRecepcion(),
                entity.getNumeroGuia(),
                entity.getResponsable(),
                entity.getObservaciones(),
                items);
    }

    public RecepcionOCJpaEntity toJpaEntity(RecepcionOC domain) {
        if (domain == null) return null;

        RecepcionOCJpaEntity entity = new RecepcionOCJpaEntity();
        entity.setIdRecepcion(domain.getIdRecepcion());
        if (domain.getOcId() != null) {
            entity.setOrdenCompra(em.getReference(OrdenCompraJpaEntity.class, domain.getOcId()));
        }
        entity.setFechaRecepcion(domain.getFechaRecepcion());
        entity.setNumeroGuia(domain.getNumeroGuia());
        entity.setResponsable(domain.getResponsable());
        entity.setObservaciones(domain.getObservaciones());

        if (domain.getItems() != null) {
            List<RecepcionOCItemJpaEntity> itemsEntity = domain.getItems().stream()
                    .map(item -> {
                        RecepcionOCItemJpaEntity ie = itemToJpaEntity(item);
                        ie.setRecepcion(entity);
                        return ie;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemsEntity);
        }

        return entity;
    }

    private RecepcionOCItem itemToDomain(RecepcionOCItemJpaEntity entity) {
        return new RecepcionOCItem(
                entity.getIdRecepcionItem(),
                entity.getRecepcion() != null ? entity.getRecepcion().getIdRecepcion() : null,
                entity.getOcItem() != null ? entity.getOcItem().getIdOCItem() : null,
                entity.getCantidadRecibida(),
                entity.getCantidadConforme(),
                entity.getCantidadRechazada(),
                entity.getMotivoRechazo());
    }

    private RecepcionOCItemJpaEntity itemToJpaEntity(RecepcionOCItem domain) {
        RecepcionOCItemJpaEntity entity = new RecepcionOCItemJpaEntity();
        entity.setIdRecepcionItem(domain.getIdRecepcionItem());
        if (domain.getOcItemId() != null) {
            entity.setOcItem(em.getReference(OrdenCompraItemJpaEntity.class, domain.getOcItemId()));
        }
        entity.setCantidadRecibida(domain.getCantidadRecibida());
        entity.setCantidadConforme(domain.getCantidadConforme());
        entity.setCantidadRechazada(domain.getCantidadRechazada());
        entity.setMotivoRechazo(domain.getMotivoRechazo());
        return entity;
    }
}
