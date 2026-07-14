package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.RubroDTO;
import backend.com.shared.domain.model.Rubro;
import backend.com.shared.infrastructure.persistence.entity.RubroJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class RubroMapper {

    public Rubro toDomain(RubroJpaEntity entity) {
        if (entity == null)
            return null;
        return Rubro.builder()
                .rubroId(entity.getRubroId())
                .descripcionRubro(entity.getDescripcionRubro())
                .nombreRubro(entity.getNombreRubro())
                .siglaRubro(entity.getSiglaRubro())
                .build();
    }

    public RubroJpaEntity toEntity(Rubro domain) {
        if (domain == null)
            return null;
        return RubroJpaEntity.builder()
                .rubroId(domain.getRubroId())
                .descripcionRubro(domain.getDescripcionRubro())
                .nombreRubro(domain.getNombreRubro())
                .siglaRubro(domain.getSiglaRubro())
                .build();
    }

    public RubroDTO toDTO(Rubro domain) {
        if (domain == null)
            return null;
        return RubroDTO.builder()
                .rubroId(domain.getRubroId())
                .descripcionRubro(domain.getDescripcionRubro())
                .nombreRubro(domain.getNombreRubro())
                .siglaRubro(domain.getSiglaRubro())
                .build();
    }

    public Rubro toDomain(RubroDTO dto) {
        if (dto == null)
            return null;
        return Rubro.builder()
                .rubroId(dto.getRubroId())
                .descripcionRubro(dto.getDescripcionRubro())
                .nombreRubro(dto.getNombreRubro())
                .siglaRubro(dto.getSiglaRubro())
                .build();
    }

}
