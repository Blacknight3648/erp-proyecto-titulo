package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.FamiliaTelaDTO;
import backend.com.shared.domain.model.FamiliaTela;
import backend.com.shared.infrastructure.persistence.entity.FamiliaTelaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class FamiliaTelaMapper {

    public FamiliaTela toDomain(FamiliaTelaJpaEntity entity) {
        if (entity == null) return null;
        return FamiliaTela.builder()
                .idFamiliaTela(entity.getIdFamiliaTela())
                .codigoFamilia(entity.getCodigoFamilia())
                .nombreFamilia(entity.getNombreFamilia())
                .build();
    }

    public FamiliaTelaJpaEntity toEntity(FamiliaTela domain) {
        if (domain == null) return null;
        return FamiliaTelaJpaEntity.builder()
                .idFamiliaTela(domain.getIdFamiliaTela())
                .codigoFamilia(domain.getCodigoFamilia())
                .nombreFamilia(domain.getNombreFamilia())
                .build();
    }

    public FamiliaTelaDTO toDTO(FamiliaTela domain) {
        if (domain == null) return null;
        return FamiliaTelaDTO.builder()
                .idFamiliaTela(domain.getIdFamiliaTela())
                .codigoFamilia(domain.getCodigoFamilia())
                .nombreFamilia(domain.getNombreFamilia())
                .build();
    }

    public FamiliaTela toDomain(FamiliaTelaDTO dto) {
        if (dto == null) return null;
        return FamiliaTela.builder()
                .idFamiliaTela(dto.getIdFamiliaTela())
                .codigoFamilia(dto.getCodigoFamilia())
                .nombreFamilia(dto.getNombreFamilia())
                .build();
    }
}