package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.GramajeTelaDTO;
import backend.com.shared.domain.model.GramajeTela;
import backend.com.shared.infrastructure.persistence.entity.GramajeTelaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class GramajeTelaMapper {

    public GramajeTela toDomain(GramajeTelaJpaEntity entity) {
        if (entity == null) return null;
        return GramajeTela.builder()
                .idGramaje(entity.getIdGramaje())
                .codigoGramaje(entity.getCodigoGramaje())
                .valorGramosM2(entity.getValorGramosM2())
                .categoriaVestuario(entity.getCategoriaVestuario())
                .build();
    }

    public GramajeTelaJpaEntity toEntity(GramajeTela domain) {
        if (domain == null) return null;
        return GramajeTelaJpaEntity.builder()
                .idGramaje(domain.getIdGramaje())
                .codigoGramaje(domain.getCodigoGramaje())
                .valorGramosM2(domain.getValorGramosM2())
                .categoriaVestuario(domain.getCategoriaVestuario())
                .build();
    }

    public GramajeTelaDTO toDTO(GramajeTela domain) {
        if (domain == null) return null;
        return GramajeTelaDTO.builder()
                .idGramaje(domain.getIdGramaje())
                .codigoGramaje(domain.getCodigoGramaje())
                .valorGramosM2(domain.getValorGramosM2())
                .categoriaVestuario(domain.getCategoriaVestuario())
                .build();
    }

    public GramajeTela toDomain(GramajeTelaDTO dto) {
        if (dto == null) return null;
        return GramajeTela.builder()
                .idGramaje(dto.getIdGramaje())
                .codigoGramaje(dto.getCodigoGramaje())
                .valorGramosM2(dto.getValorGramosM2())
                .categoriaVestuario(dto.getCategoriaVestuario())
                .build();
    }
}