package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.PaisDTO;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.infrastructure.persistence.entity.PaisJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.PaisJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaisMapper {

    private final PaisJpaRepository paisJpaRepository;

    public Pais toDomain(PaisJpaEntity entity) {
        if (entity == null)
            return null;
        return Pais.builder()
                .idPais(entity.getPaisId())
                .nombrePais(entity.getNombrePais())
                .build();
    }

    public PaisJpaEntity toEntity(Pais domain) {
        if (domain == null)
            return null;

        if (domain.getIdPais() != null) {
            return paisJpaRepository.findById(domain.getIdPais().intValue())
                    .orElseGet(() -> {
                        PaisJpaEntity entity = new PaisJpaEntity();
                        entity.setPaisId(domain.getIdPais());
                        entity.setNombrePais(domain.getNombrePais());
                        return entity;
                    });
        }

        PaisJpaEntity entity = new PaisJpaEntity();
        entity.setPaisId(domain.getIdPais());
        entity.setNombrePais(domain.getNombrePais());
        return entity;
    }

    public PaisDTO toDTO(Pais domain) {
        if (domain == null)
            return null;
        return PaisDTO.builder()
                .idPais(domain.getIdPais())
                .nombrePais(domain.getNombrePais())
                .build();
    }

    public Pais toDomain(PaisDTO dto) {
        if (dto == null)
            return null;
        return Pais.builder()
                .idPais(dto.getIdPais())
                .nombrePais(dto.getNombrePais())
                .build();
    }
}