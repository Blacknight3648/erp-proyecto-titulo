package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.GastoAdicional;
import backend.com.comercial.domain.model.GastoAdicionalDetalle;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.model.ItemEspecificacion;
import backend.com.comercial.domain.model.TomaTallaje;
import backend.com.comercial.domain.model.TomaTallajeDetalle;
import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioItemJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioItemSpecJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.GastoAdicionalJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.GastoAdicionalDetalleJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.TomaTallajeJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.TomaTallajeDetalleJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EvaluacionNegocioMapper {

    public EvaluacionNegocio toDomain(EvaluacionNegocioJpaEntity entity) {
        if (entity == null) return null;

        TomaTallaje tomaTallaje = null;
        if (entity.getTomaTallaje() != null) {
            TomaTallajeJpaEntity tt = entity.getTomaTallaje();
            List<TomaTallajeDetalle> detalles = tt.getDetalles().stream()
                    .map(d -> new TomaTallajeDetalle(d.getIdentificadorPersona(), d.getTalla(), d.getCantidad()))
                    .collect(Collectors.toList());
            tomaTallaje = new TomaTallaje(
                    tt.getObservaciones(),
                    tt.getFechaProgramada(),
                    tt.getDiasXRecinto(),
                    tt.getPersXRecinto(),
                    tt.getColaccionXPers(),
                    tt.getAsignacionXPers(),
                    tt.getPeajes(),
                    tt.getBencinaXLt(),
                    tt.getKmTotal(),
                    tt.getRendKmLt(),
                    tt.getRecintos(),
                    detalles);
        }

        List<GastoAdicional> gastosAdicionales = entity.getGastosAdicionales().stream()
                .map(gasto -> new GastoAdicional(
                        GastoAdicional.TipoGastoAdicional.valueOf(gasto.getTipoGasto()),
                        new Money(gasto.getMonto(), gasto.getMoneda()),
                        gasto.getDetalles().stream()
                                .map(d -> new GastoAdicionalDetalle(d.getClave(), d.getValor()))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());

        String vNombre = null;
        if (entity.getVendedor() != null && entity.getVendedor().getUsuario() != null) {
            vNombre = entity.getVendedor().getUsuario().getUsuarioNombre() + " " +
                    entity.getVendedor().getUsuario().getUsuarioApellidos();
        }

        return new EvaluacionNegocio(
                entity.getIdEVN(),
                entity.getNumero() != null ? new DocumentNumber(entity.getNumero()) : null,
                entity.getCliente() != null ? entity.getCliente().getClienteId() : null,
                entity.getVendedor() != null ? entity.getVendedor().getIdVendedor() : null,
                EstadoEVN.valueOf(entity.getEstado()),
                entity.getFechaEvaluacion(),
                tomaTallaje,
                gastosAdicionales,
                entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList()),
                entity.getPorcentajeComision(),
                entity.getClienteNombre(),
                entity.getReferencia(),
                vNombre);
    }

    private ItemEVN toItemDomain(EvaluacionNegocioItemJpaEntity entity) {
        if (entity == null) return null;

        List<ItemEspecificacion> specs = entity.getSpecs() == null
                ? java.util.Collections.emptyList()
                : entity.getSpecs().stream()
                        .map(s -> new ItemEspecificacion(s.getClave(), s.getValor()))
                        .collect(Collectors.toList());

        return new ItemEVN(
                entity.getArticulo() != null ? entity.getArticulo().getIdArticulo() : null,
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getNroItem(),
                entity.getDescripcion(),
                entity.getModelo(),
                entity.getTela(),
                entity.getComposicion(),
                entity.getGenero(),
                entity.getCodigoInterno(),
                entity.getCodigoProveedor(),
                entity.getProveedorNombre(),
                entity.getColor(),
                entity.getCantidad(),
                new Money(entity.getPrecioUnitario(), entity.getMonedaPrecioUnitario()),
                new Money(entity.getCostoUnitario(), entity.getMonedaCostoUnitario()),
                entity.getCostoProducto(),
                entity.getCostoLogo(),
                entity.getCostoOrdenTrabajo(),
                entity.getTipoItem(),
                specs,
                entity.getCosteoId(),
                entity.getSolicitudCostosId());
    }

    public EvaluacionNegocioJpaEntity toEntity(EvaluacionNegocio domain) {
        if (domain == null) return null;

        EvaluacionNegocioJpaEntity entity = new EvaluacionNegocioJpaEntity();
        if (domain.getEvaluacionNegocioId() != null) {
            entity.setIdEVN(domain.getEvaluacionNegocioId());
        }
        entity.setNumero(domain.getNumeroEvn().getValue());
        entity.setEstado(domain.getEstado().name());
        entity.setFechaEvaluacion(domain.getFechaEvaluacion());
        entity.setClienteNombre(domain.getClienteNombre());
        entity.setReferencia(domain.getReferencia());

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

        if (domain.getTomaTallaje() != null) {
            TomaTallaje tt = domain.getTomaTallaje();
            TomaTallajeJpaEntity ttEntity = new TomaTallajeJpaEntity();
            ttEntity.setObservaciones(tt.getObservaciones());
            ttEntity.setFechaProgramada(tt.getFechaProgramada());
            ttEntity.setDiasXRecinto(tt.getDiasXRecinto());
            ttEntity.setPersXRecinto(tt.getPersXRecinto());
            ttEntity.setColaccionXPers(tt.getColaccionXPers());
            ttEntity.setAsignacionXPers(tt.getAsignacionXPers());
            ttEntity.setPeajes(tt.getPeajes());
            ttEntity.setBencinaXLt(tt.getBencinaXLt());
            ttEntity.setKmTotal(tt.getKmTotal());
            ttEntity.setRendKmLt(tt.getRendKmLt());
            ttEntity.setRecintos(tt.getRecintos());
            if (tt.getDetalles() != null) {
                tt.getDetalles().forEach(d -> {
                    TomaTallajeDetalleJpaEntity tde = new TomaTallajeDetalleJpaEntity();
                    tde.setIdentificadorPersona(d.getIdentificadorPersona());
                    tde.setTalla(d.getTalla());
                    tde.setCantidad(d.getCantidad());
                    ttEntity.addDetalle(tde);
                });
            }
            entity.setTomaTallajeEntity(ttEntity);
        }

        domain.getGastosAdicionales().forEach(gasto -> {
            GastoAdicionalJpaEntity g = new GastoAdicionalJpaEntity();
            g.setTipoGasto(gasto.getTipoGasto().name());
            g.setMonto(gasto.getMonto().getAmount());
            g.setMoneda(gasto.getMonto().getCurrency());
            if (gasto.getDetalles() != null) {
                gasto.getDetalles().forEach(d -> {
                    GastoAdicionalDetalleJpaEntity gde = new GastoAdicionalDetalleJpaEntity();
                    gde.setClave(d.getClave());
                    gde.setValor(d.getValor());
                    g.addDetalle(gde);
                });
            }
            entity.addGastoAdicional(g);
        });

        entity.setPorcentajeComision(domain.getPorcentajeComision());

        domain.getItems().forEach(itemDomain -> {
            EvaluacionNegocioItemJpaEntity itemEntity = new EvaluacionNegocioItemJpaEntity();
            if (itemDomain.getArticuloId() != null) {
                ArticuloJpaEntity a = new ArticuloJpaEntity();
                a.setIdArticulo(itemDomain.getArticuloId());
                itemEntity.setArticulo(a);
            }
            if (itemDomain.getProveedorId() != null) {
                ProveedorJpaEntity prov = new ProveedorJpaEntity();
                prov.setProveedorId(itemDomain.getProveedorId());
                itemEntity.setProveedor(prov);
            }
            itemEntity.setNroItem(itemDomain.getNroItem());
            itemEntity.setDescripcion(itemDomain.getDescripcion());
            itemEntity.setModelo(itemDomain.getModelo());
            itemEntity.setTela(itemDomain.getTela());
            itemEntity.setComposicion(itemDomain.getComposicion());
            itemEntity.setGenero(itemDomain.getGenero());
            itemEntity.setCodigoInterno(itemDomain.getCodigoInterno());
            itemEntity.setCodigoProveedor(itemDomain.getCodigoProveedor());
            itemEntity.setProveedorNombre(itemDomain.getProveedorNombre());
            itemEntity.setColor(itemDomain.getColor());
            itemEntity.setCantidad(itemDomain.getCantidad());
            if (itemDomain.getPrecioUnitario() != null) {
                itemEntity.setPrecioUnitario(itemDomain.getPrecioUnitario().getAmount());
                itemEntity.setMonedaPrecioUnitario(itemDomain.getPrecioUnitario().getCurrency());
            }
            if (itemDomain.getCostoUnitario() != null) {
                itemEntity.setCostoUnitario(itemDomain.getCostoUnitario().getAmount());
                itemEntity.setMonedaCostoUnitario(itemDomain.getCostoUnitario().getCurrency());
            }
            itemEntity.setCostoProducto(itemDomain.getCostoProducto());
            itemEntity.setCostoLogo(itemDomain.getCostoLogo());
            itemEntity.setCostoOrdenTrabajo(itemDomain.getCostoOrdenTrabajo());
            itemEntity.setTipoItem(itemDomain.getTipoItem());
            itemEntity.setCosteoId(itemDomain.getCosteoId());
            itemEntity.setSolicitudCostosId(itemDomain.getSolicitudCostosId());

            // Specs verdaderamente dinámicas
            if (itemDomain.getTechnicalSpecs() != null) {
                itemDomain.getTechnicalSpecs().forEach(spec -> {
                    EvaluacionNegocioItemSpecJpaEntity specEntity = new EvaluacionNegocioItemSpecJpaEntity();
                    specEntity.setClave(spec.getClave());
                    specEntity.setValor(spec.getValor());
                    itemEntity.addSpec(specEntity);
                });
            }

            entity.addItem(itemEntity);
        });

        return entity;
    }

    public void updateEntityFromDomain(EvaluacionNegocio domain, EvaluacionNegocioJpaEntity entity) {
        if (domain == null || entity == null) return;

        entity.setEstado(domain.getEstado().name());
        entity.setFechaEvaluacion(domain.getFechaEvaluacion());
        entity.setClienteNombre(domain.getClienteNombre());
        entity.setReferencia(domain.getReferencia());
        entity.setPorcentajeComision(domain.getPorcentajeComision());
    }
}
