package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.TipoArticuloJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoArticuloJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticuloMapper {

    private final CategoriaTelaMapper categoriaTelaMapper;
    private final SubCategoriaTelaMapper subCategoriaTelaMapper;
    private final ArticuloTelaMapper articuloTelaMapper;
    private final ArticuloPrendaMapper articuloPrendaMapper;
    private final ArticuloAccesorioMapper articuloAccesorioMapper;
    private final TipoArticuloJpaRepository tipoArticuloJpaRepository;

    public Articulo toDomain(ArticuloJpaEntity entity) {
        if (entity == null) return null;
        return Articulo.builder()
                .idArticulo(entity.getIdArticulo())
                .codigoArticulo(entity.getCodigoArticulo())
                .nombreArticulo(entity.getNombreArticulo())
                .descripcionArticulo(entity.getDescripcionArticulo())
                .codigoBarra(entity.getCodigoBarra())
                .tipoArticulo(entity.getTipoArticulo() != null ? entity.getTipoArticulo().getCodigo() : null)
                .activo(entity.getActivo())
                .stockActual(entity.getStockActual() != null ? entity.getStockActual() : java.math.BigDecimal.ZERO)
                .categoriaTela(categoriaTelaMapper.toDomain(entity.getCategoriaTela()))
                .subCategoriaTela(subCategoriaTelaMapper.toDomain(entity.getSubCategoriaTela()))
                .detalleTela(articuloTelaMapper.toDomain(entity.getDetalleTela()))
                .detallePrenda(articuloPrendaMapper.toDomain(entity.getDetallePrenda()))
                .detalleAccesorio(articuloAccesorioMapper.toDomain(entity.getDetalleAccesorio()))
                .build();
    }

    public ArticuloJpaEntity toEntity(Articulo domain) {
        if (domain == null) return null;
        TipoArticuloJpaEntity tipoEntidad = null;
        if (domain.getTipoArticulo() != null) {
            tipoEntidad = tipoArticuloJpaRepository.findByCodigo(domain.getTipoArticulo())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "TipoArticulo no encontrado: " + domain.getTipoArticulo()));
        }
        ArticuloJpaEntity entity = ArticuloJpaEntity.builder()
                .idArticulo(domain.getIdArticulo())
                .codigoArticulo(domain.getCodigoArticulo())
                .nombreArticulo(domain.getNombreArticulo())
                .descripcionArticulo(domain.getDescripcionArticulo())
                .codigoBarra(domain.getCodigoBarra())
                .tipoArticulo(tipoEntidad)
                .activo(domain.getActivo() != null ? domain.getActivo() : true)
                .stockActual(domain.getStockActual() != null ? domain.getStockActual() : java.math.BigDecimal.ZERO)
                .categoriaTela(categoriaTelaMapper.toEntity(domain.getCategoriaTela()))
                .subCategoriaTela(subCategoriaTelaMapper.toEntity(domain.getSubCategoriaTela()))
                .detalleTela(articuloTelaMapper.toEntity(domain.getDetalleTela()))
                .detallePrenda(articuloPrendaMapper.toEntity(domain.getDetallePrenda()))
                .detalleAccesorio(articuloAccesorioMapper.toEntity(domain.getDetalleAccesorio()))
                .build();
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
                .activo(domain.getActivo())
                .stockActual(domain.getStockActual() != null ? domain.getStockActual() : java.math.BigDecimal.ZERO)
                .idCategoriaTela(domain.getCategoriaTela() != null ? domain.getCategoriaTela().getIdCategoriaTela() : null)
                .idSubCategoriaTela(domain.getSubCategoriaTela() != null ? domain.getSubCategoriaTela().getIdSubCategoriaTela() : null)
                .detalleTela(articuloTelaMapper.toDTO(domain.getDetalleTela()))
                .detallePrenda(articuloPrendaMapper.toDTO(domain.getDetallePrenda()))
                .detalleAccesorio(articuloAccesorioMapper.toDTO(domain.getDetalleAccesorio()))
                .build();
    }
}
