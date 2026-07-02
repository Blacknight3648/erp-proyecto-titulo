package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.domain.enums.EstadoNV;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.ItemNVTalla;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.infrastructure.persistence.entity.NotaVentaItemJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.NotaVentaItemTallaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.NotaVentaJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Component
public class NotaVentaMapper {

    public NotaVenta toDomain(NotaVentaJpaEntity entity) {
        if (entity == null)
            return null;

        return new NotaVenta(
                entity.getIdNV(),
                entity.getNumeroNV() != null ? new DocumentNumber(entity.getNumeroNV()) : null,
                entity.getEvaluacionNegocioId(),
                entity.getCliente() != null ? entity.getCliente().getClienteId() : null,
                entity.getVendedor() != null ? entity.getVendedor().getIdVendedor() : null,
                EstadoNV.valueOf(entity.getEstado()),
                entity.getEsKit(),
                entity.getDetalleKit(),
                entity.getFechaEmision(),
                entity.getFechaEntregaEstimada(),
                safeMoneyFromEntity(entity.getMontoSubtotal(), entity.getMonedaSubtotal()),
                safeMoneyFromEntity(entity.getMontoIva(), entity.getMonedaIva()),
                safeMoneyFromEntity(entity.getMontoTotal(), entity.getMonedaTotal()),
                entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList()));
    }

    private ItemNV toItemDomain(NotaVentaItemJpaEntity entity) {
        if (entity == null)
            return null;
        ItemNV item = new ItemNV(
                entity.getIdItemNV(),
                entity.getNroItem(),
                entity.getArticulo() != null ? entity.getArticulo().getIdArticulo() : null,
                entity.getArticulo() != null ? entity.getArticulo().getNombreArticulo() : "Prenda",
                entity.getModelo(),
                entity.getTela(),
                entity.getComposicion(),
                entity.getColor(),
                entity.getTalla(),
                entity.getGenero(),
                entity.getCodigo(),
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getProveedor() != null ? entity.getProveedor().getRazonSocialProveedor() : "PENDIENTE",
                entity.getLlevaLogo(),
                entity.getTipoItem(),
                entity.getRequiereOt(),
                entity.getDetalleOt(),
                entity.getLogoDetalle(),
                entity.getCantidad(),
                safeMoneyFromEntity(entity.getPrecioUnitario(), entity.getMonedaPrecioUnitario()),
                entity.getTallas().stream().map(this::toTallaDomain).collect(Collectors.toList()));
        if (entity.getOpId() != null) {
            item.vincularOp(entity.getOpId());
        }
        return item;
    }

    private ItemNVTalla toTallaDomain(NotaVentaItemTallaJpaEntity entity) {
        if (entity == null)
            return null;
        return new ItemNVTalla(entity.getTalla(), entity.getCantidad());
    }

    private Money safeMoneyFromEntity(BigDecimal amount, String currency) {
        if (amount == null || currency == null || currency.isBlank()) {
            return Money.zero("CLP");
        }
        return new Money(amount, currency);
    }

    public NotaVentaJpaEntity toEntity(NotaVenta domain) {
        if (domain == null)
            return null;

        NotaVentaJpaEntity entity = new NotaVentaJpaEntity();
        if (domain.getIdNV() != null) {
            entity.setIdNV(domain.getIdNV());
        }
        if (domain.getNumeroNV() != null) {
            entity.setNumeroNV(domain.getNumeroNV().getValue());
        }
        entity.setEvaluacionNegocioId(domain.getEvaluacionNegocioId());
        entity.setEstado(domain.getEstado().name());
        entity.setEsKit(domain.getEsKit());
        entity.setDetalleKit(domain.getDetalleKit());
        entity.setFechaEmision(domain.getFechaEmision());
        entity.setFechaEntregaEstimada(domain.getFechaEntregaEstimada());

        if (domain.getClienteId() != null) {
            ClienteJpaEntity c = new ClienteJpaEntity();
            c.setClienteId(domain.getClienteId());
            entity.setCliente(c);
        }
        if (domain.getVendedorId() != null) {
            VendedorJpaEntity v = new VendedorJpaEntity();
            v.setIdVendedor(domain.getVendedorId());
            entity.setVendedor(v);
        }

        if (domain.getMontoSubtotal() != null) {
            entity.setMontoSubtotal(domain.getMontoSubtotal().getAmount());
            entity.setMonedaSubtotal(domain.getMontoSubtotal().getCurrency());
        }
        if (domain.getMontoIva() != null) {
            entity.setMontoIva(domain.getMontoIva().getAmount());
            entity.setMonedaIva(domain.getMontoIva().getCurrency());
        }
        if (domain.getMontoTotal() != null) {
            entity.setMontoTotal(domain.getMontoTotal().getAmount());
            entity.setMonedaTotal(domain.getMontoTotal().getCurrency());
        }

        domain.getItems().forEach(itemDomain -> {
            NotaVentaItemJpaEntity itemEntity = new NotaVentaItemJpaEntity();
            if (itemDomain.getIdItemNV() != null) {
                itemEntity.setIdItemNV(itemDomain.getIdItemNV());
            }
            itemEntity.setNroItem(itemDomain.getNroItem());
            if (itemDomain.getArticuloId() != null) {
                ArticuloJpaEntity a = new ArticuloJpaEntity();
                a.setIdArticulo(itemDomain.getArticuloId());
                itemEntity.setArticulo(a);
            }
            itemEntity.setModelo(itemDomain.getModelo());
            itemEntity.setTela(itemDomain.getTela());
            itemEntity.setComposicion(itemDomain.getComposicion());
            itemEntity.setColor(itemDomain.getColor());
            itemEntity.setTalla(itemDomain.getTalla());
            itemEntity.setGenero(itemDomain.getGenero());
            itemEntity.setCodigo(itemDomain.getCodigo());
            if (itemDomain.getProveedorId() != null) {
                ProveedorJpaEntity prov = new ProveedorJpaEntity();
                prov.setProveedorId(itemDomain.getProveedorId());
                itemEntity.setProveedor(prov);
            }
            itemEntity.setOpId(itemDomain.getOpId());
            itemEntity.setLlevaLogo(itemDomain.getLlevaLogo());
            itemEntity.setTipoItem(itemDomain.getTipoItem());
            itemEntity.setRequiereOt(itemDomain.getRequiereOt());
            itemEntity.setDetalleOt(itemDomain.getDetalleOt());
            itemEntity.setLogoDetalle(itemDomain.getLogoDetalle());
            itemEntity.setCantidad(itemDomain.getCantidad());

            if (itemDomain.getPrecioUnitario() != null) {
                itemEntity.setPrecioUnitario(itemDomain.getPrecioUnitario().getAmount());
                itemEntity.setMonedaPrecioUnitario(itemDomain.getPrecioUnitario().getCurrency());
            }
            if (itemDomain.getTotal() != null) {
                itemEntity.setTotal(itemDomain.getTotal().getAmount());
                itemEntity.setMonedaTotal(itemDomain.getTotal().getCurrency());
            }

            itemDomain.getTallas().forEach(tallaDomain -> {
                NotaVentaItemTallaJpaEntity te = new NotaVentaItemTallaJpaEntity();
                te.setTalla(tallaDomain.getTalla());
                te.setCantidad(tallaDomain.getCantidad());
                itemEntity.addTalla(te);
            });

            entity.addItem(itemEntity);
        });

        return entity;
    }
}
