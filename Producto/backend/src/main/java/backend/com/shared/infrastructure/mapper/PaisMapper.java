package backend.com.shared.infrastructure.mapper;

import org.springframework.stereotype.Component;

@Component
public class PaisMapper {

    public backend.com.shared.domain.model.Pais toDomain(
            backend.com.shared.infrastructure.persistence.entity.Pais entity) {
        if (entity == null) return null;
        return backend.com.shared.domain.model.Pais.builder()
                .idPais(entity.getPaisId())
                .nombrePais(entity.getNombrePais())
                .build();
    }

    public backend.com.shared.infrastructure.persistence.entity.Pais toEntity(
            backend.com.shared.domain.model.Pais domain) {
        if (domain == null) return null;
        backend.com.shared.infrastructure.persistence.entity.Pais entity =
                new backend.com.shared.infrastructure.persistence.entity.Pais();
        entity.setPaisId(domain.getIdPais());
        entity.setNombrePais(domain.getNombrePais());
        return entity;
    }
}
