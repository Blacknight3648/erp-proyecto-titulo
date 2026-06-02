package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.TipoDireccionDTO;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.infrastructure.persistence.entity.TipoDireccionJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoDireccionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TipoDireccionMapper {

    private final TipoDireccionJpaRepository tipoDireccionJpaRepository;

    public TipoDireccion toDomain(TipoDireccionJpaEntity entity) {
        if (entity == null)
            return null;
        return TipoDireccion.builder()
                .tipoDireccionId(entity.getTipoDireccionId())
                .descripcion(entity.getDescripcion())
                .build();
    }

    public TipoDireccionJpaEntity toEntity(TipoDireccion domain) {
        if (domain == null)
            return null;

        if (domain.getTipoDireccionId() != null) {
            return tipoDireccionJpaRepository.findById(domain.getTipoDireccionId())
                    .orElseGet(() -> {
                        TipoDireccionJpaEntity entity = new TipoDireccionJpaEntity();
                        entity.setTipoDireccionId(domain.getTipoDireccionId());
                        entity.setDescripcion(domain.getDescripcion());
                        return entity;
                    });
        }

        TipoDireccionJpaEntity entity = new TipoDireccionJpaEntity();
        entity.setTipoDireccionId(domain.getTipoDireccionId());
        entity.setDescripcion(domain.getDescripcion());
        return entity;
    }

    public TipoDireccionDTO toDTO(TipoDireccion domain) {
        if (domain == null)
            return null;
        return TipoDireccionDTO.builder()
                .tipoDireccionId(domain.getTipoDireccionId())
                .descripcion(domain.getDescripcion())
                .build();
    }

    public TipoDireccion toDomain(TipoDireccionDTO dto) {
        if (dto == null)
            return null;
        return TipoDireccion.builder()
                .tipoDireccionId(dto.getTipoDireccionId())
                .descripcion(dto.getDescripcion())
                .build();
    }

}
