package backend.com.produccion.infrastructure.mapper;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.produccion.domain.model.DespachoOS;
import backend.com.produccion.domain.model.OrdenServicio;
import backend.com.produccion.domain.model.RecepcionOS;
import backend.com.produccion.infrastructure.persistence.entity.DespachoOSJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenServicioJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.RecepcionOSJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrdenServicioMapper {

    @PersistenceContext
    private EntityManager em;

    public OrdenServicio toDomain(OrdenServicioJpaEntity entity) {
        if (entity == null) return null;

        List<DespachoOS> despachos = entity.getDespachos() != null
                ? entity.getDespachos().stream().map(this::despachoToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        List<RecepcionOS> recepciones = entity.getRecepciones() != null
                ? entity.getRecepciones().stream().map(this::recepcionToDomain).collect(Collectors.toList())
                : new ArrayList<>();

        return new OrdenServicio(
                entity.getIdOS(),
                entity.getNumeroOS() != null ? new DocumentNumber(entity.getNumeroOS()) : null,
                entity.getOrdenProduccion() != null ? entity.getOrdenProduccion().getIdOP() : null,
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getTipoServicio(),
                entity.getEstado(),
                entity.getFechaEmision(),
                entity.getFechaEntregaEstimada(),
                entity.getDescripcionTrabajo(),
                entity.getCantidadPactada(),
                entity.getPrecioUnitario(),
                entity.getTotalNeto(),
                entity.getObservaciones(),
                despachos,
                recepciones);
    }

    public OrdenServicioJpaEntity toJpaEntity(OrdenServicio domain) {
        if (domain == null) return null;

        OrdenServicioJpaEntity entity = new OrdenServicioJpaEntity();
        entity.setIdOS(domain.getIdOS());
        if (domain.getNumeroOS() != null) {
            entity.setNumeroOS(domain.getNumeroOS().getValue());
        }
        if (domain.getOpId() != null) {
            entity.setOrdenProduccion(em.getReference(OrdenProduccionJpaEntity.class, domain.getOpId()));
        }
        if (domain.getProveedorId() != null) {
            entity.setProveedor(em.getReference(ProveedorJpaEntity.class, domain.getProveedorId()));
        }
        entity.setTipoServicio(domain.getTipoServicio());
        entity.setEstado(domain.getEstado());
        entity.setFechaEmision(domain.getFechaEmision());
        entity.setFechaEntregaEstimada(domain.getFechaEntregaEstimada());
        entity.setDescripcionTrabajo(domain.getDescripcionTrabajo());
        entity.setCantidadPactada(domain.getCantidadPactada());
        entity.setPrecioUnitario(domain.getPrecioUnitario());
        entity.setTotalNeto(domain.getTotalNeto());
        entity.setObservaciones(domain.getObservaciones());

        if (domain.getDespachos() != null) {
            List<DespachoOSJpaEntity> despachosEntity = domain.getDespachos().stream()
                    .map(d -> {
                        DespachoOSJpaEntity de = despachoToJpaEntity(d);
                        de.setOrdenServicio(entity);
                        return de;
                    })
                    .collect(Collectors.toList());
            entity.setDespachos(despachosEntity);
        }

        if (domain.getRecepciones() != null) {
            List<RecepcionOSJpaEntity> recepcionesEntity = domain.getRecepciones().stream()
                    .map(r -> {
                        RecepcionOSJpaEntity re = recepcionToJpaEntity(r);
                        re.setOrdenServicio(entity);
                        return re;
                    })
                    .collect(Collectors.toList());
            entity.setRecepciones(recepcionesEntity);
        }

        return entity;
    }

    private DespachoOS despachoToDomain(DespachoOSJpaEntity entity) {
        return new DespachoOS(
                entity.getIdDespacho(),
                entity.getOrdenServicio() != null ? entity.getOrdenServicio().getIdOS() : null,
                entity.getFechaDespacho(),
                entity.getCantidadDespachada(),
                entity.getDescripcion(),
                entity.getResponsable(),
                entity.getObservaciones());
    }

    private DespachoOSJpaEntity despachoToJpaEntity(DespachoOS domain) {
        DespachoOSJpaEntity entity = new DespachoOSJpaEntity();
        entity.setIdDespacho(domain.getIdDespacho());
        entity.setFechaDespacho(domain.getFechaDespacho());
        entity.setCantidadDespachada(domain.getCantidadDespachada());
        entity.setDescripcion(domain.getDescripcion());
        entity.setResponsable(domain.getResponsable());
        entity.setObservaciones(domain.getObservaciones());
        return entity;
    }

    private RecepcionOS recepcionToDomain(RecepcionOSJpaEntity entity) {
        return new RecepcionOS(
                entity.getIdRecepcion(),
                entity.getOrdenServicio() != null ? entity.getOrdenServicio().getIdOS() : null,
                entity.getFechaRecepcion(),
                entity.getCantidadRecibida(),
                entity.getCantidadConforme(),
                entity.getCantidadDefectuosa(),
                entity.getResponsable(),
                entity.getObservaciones());
    }

    private RecepcionOSJpaEntity recepcionToJpaEntity(RecepcionOS domain) {
        RecepcionOSJpaEntity entity = new RecepcionOSJpaEntity();
        entity.setIdRecepcion(domain.getIdRecepcion());
        entity.setFechaRecepcion(domain.getFechaRecepcion());
        entity.setCantidadRecibida(domain.getCantidadRecibida());
        entity.setCantidadConforme(domain.getCantidadConforme());
        entity.setCantidadDefectuosa(domain.getCantidadDefectuosa());
        entity.setResponsable(domain.getResponsable());
        entity.setObservaciones(domain.getObservaciones());
        return entity;
    }
}
