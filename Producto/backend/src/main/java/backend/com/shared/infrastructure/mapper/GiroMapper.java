package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiroMapper {

    private final RubroMapper rubroMapper;

    public GiroMapper(RubroMapper rubroMapper) {
        this.rubroMapper = rubroMapper;
    }

    public Giro toDomain(GiroJpaEntity entity) {
        if (entity == null)
            return null;
        return Giro.builder()
                .giroId(entity.getGiroId())
                .descripcionGiro(entity.getDescripcionGiro())
                .codigoSii(entity.getCodigoSii())
                .nombreGiro(entity.getNombreGiro())
                .rubro(entity.getRubro() != null ? rubroMapper.toDomain(entity.getRubro()) : null)
                .build();
    }

    public GiroJpaEntity toEntity(Giro domain) {
        if (domain == null)
            return null;
        return GiroJpaEntity.builder()
                .giroId(domain.getGiroId())
                .descripcionGiro(domain.getDescripcionGiro())
                .codigoSii(domain.getCodigoSii())
                .nombreGiro(domain.getNombreGiro())
                .rubro(domain.getRubro() != null ? rubroMapper.toEntity(domain.getRubro()) : null)
                .build();
    }

    public GiroDTO toDTO(Giro domain) {
        if (domain == null)
            return null;
        return GiroDTO.builder()
                .giroId(domain.getGiroId())
                .descripcionGiro(domain.getDescripcionGiro())
                .codigoSii(domain.getCodigoSii())
                .nombreGiro(domain.getNombreGiro())
                .rubro(domain.getRubro() != null ? rubroMapper.toDTO(domain.getRubro()) : null)
                .build();
    }

    public Giro toDomain(GiroDTO dto) {
        if (dto == null)
            return null;
        return Giro.builder()
                .giroId(dto.getGiroId())
                .codigoSii(dto.getCodigoSii())
                .nombreGiro(dto.getNombreGiro())
                .descripcionGiro(dto.getDescripcionGiro())
                .rubro(dto.getRubro() != null ? rubroMapper.toDomain(dto.getRubro()) : null)
                .build();
    }

    public List<GiroDTO> toDTOList(List<Giro> giros) {
        return giros.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
