package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.infrastructure.persistence.entity.ArticuloCamposPlantillaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.CamposPlantillaJpaEntity;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.infrastructure.mapper.ArticuloMapper;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ArticuloJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticuloCamposPlantillaMapper {

    private final CamposPlantillaMapper plantillaMapper;
    private final ArticuloMapper articuloMapper;
    private final ArticuloJpaRepository articuloJpaRepository;

    public ArticuloCamposPlantilla toDomain(ArticuloCamposPlantillaJpaEntity entity) {
        if (entity == null)
            return null;
        return ArticuloCamposPlantilla.builder()
                .idModeloPlantilla(entity.getIdModeloPlantilla())
                .articulo(articuloMapper.toDomain(entity.getArticulo()))
                .plantilla(plantillaMapper.toDomain(entity.getPlantilla()))
                .build();
    }

    public ArticuloCamposPlantillaJpaEntity toEntity(ArticuloCamposPlantilla domain) {
        if (domain == null)
            return null;
        ArticuloJpaEntity articulo = resolverArticulo(domain.getArticulo());
        CamposPlantillaJpaEntity plantilla = domain.getPlantilla() != null
                ? CamposPlantillaJpaEntity.builder()
                        .idPlantilla(domain.getPlantilla().getIdPlantilla())
                        .nombreCampo(domain.getPlantilla().getNombreCampo())
                        .build()
                : null;
        return ArticuloCamposPlantillaJpaEntity.builder()
                .idModeloPlantilla(domain.getIdModeloPlantilla())
                .articulo(articulo)
                .plantilla(plantilla)
                .build();
    }

    public ArticuloCamposPlantillaDTO toDTO(ArticuloCamposPlantilla domain) {
        if (domain == null)
            return null;
        CamposPlantilla p = domain.getPlantilla();
        Articulo a = domain.getArticulo();
        return ArticuloCamposPlantillaDTO.builder()
                .idModeloPlantilla(domain.getIdModeloPlantilla())
                .idArticulo(a != null ? a.getIdArticulo() : null)
                .idPlantilla(p != null ? p.getIdPlantilla() : null)
                .nombreCampo(p != null ? p.getNombreCampo() : null)
                .build();
    }

    private ArticuloJpaEntity resolverArticulo(Articulo articulo) {
        if (articulo == null || articulo.getIdArticulo() == null)
            return null;
        return articuloJpaRepository.findById(articulo.getIdArticulo()).orElse(null);
    }
}
