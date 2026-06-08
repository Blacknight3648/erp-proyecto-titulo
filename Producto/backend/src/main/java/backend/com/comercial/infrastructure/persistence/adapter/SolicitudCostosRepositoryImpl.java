package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.SCOSAccesorio;
import backend.com.comercial.domain.model.SCOSLogotipo;
import backend.com.comercial.domain.model.SCOSTela;
import backend.com.comercial.domain.model.SolicitudCostos;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.comercial.infrastructure.mapper.SolicitudCostosMapper;
import backend.com.comercial.infrastructure.persistence.entity.SCOSAccesorioJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SCOSLogotipoJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SCOSTelaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SolicitudCostosJpaEntity;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.EspecificacionTecnica;
import backend.com.shared.valueobjects.DocumentNumber;
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
        if (domain == null) return null;

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

        SolicitudCostosJpaEntity reloaded = jpaRepository.findById(savedEntity.getIdSCOS())
                .orElse(savedEntity);

        return mapper.toDomain(reloaded);
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
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    @SuppressWarnings("null")
    public Optional<SolicitudCostos> findByNumero(DocumentNumber numero) {
        if (numero == null || numero.getValue() == null) return Optional.empty();
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
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    public long countByTipo(String tipo) {
        return jpaRepository.countByTipo(tipo);
    }
}
