package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.Producto;
import backend.com.shared.infrastructure.persistence.entity.ProductoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toDomain(ProductoJpaEntity entity) {
        if (entity == null)
            return null;
        return Producto.builder()
                .productoId(entity.getProductoId())
                .codigoProducto(entity.getCodigoProducto())
                .nombreProducto(entity.getNombre())
                .descripcionProducto(entity.getDescripcion())
                .generoProducto(entity.getGenero())
                .colorProducto(entity.getColor())
                .build();
    }

    public ProductoJpaEntity toEntity(Producto domain) {
        if (domain == null)
            return null;
        ProductoJpaEntity entity = new ProductoJpaEntity();
        entity.setProductoId(domain.getProductoId());
        entity.setCodigoProducto(domain.getCodigoProducto());
        entity.setNombre(domain.getNombreProducto());
        entity.setDescripcion(domain.getDescripcionProducto());
        entity.setGenero(domain.getGeneroProducto());
        entity.setColor(domain.getColorProducto());
        return entity;
    }
}
