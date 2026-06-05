package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.CategoriaDTO;
import backend.com.shared.domain.model.Categoria;
import backend.com.shared.infrastructure.persistence.entity.CategoriaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    public Categoria toDomain(CategoriaJpaEntity entity) {
        if (entity == null) return null;
        return Categoria.builder()
                .idCategoria(entity.getIdCategoria())
                .codigoCategoria(entity.getCodigoCategoria())
                .nombreCategoria(entity.getNombreCategoria())
                .build();
    }

    public CategoriaJpaEntity toEntity(Categoria domain) {
        if (domain == null) return null;
        return CategoriaJpaEntity.builder()
                .idCategoria(domain.getIdCategoria())
                .codigoCategoria(domain.getCodigoCategoria())
                .nombreCategoria(domain.getNombreCategoria())
                .build();
    }

    public CategoriaDTO toDTO(Categoria domain) {
        if (domain == null) return null;
        return CategoriaDTO.builder()
                .idCategoria(domain.getIdCategoria())
                .codigoCategoria(domain.getCodigoCategoria())
                .nombreCategoria(domain.getNombreCategoria())
                .build();
    }

    public Categoria toDomain(CategoriaDTO dto) {
        if (dto == null) return null;
        return Categoria.builder()
                .idCategoria(dto.getIdCategoria())
                .codigoCategoria(dto.getCodigoCategoria())
                .nombreCategoria(dto.getNombreCategoria())
                .build();
    }
}