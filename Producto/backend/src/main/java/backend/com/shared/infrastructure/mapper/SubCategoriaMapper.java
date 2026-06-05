package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.SubCategoriaDTO;
import backend.com.shared.domain.model.Categoria;
import backend.com.shared.domain.model.SubCategoria;
import backend.com.shared.infrastructure.persistence.entity.SubCategoriaJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubCategoriaMapper {

    private final CategoriaMapper categoriaMapper;

    public SubCategoria toDomain(SubCategoriaJpaEntity entity) {
        if (entity == null) return null;
        return SubCategoria.builder()
                .idSubCategoria(entity.getIdSubCategoria())
                .codigoSubcategoria(entity.getCodigoSubcategoria())
                .nombreSubCategoria(entity.getNombreSubCategoria())
                .categoria(categoriaMapper.toDomain(entity.getCategoria()))
                .build();
    }

    public SubCategoriaJpaEntity toEntity(SubCategoria domain) {
        if (domain == null) return null;
        return SubCategoriaJpaEntity.builder()
                .idSubCategoria(domain.getIdSubCategoria())
                .codigoSubcategoria(domain.getCodigoSubcategoria())
                .nombreSubCategoria(domain.getNombreSubCategoria())
                .categoria(categoriaMapper.toEntity(domain.getCategoria()))
                .build();
    }

    public SubCategoriaDTO toDTO(SubCategoria domain) {
        if (domain == null) return null;
        Categoria cat = domain.getCategoria();
        return SubCategoriaDTO.builder()
                .idSubCategoria(domain.getIdSubCategoria())
                .codigoSubcategoria(domain.getCodigoSubcategoria())
                .nombreSubCategoria(domain.getNombreSubCategoria())
                .idCategoria(cat != null ? cat.getIdCategoria() : null)
                .codigoCategoria(cat != null ? cat.getCodigoCategoria() : null)
                .nombreCategoria(cat != null ? cat.getNombreCategoria() : null)
                .build();
    }

    public SubCategoria toDomain(SubCategoriaDTO dto) {
        if (dto == null) return null;
        Categoria categoria = dto.getIdCategoria() != null
                ? Categoria.builder().idCategoria(dto.getIdCategoria()).build()
                : null;
        return SubCategoria.builder()
                .idSubCategoria(dto.getIdSubCategoria())
                .codigoSubcategoria(dto.getCodigoSubcategoria())
                .nombreSubCategoria(dto.getNombreSubCategoria())
                .categoria(categoria)
                .build();
    }
}