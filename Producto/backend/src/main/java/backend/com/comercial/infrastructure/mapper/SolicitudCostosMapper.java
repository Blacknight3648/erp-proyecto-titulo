package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.domain.model.*;
import backend.com.comercial.infrastructure.persistence.entity.*;
import backend.com.shared.infrastructure.persistence.entity.EspecificacionTecnica;
import backend.com.shared.infrastructure.persistence.entity.Tela;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SolicitudCostosMapper {

    public SolicitudCostos toDomain(SolicitudCostosJpaEntity entity) {
        if (entity == null)
            return null;

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
                entity.getEspecificacionTecnica() != null
                        ? entity.getEspecificacionTecnica().getEspecificacionTecnicaId()
                        : null,
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
                (entity.getPlantillas() != null)
                        ? entity.getPlantillas().stream().map(this::mapPlantillaToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                (entity.getPrendas() != null)
                        ? entity.getPrendas().stream().map(this::mapPrendaToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                (entity.getLogotipos() != null)
                        ? entity.getLogotipos().stream().map(this::mapLogotipoToDomain).collect(Collectors.toList())
                        : new ArrayList<>(),
                entity.getCostoTotal());
    }

    private SCOSTela mapTelaToDomain(SCOSTelaJpaEntity entity) {
        if (entity == null)
            return null;
        return new SCOSTela(
                entity.getTela() != null ? entity.getTela().getIdTela() : null,
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
                null // tempId
        );
    }

    private SCOSAccesorio mapAccesorioToDomain(SCOSAccesorioJpaEntity entity) {
        if (entity == null)
            return null;
        return new SCOSAccesorio(
                entity.getAccesorio() != null ? entity.getAccesorio().getAccesorioId() : null,
                entity.getTipo(),
                entity.getDescripcion(),
                entity.getCantidad(),
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getProveedorReferencia(),
                entity.getConsumo(),
                entity.getUnidadMedida(),
                new Money(entity.getPrecioUnitario() != null ? entity.getPrecioUnitario() : BigDecimal.ZERO,
                        entity.getMonedaPrecioUnitario() != null ? entity.getMonedaPrecioUnitario() : "CLP"),
                null // tempId
        );
    }

    private SCOSPlantilla mapPlantillaToDomain(SCOSPlantillaJpaEntity entity) {
        if (entity == null)
            return null;

        List<backend.com.comercial.domain.model.PlantillaTela> telasDom = (entity.getPlantillaTelas() != null)
                ? entity.getPlantillaTelas().stream()
                        .map(t -> new backend.com.comercial.domain.model.PlantillaTela(t.getAplicacion(), t.getNombre(),
                                t.getComposicion(), t.getColor(), t.getPeso(), t.getUnidadMedida()))
                        .collect(Collectors.toList())
                : new ArrayList<>();

        List<backend.com.comercial.domain.model.PlantillaAccesorio> accesoriosDom = (entity
                .getPlantillaAccesorios() != null)
                        ? entity.getPlantillaAccesorios().stream()
                                .map(a -> new backend.com.comercial.domain.model.PlantillaAccesorio(a.getTipo(),
                                        a.getNombreAccesorio(), a.getCantidad()))
                                .collect(Collectors.toList())
                        : new ArrayList<>();

        List<SCOSLogotipo> logotiposDom = (entity.getPlantillaLogotipos() != null)
                ? entity.getPlantillaLogotipos().stream()
                        .map(l -> new SCOSLogotipo(null, l.getTipo(), l.getNombre(), l.getUbicacion(), l.getColor(),
                                l.getTamano(), l.getCantidad(), l.getPrecio()))
                        .collect(Collectors.toList())
                : new ArrayList<>();

        return new SCOSPlantilla(
                entity.getId(),
                entity.getNombre(),
                entity.getDescripcion(),
                entity.getNombrePrenda(),
                entity.getForro(),
                entity.getRelleno(),
                entity.getGorro(),
                entity.getCuello(),
                entity.getAbotonaduraCierre(),
                entity.getCortesAplicaciones(),
                entity.getFuelles(),
                entity.getMangas(),
                entity.getPretinasRuedo(),
                entity.getBolsillos(),
                entity.getCintaDetalle(),
                entity.getLogoDetalle(),
                entity.getColorForro(),
                entity.getAccesoriosDetalle(),
                entity.getObsModelo(),
                entity.getGenero(),
                entity.getCustomFields(),
                entity.getCamposActivos() != null ? new ArrayList<String>(entity.getCamposActivos())
                        : new ArrayList<String>(),
                telasDom,
                accesoriosDom,
                logotiposDom,
                (entity.getVinculos() != null)
                        ? entity.getVinculos().stream()
                                .map(v -> new SCOSPlantillaMaterialVinculo(v.getId(), null, v.getFieldName(),
                                        v.getMaterialType(), v.getMaterialId(), null, v.getCantidad()))
                                .collect(Collectors.toList())
                        : new ArrayList<>(),
                entity.getMoPrenda(),
                entity.getMoCosturaSellada(),
                entity.getMoAcolchado());
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

    private SCOSPlantillaJpaEntity toPlantillaEntity(SCOSPlantilla domain) {
        SCOSPlantillaJpaEntity entity = new SCOSPlantillaJpaEntity();
        entity.setId(domain.getId());
        entity.setNombre(domain.getNombre());
        entity.setDescripcion(domain.getDescripcion());
        entity.setNombrePrenda(domain.getNombrePrenda());
        entity.setForro(domain.getForro());
        entity.setRelleno(domain.getRelleno());
        entity.setGorro(domain.getGorro());
        entity.setCuello(domain.getCuello());
        entity.setAbotonaduraCierre(domain.getAbotonaduraCierre());
        entity.setCortesAplicaciones(domain.getCortesAplicaciones());
        entity.setFuelles(domain.getFuelles());
        entity.setMangas(domain.getMangas());
        entity.setPretinasRuedo(domain.getPretinasRuedo());
        entity.setBolsillos(domain.getBolsillos());
        entity.setCintaDetalle(domain.getCintaDetalle());
        entity.setLogoDetalle(domain.getLogoDetalle());
        entity.setColorForro(domain.getColorForro());
        entity.setAccesoriosDetalle(domain.getAccesoriosDetalle());
        entity.setObsModelo(domain.getObsModelo());

        if (domain.getPlantillaTelas() != null) {
            entity.setPlantillaTelas(domain.getPlantillaTelas().stream()
                    .map(t -> {
                        backend.com.comercial.infrastructure.persistence.entity.PlantillaTela pt = new backend.com.comercial.infrastructure.persistence.entity.PlantillaTela();
                        pt.setAplicacion(t.getAplicacion());
                        pt.setNombre(t.getNombre());
                        pt.setComposicion(t.getComposicion());
                        pt.setColor(t.getColor());
                        pt.setPeso(t.getPeso());
                        pt.setUnidadMedida(t.getUnidadMedida());
                        return pt;
                    })
                    .collect(Collectors.toList()));
        }

        if (domain.getPlantillaAccesorios() != null) {
            entity.setPlantillaAccesorios(domain.getPlantillaAccesorios().stream()
                    .map(a -> {
                        backend.com.comercial.infrastructure.persistence.entity.PlantillaAccesorio pa = new backend.com.comercial.infrastructure.persistence.entity.PlantillaAccesorio();
                        pa.setTipo(a.getTipo());
                        pa.setNombreAccesorio(a.getNombreAccesorio());
                        pa.setCantidad(a.getCantidad());
                        return pa;
                    })
                    .collect(Collectors.toList()));
        }

        if (domain.getPlantillaLogotipos() != null) {
            entity.setPlantillaLogotipos(domain.getPlantillaLogotipos().stream()
                    .map(l -> new PlantillaLogotipo(l.getTipo(), l.getNombre(), l.getUbicacion(), l.getColor(),
                            l.getTamano(), l.getCantidad(), l.getPrecio()))
                    .collect(Collectors.toList()));
        }

        entity.setGenero(domain.getGenero());
        if (domain.getCamposActivos() != null) {
            entity.setCamposActivos(new ArrayList<>(domain.getCamposActivos()));
        }

        entity.setMoPrenda(domain.getMoPrenda());
        entity.setMoCosturaSellada(domain.getMoCosturaSellada());
        entity.setMoAcolchado(domain.getMoAcolchado());

        if (domain.getVinculos() != null) {
            entity.setVinculos(domain.getVinculos().stream()
                    .map(v -> {
                        SCOSPlantillaMaterialVinculoJpaEntity ve = new SCOSPlantillaMaterialVinculoJpaEntity();
                        ve.setFieldName(v.getFieldName());
                        ve.setMaterialType(v.getMaterialType());
                        ve.setMaterialId(v.getMaterialId());
                        ve.setCantidad(v.getCantidad());
                        ve.setPlantilla(entity);
                        return ve;
                    })
                    .collect(Collectors.toList()));
        }

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

    public SolicitudCostosJpaEntity toEntity(SolicitudCostos domain) {
        if (domain == null)
            return null;

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
        if (domain.getEspecificacionTecnicaId() != null) {
            EspecificacionTecnica et = new EspecificacionTecnica();
            et.setEspecificacionTecnicaId(domain.getEspecificacionTecnicaId());
            entity.setEspecificacionTecnica(et);
        }

        domain.getTelas().forEach(t -> {
            SCOSTelaJpaEntity te = new SCOSTelaJpaEntity();
            if (t.getIdTela() != null) {
                Tela telaResource = new Tela();
                telaResource.setIdTela(t.getIdTela());
                te.setTela(telaResource);
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

        domain.getPlantillas().forEach(p -> entity.addPlantilla(toPlantillaEntity(p)));
        domain.getProductos().forEach(prod -> entity.addPrenda(toPrendaEntity(prod)));
        domain.getLogotipos().forEach(l -> entity.addLogotipo(toLogotipoEntity(l)));
        domain.getAccesorios().forEach(a -> {
            SCOSAccesorioJpaEntity ae = new SCOSAccesorioJpaEntity();
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
