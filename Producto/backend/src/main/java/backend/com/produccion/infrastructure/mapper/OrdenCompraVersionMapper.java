package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.OrdenCompraItemVersion;
import backend.com.produccion.domain.model.OrdenCompraVersion;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraItemVersionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraVersionJpaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenCompraVersionMapper {

    @PersistenceContext
    private EntityManager em;

    public OrdenCompraVersion toDomain(OrdenCompraVersionJpaEntity entity) {
        if (entity == null)
            return null;

        List<OrdenCompraItemVersion> items = entity.getItems() != null
                ? entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new OrdenCompraVersion(
                entity.getIdOCVersion(),
                entity.getOrdenCompra() != null ? entity.getOrdenCompra().getIdOC() : null,
                entity.getNumeroVersion(),
                entity.getFechaCreacion(),
                entity.getMotivoCambio(),
                entity.getUsuarioCreador(),
                items,
                entity.getProveedorId(),
                entity.getFechaEntregaEstimada(),
                entity.getObservaciones(),
                entity.getTotalNeto());
    }

    public OrdenCompraVersionJpaEntity toJpaEntity(OrdenCompraVersion domain) {
        if (domain == null)
            return null;

        OrdenCompraVersionJpaEntity entity = new OrdenCompraVersionJpaEntity();
        entity.setIdOCVersion(domain.getIdOCVersion());
        if (domain.getOcId() != null) {
            entity.setOrdenCompra(em.getReference(OrdenCompraJpaEntity.class, domain.getOcId()));
        }
        entity.setNumeroVersion(domain.getNumeroVersion());
        entity.setFechaCreacion(domain.getFechaCreacion());
        entity.setMotivoCambio(domain.getMotivoCambio());
        entity.setUsuarioCreador(domain.getUsuarioCreador());
        entity.setProveedorId(domain.getProveedorId());
        entity.setFechaEntregaEstimada(domain.getFechaEntregaEstimada());
        entity.setObservaciones(domain.getObservaciones());
        entity.setTotalNeto(domain.getTotalNeto());

        if (domain.getItems() != null) {
            List<OrdenCompraItemVersionJpaEntity> itemsEntity = domain.getItems().stream()
                    .map(item -> {
                        OrdenCompraItemVersionJpaEntity itemEntity = itemToJpaEntity(item);
                        itemEntity.setOcVersion(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemsEntity);
        }

        return entity;
    }

    private OrdenCompraItemVersion itemToDomain(OrdenCompraItemVersionJpaEntity entity) {
        return new OrdenCompraItemVersion(
                entity.getIdOCItemVersion(),
                entity.getOcVersion() != null ? entity.getOcVersion().getIdOCVersion() : null,
                entity.getOcItemId(),
                entity.getTipoInsumo(),
                entity.getArticuloId(),
                entity.getNombreInsumo(),
                entity.getCantidadComprada(),
                entity.getPrecioUnitario(),
                entity.getSubtotal());
    }

    private OrdenCompraItemVersionJpaEntity itemToJpaEntity(OrdenCompraItemVersion domain) {
        OrdenCompraItemVersionJpaEntity entity = new OrdenCompraItemVersionJpaEntity();
        entity.setIdOCItemVersion(domain.getIdOCItemVersion());
        entity.setOcItemId(domain.getOcItemId());
        entity.setTipoInsumo(domain.getTipoInsumo());
        entity.setArticuloId(domain.getArticuloId());
        entity.setNombreInsumo(domain.getNombreInsumo());
        entity.setCantidadComprada(domain.getCantidadComprada());
        entity.setPrecioUnitario(domain.getPrecioUnitario());
        entity.setSubtotal(domain.getSubtotal());
        return entity;
    }
}
