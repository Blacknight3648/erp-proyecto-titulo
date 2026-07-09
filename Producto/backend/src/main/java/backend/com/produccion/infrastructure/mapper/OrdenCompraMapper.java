package backend.com.produccion.infrastructure.mapper;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.produccion.domain.model.HCItemOCItemLink;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenCompraItem;
import backend.com.produccion.infrastructure.persistence.entity.HCItemOCItemLinkJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.HojaCompraItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenCompraJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenCompraMapper {

    @PersistenceContext
    private EntityManager em;

    public OrdenCompra toDomain(OrdenCompraJpaEntity entity) {
        if (entity == null) return null;

        List<OrdenCompraItem> items = entity.getItems() != null
                ? entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new OrdenCompra(
                entity.getIdOC(),
                entity.getNumeroOC() != null ? new DocumentNumber(entity.getNumeroOC()) : null,
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getEstado(),
                entity.getFechaEmision(),
                entity.getFechaEntregaEstimada(),
                entity.getObservaciones(),
                entity.getTotalNeto(),
                items,
                entity.getMotivoRechazo(),
                entity.getVersion());
    }

    public OrdenCompraJpaEntity toJpaEntity(OrdenCompra domain) {
        if (domain == null) return null;

        OrdenCompraJpaEntity entity = new OrdenCompraJpaEntity();
        entity.setIdOC(domain.getIdOC());
        if (domain.getNumeroOC() != null) {
            entity.setNumeroOC(domain.getNumeroOC().getValue());
        }
        if (domain.getProveedorId() != null) {
            entity.setProveedor(em.getReference(ProveedorJpaEntity.class, domain.getProveedorId()));
        }
        entity.setEstado(domain.getEstado());
        entity.setFechaEmision(domain.getFechaEmision());
        entity.setFechaEntregaEstimada(domain.getFechaEntregaEstimada());
        entity.setObservaciones(domain.getObservaciones());
        entity.setTotalNeto(domain.getTotalNeto());
        entity.setMotivoRechazo(domain.getMotivoRechazo());
        entity.setVersion(domain.getVersion());

        if (domain.getItems() != null) {
            List<OrdenCompraItemJpaEntity> itemsEntity = domain.getItems().stream()
                    .map(item -> {
                        OrdenCompraItemJpaEntity itemEntity = itemToJpaEntity(item);
                        itemEntity.setOrdenCompra(entity);
                        return itemEntity;
                    })
                    .collect(Collectors.toList());
            entity.setItems(itemsEntity);
        }

        return entity;
    }

    private OrdenCompraItem itemToDomain(OrdenCompraItemJpaEntity entity) {
        List<HCItemOCItemLink> links = entity.getHcLinks() != null
                ? entity.getHcLinks().stream().map(this::linkToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new OrdenCompraItem(
                entity.getIdOCItem(),
                entity.getOrdenCompra() != null ? entity.getOrdenCompra().getIdOC() : null,
                entity.getTipoInsumo(),
                entity.getArticuloId(),
                entity.getNombreInsumo(),
                entity.getCantidadRequerida(),
                entity.getCantidadStock(),
                entity.getCantidadComprada(),
                entity.getPrecioUnitario(),
                entity.getSubtotal(),
                links);
    }

    private OrdenCompraItemJpaEntity itemToJpaEntity(OrdenCompraItem domain) {
        OrdenCompraItemJpaEntity entity = new OrdenCompraItemJpaEntity();
        entity.setIdOCItem(domain.getIdOCItem());
        entity.setTipoInsumo(domain.getTipoInsumo());
        entity.setArticuloId(domain.getArticuloId());
        entity.setNombreInsumo(domain.getNombreInsumo());
        entity.setCantidadRequerida(domain.getCantidadRequerida());
        entity.setCantidadStock(domain.getCantidadStock());
        entity.setCantidadComprada(domain.getCantidadComprada());
        entity.setPrecioUnitario(domain.getPrecioUnitario());
        entity.setSubtotal(domain.getSubtotal());

        if (domain.getHcLinks() != null) {
            List<HCItemOCItemLinkJpaEntity> linksEntity = domain.getHcLinks().stream()
                    .map(link -> {
                        HCItemOCItemLinkJpaEntity le = linkToJpaEntity(link);
                        le.setOcItem(entity);
                        return le;
                    })
                    .collect(Collectors.toList());
            entity.setHcLinks(linksEntity);
        }

        return entity;
    }

    private HCItemOCItemLink linkToDomain(HCItemOCItemLinkJpaEntity entity) {
        return new HCItemOCItemLink(
                entity.getHcItem() != null ? entity.getHcItem().getIdHCItem() : null,
                entity.getOcItem() != null ? entity.getOcItem().getIdOCItem() : null,
                entity.getCantidadAsignada());
    }

    private HCItemOCItemLinkJpaEntity linkToJpaEntity(HCItemOCItemLink domain) {
        HCItemOCItemLinkJpaEntity entity = new HCItemOCItemLinkJpaEntity();
        if (domain.getHcItemId() != null) {
            entity.setHcItem(em.getReference(HojaCompraItemJpaEntity.class, domain.getHcItemId()));
        }
        entity.setCantidadAsignada(domain.getCantidadAsignada());
        return entity;
    }
}
