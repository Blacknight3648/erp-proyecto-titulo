package backend.com.produccion.infrastructure.mapper;

import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionItemJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrdenProduccionMapper {

    public OrdenProduccion toDomain(OrdenProduccionJpaEntity entity) {
        if (entity == null)
            return null;

        return new OrdenProduccion(
                entity.getIdOP(),
                entity.getNumeroOP() != null ? new DocumentNumber(entity.getNumeroOP()) : null,
                entity.getNotaVentaId(),
                entity.getEstado(),
                entity.getFechaInicio(),
                entity.getFechaEntregaProgramada(),
                entity.getObservaciones(),
                entity.getItems().stream().map(this::itemToDomain).collect(Collectors.toList()));
    }

    public OrdenProduccionJpaEntity toJpaEntity(OrdenProduccion domain) {
        if (domain == null)
            return null;

        OrdenProduccionJpaEntity entity = new OrdenProduccionJpaEntity();
        entity.setIdOP(domain.getIdOP());
        if (domain.getNumeroOP() != null) {
            entity.setNumeroOP(domain.getNumeroOP().getValue());
        }
        entity.setNotaVentaId(domain.getNotaVentaId());
        entity.setEstado(domain.getEstado());
        entity.setFechaInicio(domain.getFechaInicio());
        entity.setFechaEntregaProgramada(domain.getFechaEntregaProgramada());
        entity.setObservaciones(domain.getObservaciones());

        if (domain.getItems() != null) {
            entity.setItems(domain.getItems().stream()
                    .map(item -> itemToJpaEntity(item, entity))
                    .collect(Collectors.toList()));
        }

        return entity;
    }

    private OrdenProduccionItem itemToDomain(OrdenProduccionItemJpaEntity entity) {
        return new OrdenProduccionItem(
                entity.getIdOPItem(),
                entity.getProductoId(),
                entity.getNroItem(),
                entity.getModelo(),
                entity.getTela(),
                entity.getComposicion(),
                entity.getColor(),
                entity.getTalla(),
                entity.getGenero(),
                entity.getCodigo(),
                entity.getLlevaLogo(),
                entity.getCantidad());
    }

    private OrdenProduccionItemJpaEntity itemToJpaEntity(OrdenProduccionItem domain,
            OrdenProduccionJpaEntity opEntity) {
        OrdenProduccionItemJpaEntity entity = new OrdenProduccionItemJpaEntity();
        entity.setIdOPItem(domain.getIdOPItem());
        entity.setOrdenProduccion(opEntity);
        entity.setProductoId(domain.getProductoId());
        entity.setNroItem(domain.getNroItem());
        entity.setModelo(domain.getModelo());
        entity.setTela(domain.getTela());
        entity.setComposicion(domain.getComposicion());
        entity.setColor(domain.getColor());
        entity.setTalla(domain.getTalla());
        entity.setGenero(domain.getGenero());
        entity.setCodigo(domain.getCodigo());
        entity.setLlevaLogo(domain.getLlevaLogo());
        entity.setCantidad(domain.getCantidad());
        return entity;
    }
}
