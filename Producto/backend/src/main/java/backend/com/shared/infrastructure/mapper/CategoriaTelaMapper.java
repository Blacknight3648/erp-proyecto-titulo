package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.CategoriaTelaDTO;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.infrastructure.persistence.entity.CategoriaTelaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CategoriaTelaMapper {

    public CategoriaTela toDomain(CategoriaTelaJpaEntity entity) {
        if (entity == null) return null;
        return CategoriaTela.builder()
                .idCategoriaTela(entity.getIdCategoriaTela())
                .codigoCategoriaTela(entity.getCodigoCategoriaTela())
                .nombreCategoriaTela(entity.getNombreCategoriaTela())
                .build();
    }

    public CategoriaTelaJpaEntity toEntity(CategoriaTela domain) {
        if (domain == null) return null;
        return CategoriaTelaJpaEntity.builder()
                .idCategoriaTela(domain.getIdCategoriaTela())
                .codigoCategoriaTela(domain.getCodigoCategoriaTela())
                .nombreCategoriaTela(domain.getNombreCategoriaTela())
                .build();
    }

    public CategoriaTelaDTO toDTO(CategoriaTela domain) {
        if (domain == null) return null;
        return CategoriaTelaDTO.builder()
                .idCategoriaTela(domain.getIdCategoriaTela())
                .codigoCategoriaTela(domain.getCodigoCategoriaTela())
                .nombreCategoriaTela(domain.getNombreCategoriaTela())
                .build();
    }

    public CategoriaTela toDomain(CategoriaTelaDTO dto) {
        if (dto == null) return null;
        return CategoriaTela.builder()
                .idCategoriaTela(dto.getIdCategoriaTela())
                .codigoCategoriaTela(dto.getCodigoCategoriaTela())
                .nombreCategoriaTela(dto.getNombreCategoriaTela())
                .build();
    }
}
