package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class HojaCompraMapper {

    @PersistenceContext
    private EntityManager em;

    public HojaCompra toDomain(HojaCompraJpaEntity entity) {
        if (entity == null) return null;

        List<HojaCompraItem> items = entity.getItems() != null
                ? entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new HojaCompra(
                entity.getIdHC(),
                entity.getNumeroHC() != null ? new DocumentNumber(entity.getNumeroHC()) : null,
                entity.getOrdenProduccion() != null ? entity.getOrdenProduccion().getIdOP() : null,
                entity.getCosteoVersion() != null ? entity.getCosteoVersion().getIdCosteoVersion() : null,
                entity.getEstado(),
                entity.getFechaGeneracion(),
                entity.getObservaciones(),
                items);
    }

    public HojaCompraJpaEntity toJpaEntity(HojaCompra domain) {
        if (domain == null) return null;

        HojaCompraJpaEntity entity = new HojaCompraJpaEntity();
        entity.setIdHC(domain.getIdHC());
        if (domain.getNumeroHC() != null) {
            entity.setNumeroHC(domain.getNumeroHC().getValue());
        }
        if (domain.getOpId() != null) {
            entity.setOrdenProduccion(em.getReference(OrdenProduccionJpaEntity.class, domain.getOpId()));
        }
        if (domain.getCosteoVersionId() != null) {
            entity.setCosteoVersion(em.getReference(CosteoVersionJpaEntity.class, domain.getCosteoVersionId()));
        }
        entity.setEstado(domain.getEstado());
        entity.setFechaGeneracion(domain.getFechaGeneracion());
        entity.setObservaciones(domain.getObservaciones());

        if (domain.getItems() != null) {
            List<HojaCompraItemJpaEntity> itemsEntity = domain.getItems().stream()
                    .map(item -> {
                        HojaCompraItemJpaEntity itemEntity = itemToJpaEntity(item);
                        itemEntity.setHojaCompra(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemsEntity);
        }

        return entity;
    }

    private HojaCompraItem itemToDomain(HojaCompraItemJpaEntity entity) {
        if (entity == null) return null;

        Long proveedorId = entity.getProveedorId();
        String proveedorNombre = entity.getProveedor() != null ? entity.getProveedor().getRazonSocialProveedor() : null;

        Long ocId = null;
        String numeroOC = null;
        if (entity.getOcLinks() != null && !entity.getOcLinks().isEmpty()) {
            backend.com.produccion.infrastructure.persistence.entity.HCItemOCItemLinkJpaEntity link = entity.getOcLinks().get(0);
            if (link.getOcItem() != null && link.getOcItem().getOrdenCompra() != null) {
                ocId = link.getOcItem().getOrdenCompra().getIdOC();
                numeroOC = link.getOcItem().getOrdenCompra().getNumeroOC();
            }
        }

        return new HojaCompraItem(
                entity.getIdHCItem(),
                entity.getHojaCompra() != null ? entity.getHojaCompra().getIdHC() : null,
                entity.getTipoInsumo(),
                entity.getArticuloId(),
                entity.getNombreInsumo(),
                entity.getConsumoUnitario(),
                entity.getCantidadOP(),
                entity.getCantidadRequerida(),
                entity.getPrecioUnitarioRef(),
                proveedorId,
                proveedorNombre,
                ocId,
                numeroOC);
    }

    private HojaCompraItemJpaEntity itemToJpaEntity(HojaCompraItem domain) {
        if (domain == null) return null;
        HojaCompraItemJpaEntity entity = new HojaCompraItemJpaEntity();
        entity.setIdHCItem(domain.getIdHCItem());
        entity.setTipoInsumo(domain.getTipoInsumo());
        entity.setArticuloId(domain.getArticuloId());
        entity.setNombreInsumo(domain.getNombreInsumo());
        entity.setConsumoUnitario(domain.getConsumoUnitario());
        entity.setCantidadOP(domain.getCantidadOP());
        entity.setCantidadRequerida(domain.getCantidadRequerida());
        entity.setPrecioUnitarioRef(domain.getPrecioUnitarioRef());
        entity.setProveedorId(domain.getProveedorId());
        return entity;
    }
}
