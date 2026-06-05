package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.AtributoTecnicoDTO;
import backend.com.shared.domain.model.AtributoTecnico;
import backend.com.shared.infrastructure.persistence.entity.AtributoTecnicoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class AtributoTecnicoMapper {

    public AtributoTecnico toDomain(AtributoTecnicoJpaEntity entity) {
        if (entity == null) return null;
        return AtributoTecnico.builder()
                .idAtributo(entity.getIdAtributo())
                .codigoAtributo(entity.getCodigoAtributo())
                .clasificacion(entity.getClasificacion())
                .descripcionTecnica(entity.getDescripcionTecnica())
                .impactoErp(entity.getImpactoErp())
                .build();
    }

    public AtributoTecnicoJpaEntity toEntity(AtributoTecnico domain) {
        if (domain == null) return null;
        return AtributoTecnicoJpaEntity.builder()
                .idAtributo(domain.getIdAtributo())
                .codigoAtributo(domain.getCodigoAtributo())
                .clasificacion(domain.getClasificacion())
                .descripcionTecnica(domain.getDescripcionTecnica())
                .impactoErp(domain.getImpactoErp())
                .build();
    }

    public AtributoTecnicoDTO toDTO(AtributoTecnico domain) {
        if (domain == null) return null;
        return AtributoTecnicoDTO.builder()
                .idAtributo(domain.getIdAtributo())
                .codigoAtributo(domain.getCodigoAtributo())
                .clasificacion(domain.getClasificacion())
                .descripcionTecnica(domain.getDescripcionTecnica())
                .impactoErp(domain.getImpactoErp())
                .build();
    }

    public AtributoTecnico toDomain(AtributoTecnicoDTO dto) {
        if (dto == null) return null;
        return AtributoTecnico.builder()
                .idAtributo(dto.getIdAtributo())
                .codigoAtributo(dto.getCodigoAtributo())
                .clasificacion(dto.getClasificacion())
                .descripcionTecnica(dto.getDescripcionTecnica())
                .impactoErp(dto.getImpactoErp())
                .build();
    }
}