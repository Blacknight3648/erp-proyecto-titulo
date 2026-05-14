package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.SiglaDTO;
import backend.com.shared.domain.model.Sigla;
import backend.com.shared.infrastructure.persistence.entity.SiglaJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SiglaMapper {

    public Sigla toDomain(SiglaJpaEntity entity) {
        if (entity == null) return null;
        return Sigla.builder()
                .siglaId(entity.getSiglaId())
                .descripcionSigla(entity.getDescripcionSigla())
                .siglaAbreviatura(entity.getSiglaAbreviatura())
                .build();
    }

    public SiglaJpaEntity toEntity(Sigla domain) {
        if (domain == null) return null;
        return SiglaJpaEntity.builder()
                .siglaId(domain.getSiglaId())
                .descripcionSigla(domain.getDescripcionSigla())
                .siglaAbreviatura(domain.getSiglaAbreviatura())
                .build();
    }

    public SiglaDTO toDTO(Sigla domain) {
        if (domain == null) return null;
        return SiglaDTO.builder()
                .siglaId(domain.getSiglaId())
                .descripcionSigla(domain.getDescripcionSigla())
                .siglaAbreviatura(domain.getSiglaAbreviatura())
                .build();
    }

    public Sigla toDomain(SiglaDTO dto) {
        if (dto == null) return null;
        return Sigla.builder()
                .siglaId(dto.getSiglaId())
                .descripcionSigla(dto.getDescripcionSigla())
                .siglaAbreviatura(dto.getSiglaAbreviatura())
                .build();
    }

    public List<SiglaDTO> toDTOList(List<Sigla> siglas) {
        return siglas.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
