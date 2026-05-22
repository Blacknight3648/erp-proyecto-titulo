package backend.com.shared.infrastructure.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RegionMapper {

    private final PaisMapper paisMapper;

    public backend.com.shared.domain.model.Region toDomain(
            backend.com.shared.infrastructure.persistence.entity.Region entity) {
        if (entity == null) return null;
        return backend.com.shared.domain.model.Region.builder()
                .regionId(entity.getRegionId())
                .nombreRegion(entity.getNombreRegion())
                .pais(paisMapper.toDomain(entity.getPais()))
                .build();
    }

    public backend.com.shared.infrastructure.persistence.entity.Region toEntity(
            backend.com.shared.domain.model.Region domain) {
        if (domain == null) return null;
        backend.com.shared.infrastructure.persistence.entity.Region entity =
                new backend.com.shared.infrastructure.persistence.entity.Region();
        entity.setRegionId(domain.getRegionId());
        entity.setNombreRegion(domain.getNombreRegion());
        entity.setPais(paisMapper.toEntity(domain.getPais()));
        return entity;
    }
}
