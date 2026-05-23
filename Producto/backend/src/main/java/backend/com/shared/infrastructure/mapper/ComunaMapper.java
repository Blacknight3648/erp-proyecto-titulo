package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ComunaDTO;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.infrastructure.persistence.entity.ComunaJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComunaMapper {

    private final RegionMapper regionMapper;

    public Comuna toDomain(ComunaJpaEntity entity) {
        if (entity == null)
            return null;
        return Comuna.builder()
                .comunaId(entity.getComunaId())
                .nombreComuna(entity.getNombreComuna())
                .region(regionMapper.toDomain(entity.getRegion()))
                .build();
    }

    public ComunaJpaEntity toEntity(Comuna domain) {
        if (domain == null)
            return null;
        ComunaJpaEntity entity = new ComunaJpaEntity();
        entity.setComunaId(domain.getComunaId());
        entity.setNombreComuna(domain.getNombreComuna());
        entity.setRegion(regionMapper.toEntity(domain.getRegion()));
        return entity;
    }

    public ComunaDTO toDTO(Comuna domain) {
        if (domain == null)
            return null;
        return ComunaDTO.builder()
                .comunaId(domain.getComunaId())
                .nombreComuna(domain.getNombreComuna())
                .region(regionMapper.toDTO(domain.getRegion()))
                .build();
    }

    public Comuna toDomain(ComunaDTO dto) {
        if (dto == null)
            return null;
        return Comuna.builder()
                .comunaId(dto.getComunaId())
                .nombreComuna(dto.getNombreComuna())
                .region(regionMapper.toDomain(dto.getRegion()))
                .build();
    }

}