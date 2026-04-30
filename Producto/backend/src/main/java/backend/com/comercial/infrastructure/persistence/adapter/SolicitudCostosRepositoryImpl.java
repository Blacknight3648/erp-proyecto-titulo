package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.*;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.comercial.infrastructure.persistence.entity.*;
import backend.com.comercial.infrastructure.mapper.SolicitudCostosMapper;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.infrastructure.persistence.entity.EspecificacionTecnica;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SolicitudCostosRepositoryImpl implements SolicitudCostosRepository {

    private final SolicitudCostosJpaRepository jpaRepository;
    private final SolicitudCostosMapper mapper;

    public SolicitudCostosRepositoryImpl(SolicitudCostosJpaRepository jpaRepository, SolicitudCostosMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    @SuppressWarnings("null")
    public SolicitudCostos save(SolicitudCostos domain) {
        if (domain == null)
            return null;

        SolicitudCostosJpaEntity entity;
        Long id = domain.getIdSCOS();

        if (id != null) {
            Optional<SolicitudCostosJpaEntity> found = jpaRepository.findById(id);
            entity = found.orElseGet(SolicitudCostosJpaEntity::new);
        } else {
            entity = new SolicitudCostosJpaEntity();
        }

        syncEntityWithDomain(entity, domain);

        SolicitudCostosJpaEntity savedEntity = jpaRepository.save(entity);

        // Resolve temp IDs in vinculos
        boolean needsSecondSave = resolveVinculosCorrelation(savedEntity);
        if (needsSecondSave) {
            savedEntity = jpaRepository.save(savedEntity);
        }

        return mapper.toDomain(savedEntity);
    }

    private boolean resolveVinculosCorrelation(SolicitudCostosJpaEntity entity) {
        if (entity.getPlantillas() == null)
            return false;

        // Build map of tempId -> databaseId
        java.util.Map<String, Long> correlationMap = new java.util.HashMap<>();
        if (entity.getTelas() != null) {
            for (SCOSTelaJpaEntity t : entity.getTelas()) {
                if (t.getTempId() != null)
                    correlationMap.put(t.getTempId(), t.getIdSCOSTela());
            }
        }
        if (entity.getAccesorios() != null) {
            for (SCOSAccesorioJpaEntity a : entity.getAccesorios()) {
                if (a.getTempId() != null)
                    correlationMap.put(a.getTempId(), a.getIdSCOSAccesorio());
            }
        }

        if (correlationMap.isEmpty())
            return false;

        boolean modified = false;
        for (SCOSPlantillaJpaEntity p : entity.getPlantillas()) {
            if (p.getVinculos() != null) {
                for (SCOSPlantillaMaterialVinculoJpaEntity v : p.getVinculos()) {
                    if (v.getTempMaterialId() != null) {
                        Long resolvedId = correlationMap.get(v.getTempMaterialId());
                        if (resolvedId != null) {
                            v.setMaterialId(resolvedId);
                            v.setTempMaterialId(null); // Clear temp ID once resolved
                            modified = true;
                        }
                    }
                }
            }
        }
        return modified;
    }

    private void syncEntityWithDomain(SolicitudCostosJpaEntity entity, SolicitudCostos domain) {
        if (domain.getNumeroSCOS() != null) {
            entity.setNumero(domain.getNumeroSCOS().getValue());
        }
        entity.setEstado(domain.getEstado());
        entity.setTipo(domain.getTipo());
        entity.setArticuloDescripcion(domain.getArticuloDescripcion());
        entity.setNombrePrenda(domain.getNombrePrenda());
        entity.setGenero(domain.getGenero());
        entity.setEsMuestra(domain.getEsMuestra());
        entity.setHasLogo(domain.getHasLogo());
        entity.setCantidad(domain.getCantidad());
        entity.setFecha(domain.getFecha());
        entity.setTallaje(domain.getTallaje());
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

        entity.clearCollections();

        // Sync Telas
        if (domain.getTelas() != null) {
            for (SCOSTela t : domain.getTelas()) {
                SCOSTelaJpaEntity te = new SCOSTelaJpaEntity();
                te.setDescripcion(t.getDescripcion());
                te.setProveedorReferencia(t.getProveedorReferencia());
                if (t.getProveedorId() != null) {
                    ProveedorJpaEntity p = new ProveedorJpaEntity();
                    p.setProveedorId(t.getProveedorId());
                    te.setProveedor(p);
                }
                te.setComposicion(t.getComposicion());
                te.setColor(t.getColor());
                te.setPeso(t.getPeso());
                te.setConsumo(t.getConsumo());
                te.setUnidadMedida(t.getUnidadMedida());
                te.setTempId(t.getTempId());
                if (t.getPrecioUnitario() != null) {
                    te.setPrecioUnitario(t.getPrecioUnitario().getAmount());
                    te.setMonedaPrecioUnitario(t.getPrecioUnitario().getCurrency());
                    if (t.getConsumo() != null) {
                        te.setCostoTotal(t.getPrecioUnitario().getAmount().multiply(t.getConsumo()));
                        te.setMonedaCostoTotal(t.getPrecioUnitario().getCurrency());
                    }
                }
                entity.addTela(te);
            }
        }

        // Sync Accesorios
        if (domain.getAccesorios() != null) {
            for (SCOSAccesorio a : domain.getAccesorios()) {
                SCOSAccesorioJpaEntity ae = new SCOSAccesorioJpaEntity();
                ae.setDescripcion(a.getDescripcion());
                ae.setProveedorReferencia(a.getProveedorReferencia());
                ae.setConsumo(a.getConsumo());
                ae.setUnidadMedida(a.getUnidadMedida());
                if (a.getPrecioUnitario() != null) {
                    ae.setPrecioUnitario(a.getPrecioUnitario().getAmount());
                    ae.setMonedaPrecioUnitario(a.getPrecioUnitario().getCurrency());
                }
                ae.setTempId(a.getTempId());
                entity.addAccesorio(ae);
            }
        }

        // Sync Plantillas
        if (domain.getPlantillas() != null) {
            for (SCOSPlantilla p : domain.getPlantillas()) {
                SCOSPlantillaJpaEntity pe = new SCOSPlantillaJpaEntity();
                pe.setNombre(p.getNombre());
                pe.setDescripcion(p.getDescripcion());
                pe.setNombrePrenda(p.getNombrePrenda());
                pe.setForro(p.getForro());
                pe.setRelleno(p.getRelleno());
                pe.setGorro(p.getGorro());
                pe.setCuello(p.getCuello());
                pe.setAbotonaduraCierre(p.getAbotonaduraCierre());
                pe.setCortesAplicaciones(p.getCortesAplicaciones());
                pe.setFuelles(p.getFuelles());
                pe.setMangas(p.getMangas());
                pe.setPretinasRuedo(p.getPretinasRuedo());
                pe.setBolsillos(p.getBolsillos());
                pe.setCintaDetalle(p.getCintaDetalle());
                pe.setLogoDetalle(p.getLogoDetalle());
                pe.setColorForro(p.getColorForro());
                pe.setAccesoriosDetalle(p.getAccesoriosDetalle());
                pe.setObsModelo(p.getObsModelo());
                pe.setGenero(p.getGenero());
                pe.setCamposActivos(
                        p.getCamposActivos() != null ? new ArrayList<>(p.getCamposActivos()) : new ArrayList<>());

                // Sync nested lists in Plantilla
                if (p.getPlantillaTelas() != null) {
                    for (backend.com.comercial.domain.model.PlantillaTela pt : p.getPlantillaTelas()) {
                        backend.com.comercial.infrastructure.persistence.entity.PlantillaTela pte = new backend.com.comercial.infrastructure.persistence.entity.PlantillaTela();
                        pte.setAplicacion(pt.getAplicacion());
                        pte.setNombre(pt.getNombre());
                        pte.setComposicion(pt.getComposicion());
                        pte.setColor(pt.getColor());
                        pte.setPeso(pt.getPeso());
                        pe.getPlantillaTelas().add(pte);
                    }
                }
                if (p.getPlantillaAccesorios() != null) {
                    for (backend.com.comercial.domain.model.PlantillaAccesorio pa : p.getPlantillaAccesorios()) {
                        backend.com.comercial.infrastructure.persistence.entity.PlantillaAccesorio pae = new backend.com.comercial.infrastructure.persistence.entity.PlantillaAccesorio();
                        pae.setTipo(pa.getTipo());
                        pae.setNombreAccesorio(pa.getNombreAccesorio());
                        pae.setCantidad(pa.getCantidad());
                        pe.getPlantillaAccesorios().add(pae);
                    }
                }
                if (p.getPlantillaLogotipos() != null) {
                    for (SCOSLogotipo pl : p.getPlantillaLogotipos()) {
                        pe.getPlantillaLogotipos().add(new PlantillaLogotipo(pl.getTipo(), pl.getNombre(),
                                pl.getUbicacion(), pl.getColor(), pl.getTamano(), pl.getCantidad(), pl.getPrecio()));
                    }
                }

                // Mano de Obra
                pe.setMoPrenda(p.getMoPrenda());

                pe.setMoCosturaSellada(p.getMoCosturaSellada());
                pe.setMoAcolchado(p.getMoAcolchado());

                entity.addPlantilla(pe);
            }
        }

        // --- Correlation Logic for Vinculos ---
        // 1. Build a map of TempId -> DatabaseId from the saved entities
        // Note: Since jpaRepository.save(entity) hasn't happened yet, we might need
        // to handle this differently IF we want to resolve them BEFORE the save.
        // Actually, we can add them to the entity and JPA will save them in cascade.
        // BUT we need the IDs.

        // Strategy: First pass of saving SCOS will populate IDs.
        // For now, let's just make sure they are mapped.
        if (domain.getPlantillas() != null) {
            for (int i = 0; i < domain.getPlantillas().size(); i++) {
                SCOSPlantilla p = domain.getPlantillas().get(i);
                SCOSPlantillaJpaEntity pe = entity.getPlantillas().get(i);

                if (p.getVinculos() != null) {
                    for (SCOSPlantillaMaterialVinculo v : p.getVinculos()) {
                        SCOSPlantillaMaterialVinculoJpaEntity ve = new SCOSPlantillaMaterialVinculoJpaEntity();
                        ve.setFieldName(v.getFieldName());
                        ve.setMaterialType(v.getMaterialType());

                        // If materialId is already present (Long), use it
                        if (v.getMaterialId() != null) {
                            ve.setMaterialId(v.getMaterialId());
                        }
                        // If it's a new material with tempId, we'll try to find it in the current SCOS
                        else if (v.getTempMaterialId() != null) {
                            // We store the tempId in the JpaEntity for now,
                            // we'll resolve it in the Save method AFTER the first flush if needed,
                            // but here we can't easily get the ID before the actual save.
                            // HOWEVER, if we are in the same TRANSACTION, maybe we can?

                            // Actually, let's just use the tempMaterialId to find the corresponding
                            // SCOSTelaJpaEntity or SCOSAccesorioJpaEntity we just added.
                            if ("TELA".equals(v.getMaterialType())) {
                                entity.getTelas().stream()
                                        .filter(t -> v.getTempMaterialId().equals(t.getTempId()))
                                        .findFirst()
                                        .ifPresent(t -> {
                                            // We can't set the ID yet, but we can set the relation if we had one.
                                            // The DB currently use a Long material_id, not a real FK to SCOSTela.
                                            // This is a design limitation.
                                        });
                            }
                        }

                        ve.setCantidad(v.getCantidad());
                        ve.setTempMaterialId(v.getTempMaterialId());
                        pe.addVinculo(ve);
                    }
                }
            }
        }

        // Sync Prendas
        if (domain.getProductos() != null) {
            for (SCOTPrendaLista p : domain.getProductos()) {
                SCOTPrendaListaJpaEntity pre = new SCOTPrendaListaJpaEntity();
                pre.setNombre(p.getNombre());
                pre.setCantidad(p.getCantidad());
                pre.setTalla(p.getTalla());
                pre.setColor(p.getColor());
                pre.setProveedorReferencia(p.getProveedorReferencia());
                pre.setLinkReferencia(p.getLinkReferencia());
                pre.setComposicion(p.getComposicion());
                pre.setPeso(p.getPeso());
                pre.setObservaciones(p.getObservaciones());
                if (p.getPrecioUnitario() != null) {
                    pre.setPrecioUnitario(p.getPrecioUnitario().getAmount());
                    pre.setMonedaPrecioUnitario(p.getPrecioUnitario().getCurrency());
                }
                entity.addPrenda(pre);
            }
        }

        // Sync Logotipos
        if (domain.getLogotipos() != null) {
            for (SCOSLogotipo l : domain.getLogotipos()) {
                SCOSLogotipoJpaEntity le = new SCOSLogotipoJpaEntity();
                le.setTipo(l.getTipo());
                le.setNombre(l.getNombre());
                le.setUbicacion(l.getUbicacion());
                le.setColor(l.getColor());
                le.setTamano(l.getTamano());
                le.setCantidad(l.getCantidad());
                le.setPrecio(l.getPrecio());
                entity.addLogotipo(le);
            }
        }

    }

    @Override
    public SolicitudCostos update(SolicitudCostos solicitudCostos) {
        return save(solicitudCostos);
    }

    @Override
    @SuppressWarnings("null")
    public Optional<SolicitudCostos> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        Optional<SolicitudCostosJpaEntity> entity = jpaRepository.findById(id);
        if (entity.isPresent()) {
            return Optional.of(mapper.toDomain(entity.get()));
        }
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("null")
    public Optional<SolicitudCostos> findByNumero(DocumentNumber numero) {
        if (numero == null || numero.getValue() == null) {
            return Optional.empty();
        }
        List<SolicitudCostosJpaEntity> all = jpaRepository.findAll();
        for (SolicitudCostosJpaEntity e : all) {
            if (e.getNumero() != null && e.getNumero().equals(numero.getValue())) {
                return Optional.of(mapper.toDomain(e));
            }
        }
        return Optional.empty();
    }

    @Override
    @SuppressWarnings("null")
    public List<SolicitudCostos> findAll() {
        List<SolicitudCostosJpaEntity> entities = jpaRepository.findAll();
        List<SolicitudCostos> domainModels = new ArrayList<>();
        for (SolicitudCostosJpaEntity entity : entities) {
            domainModels.add(mapper.toDomain(entity));
        }
        return domainModels;
    }

    @Override
    @SuppressWarnings("null")
    public void deleteById(Long id) {
        if (id != null) {
            jpaRepository.deleteById(id);
        }
    }

    @Override
    public long countByTipo(String tipo) {
        return jpaRepository.countByTipo(tipo);
    }

}
