package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.domain.model.TipoContacto;
import backend.com.shared.infrastructure.persistence.entity.TipoContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.TipoContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TipoContactoMapper {

    private final TipoContactoRepository tipoContactoRepository;

    public TipoContacto toDomain(TipoContactoJpaEntity entity) {
        if (entity == null)
            return null;
        return TipoContacto.builder()
                .tipoContactoId(entity.getTipoContactoId())
                .descripcionTipoContacto(entity.getDescripcionTipoContacto())
                .build();
    }

    public TipoContactoJpaEntity toEntity(TipoContacto domain) {
        if (domain == null)
            return null;

        if (domain.getTipoContactoId() != null) {
            return tipoContactoRepository.findById(domain.getTipoContactoId())
                    .orElseGet(() -> TipoContactoJpaEntity.builder()
                            .tipoContactoId(domain.getTipoContactoId())
                            .descripcionTipoContacto(domain.getDescripcionTipoContacto())
                            .build());
        }

        return TipoContactoJpaEntity.builder()
                .tipoContactoId(domain.getTipoContactoId())
                .descripcionTipoContacto(domain.getDescripcionTipoContacto())
                .build();
    }

    public TipoContactoDTO toDTO(TipoContacto domain) {
        if (domain == null)
            return null;
        TipoContactoDTO dto = new TipoContactoDTO();
        dto.setTipoContactoId(domain.getTipoContactoId());
        dto.setDescripcionTipoContacto(domain.getDescripcionTipoContacto());
        return dto;
    }

    public TipoContacto toDomain(TipoContactoDTO dto) {
        if (dto == null)
            return null;
        return TipoContacto.builder()
                .tipoContactoId(dto.getTipoContactoId())
                .descripcionTipoContacto(dto.getDescripcionTipoContacto())
                .build();
    }

}
