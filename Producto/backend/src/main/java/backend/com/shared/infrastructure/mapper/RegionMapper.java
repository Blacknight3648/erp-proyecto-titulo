package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.RegionDTO;
import backend.com.shared.domain.model.Region;
import backend.com.shared.infrastructure.persistence.entity.RegionJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionMapper {

    private final PaisMapper paisMapper;

    public Region toDomain(RegionJpaEntity entity) {
        if (entity == null)
            return null;
        return Region.builder()
                .regionId(entity.getRegionId())
                .nombreRegion(entity.getNombreRegion())
                .pais(paisMapper.toDomain(entity.getPais()))
                .build();
    }

    public RegionJpaEntity toEntity(Region domain) {
        if (domain == null)
            return null;
        RegionJpaEntity entity = new RegionJpaEntity();
        entity.setRegionId(domain.getRegionId());
        entity.setNombreRegion(domain.getNombreRegion());
        entity.setPais(paisMapper.toEntity(domain.getPais()));
        return entity;
    }

    public RegionDTO toDTO(Region domain) {
        if (domain == null)
            return null;
        return RegionDTO.builder()
                .regionId(domain.getRegionId())
                .nombreRegion(domain.getNombreRegion())
                .pais(paisMapper.toDTO(domain.getPais()))
                .build();
    }

    public Region toDomain(RegionDTO dto) {
        if (dto == null)
            return null;
        return Region.builder()
                .regionId(dto.getRegionId())
                .nombreRegion(dto.getNombreRegion())
                .pais(paisMapper.toDomain(dto.getPais()))
                .build();
    }
}