package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticuloMapper {

    private final CategoriaMapper categoriaMapper;
    private final SubCategoriaMapper subCategoriaMapper;
    private final UnidadMedidaMapper unidadMedidaMapper;
    private final ArticuloTelaMapper articuloTelaMapper;
    private final ArticuloPrendaMapper articuloPrendaMapper;
    private final ArticuloAccesorioMapper articuloAccesorioMapper;

    public Articulo toDomain(ArticuloJpaEntity entity) {
        if (entity == null) return null;
        return Articulo.builder()
                .idArticulo(entity.getIdArticulo())
                .codigoArticulo(entity.getCodigoArticulo())
                .nombreArticulo(entity.getNombreArticulo())
                .descripcionArticulo(entity.getDescripcionArticulo())
                .codigoBarra(entity.getCodigoBarra())
                .tipoArticulo(entity.getTipoArticulo())
                .procedencia(entity.getProcedencia())
                .lotes(entity.getLotes())
                .seriales(entity.getSeriales())
                .compras(entity.getCompras())
                .comision(entity.getComision())
                .activo(entity.getActivo())
                .categoria(categoriaMapper.toDomain(entity.getCategoria()))
                .subCategoria(subCategoriaMapper.toDomain(entity.getSubCategoria()))
                .unidadMedida(unidadMedidaMapper.toDomain(entity.getUnidadMedida()))
                .detalleTela(articuloTelaMapper.toDomain(entity.getDetalleTela()))
                .detallePrenda(articuloPrendaMapper.toDomain(entity.getDetallePrenda()))
                .detalleAccesorio(articuloAccesorioMapper.toDomain(entity.getDetalleAccesorio()))
                .build();
    }

    public ArticuloJpaEntity toEntity(Articulo domain) {
        if (domain == null) return null;
        ArticuloJpaEntity entity = ArticuloJpaEntity.builder()
                .idArticulo(domain.getIdArticulo())
                .codigoArticulo(domain.getCodigoArticulo())
                .nombreArticulo(domain.getNombreArticulo())
                .descripcionArticulo(domain.getDescripcionArticulo())
                .codigoBarra(domain.getCodigoBarra())
                .tipoArticulo(domain.getTipoArticulo())
                .procedencia(domain.getProcedencia())
                .lotes(domain.getLotes() != null ? domain.getLotes() : false)
                .seriales(domain.getSeriales() != null ? domain.getSeriales() : false)
                .compras(domain.getCompras() != null ? domain.getCompras() : true)
                .comision(domain.getComision())
                .activo(domain.getActivo() != null ? domain.getActivo() : true)
                .categoria(categoriaMapper.toEntity(domain.getCategoria()))
                .subCategoria(subCategoriaMapper.toEntity(domain.getSubCategoria()))
                .unidadMedida(unidadMedidaMapper.toEntity(domain.getUnidadMedida()))
                .detalleTela(articuloTelaMapper.toEntity(domain.getDetalleTela()))
                .detallePrenda(articuloPrendaMapper.toEntity(domain.getDetallePrenda()))
                .detalleAccesorio(articuloAccesorioMapper.toEntity(domain.getDetalleAccesorio()))
                .build();
        // Back-references para mantener la relación OneToOne con @MapsId
        if (entity.getDetalleTela() != null) entity.getDetalleTela().setArticulo(entity);
        if (entity.getDetallePrenda() != null) entity.getDetallePrenda().setArticulo(entity);
        if (entity.getDetalleAccesorio() != null) entity.getDetalleAccesorio().setArticulo(entity);
        return entity;
    }

    public ArticuloDTO toDTO(Articulo domain) {
        if (domain == null) return null;
        return ArticuloDTO.builder()
                .idArticulo(domain.getIdArticulo())
                .codigoArticulo(domain.getCodigoArticulo())
                .nombreArticulo(domain.getNombreArticulo())
                .descripcionArticulo(domain.getDescripcionArticulo())
                .codigoBarra(domain.getCodigoBarra())
                .tipoArticulo(domain.getTipoArticulo())
                .procedencia(domain.getProcedencia())
                .lotes(domain.getLotes())
                .seriales(domain.getSeriales())
                .compras(domain.getCompras())
                .comision(domain.getComision())
                .activo(domain.getActivo())
                .idCategoria(domain.getCategoria() != null ? domain.getCategoria().getIdCategoria() : null)
                .idSubCategoria(domain.getSubCategoria() != null ? domain.getSubCategoria().getIdSubCategoria() : null)
                .idUnidadMedida(domain.getUnidadMedida() != null ? domain.getUnidadMedida().getIdUnidadMedida() : null)
                .detalleTela(articuloTelaMapper.toDTO(domain.getDetalleTela()))
                .detallePrenda(articuloPrendaMapper.toDTO(domain.getDetallePrenda()))
                .detalleAccesorio(articuloAccesorioMapper.toDTO(domain.getDetalleAccesorio()))
                .build();
    }
}