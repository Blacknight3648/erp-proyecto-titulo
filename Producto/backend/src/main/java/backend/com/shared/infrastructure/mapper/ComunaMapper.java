package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.Comuna;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ComunaMapper {

    private final RegionMapper regionMapper;

    public Comuna toDomain(backend.com.shared.infrastructure.persistence.entity.Comuna entity) {
        if (entity == null) return null;
        return Comuna.builder()
                .comunaId(entity.getComunaId())
                .nombreComuna(entity.getNombreComuna())
                .region(regionMapper.toDomain(entity.getRegion()))
                .build();
    }

    public backend.com.shared.infrastructure.persistence.entity.Comuna toEntity(Comuna domain) {
        if (domain == null) return null;
        backend.com.shared.infrastructure.persistence.entity.Comuna entity =
                new backend.com.shared.infrastructure.persistence.entity.Comuna();
        entity.setComunaId(domain.getComunaId());
        entity.setNombreComuna(domain.getNombreComuna());
        entity.setRegion(regionMapper.toEntity(domain.getRegion()));
        return entity;
    }
}
