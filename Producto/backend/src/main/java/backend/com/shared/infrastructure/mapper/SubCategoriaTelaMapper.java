package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.SubCategoriaTelaDTO;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.domain.model.SubCategoriaTela;
import backend.com.shared.infrastructure.persistence.entity.SubCategoriaTelaJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubCategoriaTelaMapper {

    private final CategoriaTelaMapper categoriaTelaMapper;

    public SubCategoriaTela toDomain(SubCategoriaTelaJpaEntity entity) {
        if (entity == null) return null;
        return SubCategoriaTela.builder()
                .idSubCategoriaTela(entity.getIdSubCategoriaTela())
                .codigoSubCategoriaTela(entity.getCodigoSubCategoriaTela())
                .nombreSubCategoriaTela(entity.getNombreSubCategoriaTela())
                .categoriaTela(categoriaTelaMapper.toDomain(entity.getCategoriaTela()))
                .build();
    }

    public SubCategoriaTelaJpaEntity toEntity(SubCategoriaTela domain) {
        if (domain == null) return null;
        return SubCategoriaTelaJpaEntity.builder()
                .idSubCategoriaTela(domain.getIdSubCategoriaTela())
                .codigoSubCategoriaTela(domain.getCodigoSubCategoriaTela())
                .nombreSubCategoriaTela(domain.getNombreSubCategoriaTela())
                .categoriaTela(categoriaTelaMapper.toEntity(domain.getCategoriaTela()))
                .build();
    }

    public SubCategoriaTelaDTO toDTO(SubCategoriaTela domain) {
        if (domain == null) return null;
        CategoriaTela cat = domain.getCategoriaTela();
        return SubCategoriaTelaDTO.builder()
                .idSubCategoriaTela(domain.getIdSubCategoriaTela())
                .codigoSubCategoriaTela(domain.getCodigoSubCategoriaTela())
                .nombreSubCategoriaTela(domain.getNombreSubCategoriaTela())
                .idCategoriaTela(cat != null ? cat.getIdCategoriaTela() : null)
                .codigoCategoriaTela(cat != null ? cat.getCodigoCategoriaTela() : null)
                .nombreCategoriaTela(cat != null ? cat.getNombreCategoriaTela() : null)
                .build();
    }

    public SubCategoriaTela toDomain(SubCategoriaTelaDTO dto) {
        if (dto == null) return null;
        CategoriaTela categoriaTela = dto.getIdCategoriaTela() != null
                ? CategoriaTela.builder().idCategoriaTela(dto.getIdCategoriaTela()).build()
                : null;
        return SubCategoriaTela.builder()
                .idSubCategoriaTela(dto.getIdSubCategoriaTela())
                .codigoSubCategoriaTela(dto.getCodigoSubCategoriaTela())
                .nombreSubCategoriaTela(dto.getNombreSubCategoriaTela())
                .categoriaTela(categoriaTela)
                .build();
    }
}
