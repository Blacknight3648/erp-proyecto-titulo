package backend.com.shared.infrastructure.mapper;

import org.springframework.stereotype.Component;

@Component
public class BancoMapper {

    public Banco toDomain(backend.com.shared.infrastructure.persistence.entity.Banco entity) {
        if (entity == null) return null;
        return backend.com.shared.domain.model.Banco.builder()
                .bancoId(entity.getBancoId())
                .nombreBanco(entity.getNombreBanco())
                .codigoBanco(entity.getCodigoBanco())
                .build();
    }

    public backend.com.shared.infrastructure.persistence.entity.Banco toEntity(
            backend.com.shared.domain.model.Banco domain) {
        if (domain == null) return null;
        backend.com.shared.infrastructure.persistence.entity.Banco entity =
                new backend.com.shared.infrastructure.persistence.entity.Banco();
        entity.setBancoId(domain.getBancoId());
        entity.setNombreBanco(domain.getNombreBanco());
        entity.setCodigoBanco(domain.getCodigoBanco());
        return entity;
    }
}
