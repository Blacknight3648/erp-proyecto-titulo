package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ArticuloTelaDTO;
import backend.com.shared.domain.model.ArticuloTela;
import backend.com.shared.infrastructure.persistence.entity.ArticuloTelaJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticuloTelaMapper {

    private final FamiliaTelaMapper familiaTelaMapper;
    private final ClasificacionTecnicaMapper clasificacionTecnicaMapper;
    private final ComposicionMapper composicionMapper;
    private final GramajeTelaMapper gramajeTelaMapper;
    private final ColorTelaMapper colorTelaMapper;
    private final AtributoTecnicoMapper atributoTecnicoMapper;

    public ArticuloTela toDomain(ArticuloTelaJpaEntity entity) {
        if (entity == null) return null;
        return ArticuloTela.builder()
                .id(entity.getId())
                .familiaTela(familiaTelaMapper.toDomain(entity.getFamiliaTela()))
                .clasificacionTecnica(clasificacionTecnicaMapper.toDomain(entity.getClasificacionTecnica()))
                .composicion(composicionMapper.toDomain(entity.getComposicion()))
                .gramaje(gramajeTelaMapper.toDomain(entity.getGramaje()))
                .abreviaturasHistoricas(entity.getAbreviaturasHistoricas())
                .usoTipico(entity.getUsoTipico())
                .observacionProveedor(entity.getObservacionProveedor())
                .colores(entity.getColores() == null ? null
                        : entity.getColores().stream().map(colorTelaMapper::toDomain).toList())
                .atributosTecnicos(entity.getAtributosTecnicos() == null ? null
                        : entity.getAtributosTecnicos().stream().map(atributoTecnicoMapper::toDomain).toList())
                .build();
    }

    public ArticuloTelaJpaEntity toEntity(ArticuloTela domain) {
        if (domain == null) return null;
        return ArticuloTelaJpaEntity.builder()
                .id(domain.getId())
                .familiaTela(familiaTelaMapper.toEntity(domain.getFamiliaTela()))
                .clasificacionTecnica(clasificacionTecnicaMapper.toEntity(domain.getClasificacionTecnica()))
                .composicion(composicionMapper.toEntity(domain.getComposicion()))
                .gramaje(gramajeTelaMapper.toEntity(domain.getGramaje()))
                .abreviaturasHistoricas(domain.getAbreviaturasHistoricas())
                .usoTipico(domain.getUsoTipico())
                .observacionProveedor(domain.getObservacionProveedor())
                .colores(domain.getColores() == null ? null
                        : domain.getColores().stream().map(colorTelaMapper::toEntity).toList())
                .atributosTecnicos(domain.getAtributosTecnicos() == null ? null
                        : domain.getAtributosTecnicos().stream().map(atributoTecnicoMapper::toEntity).toList())
                .build();
    }

    public ArticuloTelaDTO toDTO(ArticuloTela domain) {
        if (domain == null) return null;
        return ArticuloTelaDTO.builder()
                .id(domain.getId())
                .familiaTela(familiaTelaMapper.toDTO(domain.getFamiliaTela()))
                .clasificacionTecnica(clasificacionTecnicaMapper.toDTO(domain.getClasificacionTecnica()))
                .composicion(composicionMapper.toDTO(domain.getComposicion()))
                .gramaje(gramajeTelaMapper.toDTO(domain.getGramaje()))
                .abreviaturasHistoricas(domain.getAbreviaturasHistoricas())
                .usoTipico(domain.getUsoTipico())
                .observacionProveedor(domain.getObservacionProveedor())
                .colores(domain.getColores() == null ? null
                        : domain.getColores().stream().map(colorTelaMapper::toDTO).toList())
                .atributosTecnicos(domain.getAtributosTecnicos() == null ? null
                        : domain.getAtributosTecnicos().stream().map(atributoTecnicoMapper::toDTO).toList())
                .build();
    }

    public ArticuloTela toDomain(ArticuloTelaDTO dto) {
        if (dto == null) return null;
        List<backend.com.shared.domain.model.ColorTela> colores = dto.getColores() == null ? null
                : dto.getColores().stream().map(colorTelaMapper::toDomain).toList();
        List<backend.com.shared.domain.model.AtributoTecnico> atributos = dto.getAtributosTecnicos() == null ? null
                : dto.getAtributosTecnicos().stream().map(atributoTecnicoMapper::toDomain).toList();
        return ArticuloTela.builder()
                .id(dto.getId())
                .familiaTela(familiaTelaMapper.toDomain(dto.getFamiliaTela()))
                .clasificacionTecnica(clasificacionTecnicaMapper.toDomain(dto.getClasificacionTecnica()))
                .composicion(composicionMapper.toDomain(dto.getComposicion()))
                .gramaje(gramajeTelaMapper.toDomain(dto.getGramaje()))
                .abreviaturasHistoricas(dto.getAbreviaturasHistoricas())
                .usoTipico(dto.getUsoTipico())
                .observacionProveedor(dto.getObservacionProveedor())
                .colores(colores)
                .atributosTecnicos(atributos)
                .build();
    }
}