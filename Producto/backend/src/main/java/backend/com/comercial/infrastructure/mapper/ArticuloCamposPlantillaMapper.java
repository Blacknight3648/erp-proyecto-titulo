package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.infrastructure.persistence.entity.ArticuloCamposPlantillaJpaEntity;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.infrastructure.mapper.ArticuloMapper;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ArticuloJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticuloCamposPlantillaMapper {

    private final ArticuloMapper articuloMapper;
    private final ArticuloJpaRepository articuloJpaRepository;

    public ArticuloCamposPlantilla toDomain(ArticuloCamposPlantillaJpaEntity entity) {
        if (entity == null)
            return null;
        return ArticuloCamposPlantilla.builder()
                .idModeloPlantilla(entity.getIdModeloPlantilla())
                .articulo(articuloMapper.toDomain(entity.getArticulo()))
                .campos(parseCampos(entity.getCampos()))
                .build();
    }

    public ArticuloCamposPlantillaJpaEntity toEntity(ArticuloCamposPlantilla domain) {
        if (domain == null)
            return null;
        return ArticuloCamposPlantillaJpaEntity.builder()
                .idModeloPlantilla(domain.getIdModeloPlantilla())
                .articulo(resolverArticulo(domain.getArticulo()))
                .campos(joinCampos(domain.getCampos()))
                .build();
    }

    public ArticuloCamposPlantillaDTO toDTO(ArticuloCamposPlantilla domain) {
        if (domain == null)
            return null;
        Articulo a = domain.getArticulo();
        return ArticuloCamposPlantillaDTO.builder()
                .idModeloPlantilla(domain.getIdModeloPlantilla())
                .idArticulo(a != null ? a.getIdArticulo() : null)
                .nombreArticulo(a != null ? a.getNombreArticulo() : null)
                .camposPlantilla(domain.getCampos())
                .build();
    }

    /** "forro,cuello,mangas" -> ["forro","cuello","mangas"] (trim, descarta vacíos). */
    public static List<String> parseCampos(String csv) {
        if (csv == null || csv.isBlank())
            return List.of();
        return Arrays.stream(csv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    /** ["forro","cuello"] -> "forro,cuello". */
    public static String joinCampos(List<String> campos) {
        if (campos == null || campos.isEmpty())
            return "";
        return String.join(",", campos);
    }

    private ArticuloJpaEntity resolverArticulo(Articulo articulo) {
        if (articulo == null || articulo.getIdArticulo() == null)
            return null;
        return articuloJpaRepository.findById(articulo.getIdArticulo()).orElse(null);
    }
}
