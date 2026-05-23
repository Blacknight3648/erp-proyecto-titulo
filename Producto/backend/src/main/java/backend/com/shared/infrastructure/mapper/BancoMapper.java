package backend.com.shared.infrastructure.mapper;

import org.springframework.stereotype.Component;

import backend.com.shared.application.dto.BancoDTO;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.infrastructure.persistence.entity.BancoJpaEntity;

@Component
public class BancoMapper {

    public Banco toDomain(BancoJpaEntity entity) {
        if (entity == null)
            return null;
        return Banco.builder()
                .bancoId(entity.getBancoId())
                .nombreBanco(entity.getNombreBanco())
                .codigoBanco(entity.getCodigoBanco())
                .build();
    }

    public BancoJpaEntity toEntity(Banco domain) {
        if (domain == null)
            return null;
        BancoJpaEntity entity = new BancoJpaEntity();
        entity.setBancoId(domain.getBancoId());
        entity.setNombreBanco(domain.getNombreBanco());
        entity.setCodigoBanco(domain.getCodigoBanco());
        return entity;
    }

    public BancoDTO toDTO(Banco domain) {
        if (domain == null)
            return null;
        return BancoDTO.builder()
                .bancoId(domain.getBancoId())
                .nombreBanco(domain.getNombreBanco())
                .codigoBanco(domain.getCodigoBanco())
                .build();
    }

    public Banco toDomain(BancoDTO dto) {
        if (dto == null)
            return null;
        return Banco.builder()
                .bancoId(dto.getBancoId())
                .nombreBanco(dto.getNombreBanco())
                .codigoBanco(dto.getCodigoBanco())
                .build();
    }

}