package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.SeguimientoOPJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SeguimientoOPMapper {

    public SeguimientoOP toDomain(SeguimientoOPJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        SeguimientoOP domain = new SeguimientoOP();
        domain.setIdSeguimiento(entity.getIdSeguimiento());
        if (entity.getOrdenProduccion() != null) {
            domain.setOrdenProduccionId(entity.getOrdenProduccion().getIdOP());
        }
        
        domain.setFechaRecepcionOp(entity.getFechaRecepcionOp());
        domain.setFinTizado(entity.getFinTizado());
        domain.setEstadoOcMp(entity.getEstadoOcMp());
        domain.setRecepcionCompras(entity.getRecepcionCompras());
        domain.setInicioCorte(entity.getInicioCorte());
        domain.setFinCorte(entity.getFinCorte());
        domain.setInicioLogo(entity.getInicioLogo());
        domain.setEstadoIdaLogo(entity.getEstadoIdaLogo());
        domain.setRegresoLogo(entity.getRegresoLogo());
        domain.setEstadoRecLogo(entity.getEstadoRecLogo());
        domain.setInicioTallerExterno(entity.getInicioTallerExterno());
        domain.setFinTallerExterno(entity.getFinTallerExterno());
        domain.setCalidadTaller(entity.getCalidadTaller());
        domain.setObsTaller(entity.getObsTaller());
        domain.setFinTerminacion(entity.getFinTerminacion());
        domain.setFinPersonalizado(entity.getFinPersonalizado());
        
        return domain;
    }

    public SeguimientoOPJpaEntity toJpaEntity(SeguimientoOP domain, OrdenProduccionJpaEntity opEntity) {
        if (domain == null) {
            return null;
        }

        SeguimientoOPJpaEntity entity = new SeguimientoOPJpaEntity();
        entity.setIdSeguimiento(domain.getIdSeguimiento());
        entity.setOrdenProduccion(opEntity);
        
        entity.setFechaRecepcionOp(domain.getFechaRecepcionOp());
        entity.setFinTizado(domain.getFinTizado());
        entity.setEstadoOcMp(domain.getEstadoOcMp());
        entity.setRecepcionCompras(domain.getRecepcionCompras());
        entity.setInicioCorte(domain.getInicioCorte());
        entity.setFinCorte(domain.getFinCorte());
        entity.setInicioLogo(domain.getInicioLogo());
        entity.setEstadoIdaLogo(domain.getEstadoIdaLogo());
        entity.setRegresoLogo(domain.getRegresoLogo());
        entity.setEstadoRecLogo(domain.getEstadoRecLogo());
        entity.setInicioTallerExterno(domain.getInicioTallerExterno());
        entity.setFinTallerExterno(domain.getFinTallerExterno());
        entity.setCalidadTaller(domain.getCalidadTaller());
        entity.setObsTaller(domain.getObsTaller());
        entity.setFinTerminacion(domain.getFinTerminacion());
        entity.setFinPersonalizado(domain.getFinPersonalizado());

        return entity;
    }
}
