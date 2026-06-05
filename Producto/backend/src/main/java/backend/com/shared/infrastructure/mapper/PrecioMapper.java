package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.PrecioDTO;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.domain.model.Moneda;
import backend.com.shared.domain.model.Precio;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.MonedaJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.PrecioJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ArticuloJpaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.MonedaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrecioMapper {

    private final ArticuloMapper articuloMapper;
    private final MonedaMapper monedaMapper;
    private final ArticuloJpaRepository articuloJpaRepository;
    private final MonedaJpaRepository monedaJpaRepository;

    public Precio toDomain(PrecioJpaEntity entity) {
        if (entity == null) return null;
        return Precio.builder()
                .idPrecio(entity.getIdPrecio())
                .articulo(articuloMapper.toDomain(entity.getArticulo()))
                .moneda(monedaMapper.toDomain(entity.getMoneda()))
                .tipoPrecio(entity.getTipoPrecio())
                .valor(entity.getValor())
                .build();
    }

    public PrecioJpaEntity toEntity(Precio domain) {
        if (domain == null) return null;
        ArticuloJpaEntity articulo = resolverArticulo(domain.getArticulo());
        MonedaJpaEntity moneda = resolverMoneda(domain.getMoneda());
        return PrecioJpaEntity.builder()
                .idPrecio(domain.getIdPrecio())
                .articulo(articulo)
                .moneda(moneda)
                .tipoPrecio(domain.getTipoPrecio())
                .valor(domain.getValor())
                .build();
    }

    public PrecioDTO toDTO(Precio domain) {
        if (domain == null) return null;
        Articulo articulo = domain.getArticulo();
        Moneda moneda = domain.getMoneda();
        return PrecioDTO.builder()
                .idPrecio(domain.getIdPrecio())
                .idArticulo(articulo != null ? articulo.getIdArticulo() : null)
                .codigoArticulo(articulo != null ? articulo.getCodigoArticulo() : null)
                .idMoneda(moneda != null ? moneda.getIdMoneda() : null)
                .codigoMoneda(moneda != null ? moneda.getCodigoMoneda() : null)
                .simboloMoneda(moneda != null ? moneda.getSimbolo() : null)
                .tipoPrecio(domain.getTipoPrecio())
                .valor(domain.getValor())
                .build();
    }

    private ArticuloJpaEntity resolverArticulo(Articulo articulo) {
        if (articulo == null || articulo.getIdArticulo() == null) return null;
        return articuloJpaRepository.findById(articulo.getIdArticulo()).orElse(null);
    }

    private MonedaJpaEntity resolverMoneda(Moneda moneda) {
        if (moneda == null || moneda.getIdMoneda() == null) return null;
        return monedaJpaRepository.findById(moneda.getIdMoneda()).orElse(null);
    }
}