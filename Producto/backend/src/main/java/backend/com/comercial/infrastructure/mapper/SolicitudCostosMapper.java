package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.domain.model.SCOSAccesorio;
import backend.com.comercial.domain.model.SCOSLogotipo;
import backend.com.comercial.domain.model.SCOSTela;
import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.comercial.infrastructure.persistence.entity.SCOSAccesorioJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SCOSLogotipoJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SCOSTelaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SolicitudCostosJpaEntity;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class SolicitudCostosMapper {

    public SolicitudCostos toDomain(SolicitudCostosJpaEntity entity) {
        if (entity == null) return null;

        String clienteNombre = null;
        if (entity.getCliente() != null) {
            clienteNombre = entity.getCliente().getRazonSocial();
        }

        String vendedorNombre = null;
        if (entity.getVendedor() != null && entity.getVendedor().getUsuario() != null) {
            vendedorNombre = entity.getVendedor().getUsuario().getUsuarioNombre();
            if (entity.getVendedor().getUsuario().getUsuarioApellidos() != null) {
                vendedorNombre += " " + entity.getVendedor().getUsuario().getUsuarioApellidos();
            }
        }

        return new SolicitudCostos(
                entity.getIdSCOS(),
                entity.getNumero() != null ? new DocumentNumber(entity.getNumero()) : null,
                entity.getEstado(),
                entity.getTipo(),
                entity.getCliente() != null ? entity.getCliente().getClienteId() : null,
                clienteNombre,
                entity.getVendedor() != null ? entity.getVendedor().getIdVendedor() : null,
                vendedorNombre,
                entity.getArticuloDescripcion(),
                entity.getNombrePrenda(),
                entity.getEsMuestra(),
                entity.getHasLogo(),
                entity.getCantidad(),
                entity.getGenero(),
                entity.getTallaje(),
                entity.getFecha(),
                (entity.getTelas() != null)
                        ? entity.getTelas().stream().map(this::mapTelaToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                (entity.getAccesorios() != null)
                        ? entity.getAccesorios().stream().map(this::mapAccesorioToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                (entity.getLogotipos() != null)
                        ? entity.getLogotipos().stream().map(this::mapLogotipoToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                new ArrayList<>(),  // descripciones se cargan vía DescripcionPlantillaService por idSCOS
                entity.getCostoTotal());
    }

    private SCOSTela mapTelaToDomain(SCOSTelaJpaEntity entity) {
        if (entity == null) return null;
        return new SCOSTela(
                entity.getArticulo() != null ? entity.getArticulo().getIdArticulo() : null,
                entity.getAplicacion(),
                entity.getDescripcion(),
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getProveedorReferencia(),
                entity.getComposicion(),
                entity.getColor(),
                entity.getPeso(),
                entity.getConsumo(),
                entity.getUnidadMedida(),
                new Money(entity.getPrecioUnitario() != null ? entity.getPrecioUnitario() : BigDecimal.ZERO,
                        entity.getMonedaPrecioUnitario() != null ? entity.getMonedaPrecioUnitario() : "CLP"),
                null);
    }

    private SCOSAccesorio mapAccesorioToDomain(SCOSAccesorioJpaEntity entity) {
        if (entity == null) return null;
        return new SCOSAccesorio(
                entity.getArticulo() != null ? entity.getArticulo().getIdArticulo() : null,
                entity.getTipo(),
                entity.getDescripcion(),
                entity.getCantidad(),
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getProveedorReferencia(),
                entity.getConsumo(),
                entity.getUnidadMedida(),
                new Money(entity.getPrecioUnitario() != null ? entity.getPrecioUnitario() : BigDecimal.ZERO,
                        entity.getMonedaPrecioUnitario() != null ? entity.getMonedaPrecioUnitario() : "CLP"),
                null);
    }

    private SCOSLogotipo mapLogotipoToDomain(SCOSLogotipoJpaEntity entity) {
        return new SCOSLogotipo(entity.getId(), entity.getTipo(), entity.getNombre(),
                entity.getUbicacion(), entity.getColor(), entity.getTamano(),
                entity.getCantidad(), entity.getPrecio());
    }

    private SCOSLogotipoJpaEntity toLogotipoEntity(SCOSLogotipo domain) {
        SCOSLogotipoJpaEntity entity = new SCOSLogotipoJpaEntity();
        entity.setTipo(domain.getTipo());
        entity.setNombre(domain.getNombre());
        entity.setUbicacion(domain.getUbicacion());
        entity.setColor(domain.getColor());
        entity.setTamano(domain.getTamano());
        entity.setCantidad(domain.getCantidad());
        entity.setPrecio(domain.getPrecio());
        return entity;
    }

    public SolicitudCostosJpaEntity toEntity(SolicitudCostos domain) {
        if (domain == null) return null;

        SolicitudCostosJpaEntity entity = new SolicitudCostosJpaEntity();
        if (domain.getIdSCOS() != null) {
            entity.setIdSCOS(domain.getIdSCOS());
        }
        entity.setNumero(domain.getNumeroSCOS().getValue());
        entity.setEstado(domain.getEstado());
        entity.setTipo(domain.getTipo());
        entity.setArticuloDescripcion(domain.getArticuloDescripcion());
        entity.setNombrePrenda(domain.getNombrePrenda());
        entity.setEsMuestra(domain.getEsMuestra());
        entity.setHasLogo(domain.getHasLogo());
        entity.setCantidad(domain.getCantidad());
        entity.setGenero(domain.getGenero());
        entity.setTallaje(domain.getTallaje());
        entity.setFecha(domain.getFecha());
        entity.setCostoTotal(domain.getCostoTotal());

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
        domain.getTelas().forEach(t -> {
            SCOSTelaJpaEntity te = new SCOSTelaJpaEntity();
            if (t.getIdArticulo() != null) {
                ArticuloJpaEntity artRef = new ArticuloJpaEntity();
                artRef.setIdArticulo(t.getIdArticulo());
                te.setArticulo(artRef);
            }
            te.setAplicacion(t.getAplicacion());
            te.setDescripcion(t.getDescripcion());
            te.setProveedorReferencia(t.getProveedorReferencia());
            te.setComposicion(t.getComposicion());
            te.setColor(t.getColor());
            te.setPeso(t.getPeso());
            if (t.getProveedorId() != null) {
                ProveedorJpaEntity p = new ProveedorJpaEntity();
                p.setProveedorId(t.getProveedorId());
                te.setProveedor(p);
            }
            te.setConsumo(t.getConsumo());
            te.setUnidadMedida(t.getUnidadMedida());
            if (t.getPrecioUnitario() != null) {
                te.setPrecioUnitario(t.getPrecioUnitario().getAmount());
                te.setMonedaPrecioUnitario(t.getPrecioUnitario().getCurrency());
            }
            if (t.getPrecioUnitario() != null && t.getConsumo() != null) {
                te.setCostoTotal(t.getPrecioUnitario().getAmount().multiply(t.getConsumo()));
                te.setMonedaCostoTotal(t.getPrecioUnitario().getCurrency());
            }
            entity.addTela(te);
        });

        domain.getLogotipos().forEach(l -> entity.addLogotipo(toLogotipoEntity(l)));

        domain.getAccesorios().forEach(a -> {
            SCOSAccesorioJpaEntity ae = new SCOSAccesorioJpaEntity();
            if (a.getIdArticulo() != null) {
                ArticuloJpaEntity artRef = new ArticuloJpaEntity();
                artRef.setIdArticulo(a.getIdArticulo());
                ae.setArticulo(artRef);
            }
            ae.setTipo(a.getTipo());
            ae.setDescripcion(a.getDescripcion());
            ae.setCantidad(a.getCantidad());
            ae.setProveedorReferencia(a.getProveedorReferencia());
            ae.setConsumo(a.getConsumo());
            ae.setUnidadMedida(a.getUnidadMedida());
            if (a.getPrecioUnitario() != null) {
                ae.setPrecioUnitario(a.getPrecioUnitario().getAmount());
                ae.setMonedaPrecioUnitario(a.getPrecioUnitario().getCurrency());
            }
            entity.addAccesorio(ae);
        });

        return entity;
    }
}
