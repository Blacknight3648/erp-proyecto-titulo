package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.domain.model.*;
import backend.com.comercial.infrastructure.persistence.entity.*;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.EspecificacionTecnica;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class SolicitudCotizacionMapper {

    public SolicitudCotizacion toDomain(SolicitudCotizacionJpaEntity entity) {
        if (entity == null)
            return null;

        return new SolicitudCotizacion(
                entity.getIdSCOT(),
                new DocumentNumber(entity.getNumero()),
                entity.getEstado(),
                entity.getTipo(),
                entity.getCliente() != null ? entity.getCliente().getClienteId() : null,
                entity.getVendedor() != null ? entity.getVendedor().getIdVendedor() : null,
                entity.getEspecificacionTecnica() != null
                        ? entity.getEspecificacionTecnica().getEspecificacionTecnicaId()
                        : null,
                entity.getArticuloDescripcion(),
                entity.getEsMuestra(),
                entity.getHasLogo(),
                entity.getCantidad(),
                entity.getFecha(),
                (entity.getPrendas() != null)
                        ? entity.getPrendas().stream().map(this::mapPrendaToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                (entity.getLogotipos() != null)
                        ? entity.getLogotipos().stream().map(this::mapLogotipoToDomain).collect(Collectors.toList())
                        : new ArrayList<>());
    }

    private SCOSLogotipo mapLogotipoToDomain(SCOSLogotipoJpaEntity entity) {
        return new SCOSLogotipo(entity.getId(), entity.getTipo(), entity.getNombre(),
                entity.getUbicacion(), entity.getColor(), entity.getTamano(),
                entity.getCantidad(), entity.getPrecio());
    }

    private SCOTPrendaLista mapPrendaToDomain(SCOTPrendaListaJpaEntity entity) {
        if (entity == null)
            return null;
        return new SCOTPrendaLista(
                entity.getIdSCOTPrendaLista(),
                entity.getNombre(),
                entity.getCantidad(),
                entity.getTalla(),
                entity.getColor(),
                entity.getProveedorReferencia(),
                entity.getLinkReferencia(),
                entity.getComposicion(),
                entity.getPeso(),
                entity.getObservaciones(),
                new Money(entity.getPrecioUnitario() != null ? entity.getPrecioUnitario() : BigDecimal.ZERO,
                        entity.getMonedaPrecioUnitario() != null ? entity.getMonedaPrecioUnitario() : "CLP"));
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

    private SCOTPrendaListaJpaEntity toPrendaEntity(SCOTPrendaLista domain) {
        SCOTPrendaListaJpaEntity entity = new SCOTPrendaListaJpaEntity();
        if (domain.getSCOTPrendaListaId() != null) {
            entity.setIdSCOTPrendaLista(domain.getSCOTPrendaListaId());
        }
        entity.setNombre(domain.getNombre());
        entity.setCantidad(domain.getCantidad());
        entity.setTalla(domain.getTalla());
        entity.setColor(domain.getColor());
        entity.setProveedorReferencia(domain.getProveedorReferencia());
        entity.setLinkReferencia(domain.getLinkReferencia());
        entity.setComposicion(domain.getComposicion());
        entity.setPeso(domain.getPeso());
        entity.setObservaciones(domain.getObservaciones());

        if (domain.getPrecioUnitario() != null) {
            entity.setPrecioUnitario(domain.getPrecioUnitario().getAmount());
            entity.setMonedaPrecioUnitario(domain.getPrecioUnitario().getCurrency());
        } else {
            entity.setPrecioUnitario(BigDecimal.ZERO);
            entity.setMonedaPrecioUnitario("CLP");
        }
        return entity;
    }

    public SolicitudCotizacionJpaEntity toEntity(SolicitudCotizacion domain) {
        if (domain == null)
            return null;

        SolicitudCotizacionJpaEntity entity = new SolicitudCotizacionJpaEntity();
        if (domain.getIdSCOT() != null) {
            entity.setIdSCOT(domain.getIdSCOT());
        }
        entity.setNumero(domain.getNumeroSCOT().getValue());
        entity.setEstado(domain.getEstado());
        entity.setTipo(domain.getTipo());
        entity.setArticuloDescripcion(domain.getArticuloDescripcion());
        entity.setEsMuestra(domain.getEsMuestra());
        entity.setHasLogo(domain.getHasLogo());
        entity.setCantidad(domain.getCantidad());
        entity.setFecha(domain.getFecha());

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
        if (domain.getEspecificacionTecnicaId() != null) {
            EspecificacionTecnica et = new EspecificacionTecnica();
            et.setEspecificacionTecnicaId(domain.getEspecificacionTecnicaId());
            entity.setEspecificacionTecnica(et);
        }

        domain.getPrendas().forEach(prod -> entity.addPrenda(toPrendaEntity(prod)));
        domain.getLogotipos().forEach(l -> entity.addLogotipo(toLogotipoEntity(l)));

        return entity;
    }
}
