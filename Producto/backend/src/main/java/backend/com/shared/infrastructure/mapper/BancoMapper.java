package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.BancoDTO;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.infrastructure.persistence.entity.BancoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.BancoJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BancoMapper {

    private final BancoJpaRepository bancoJpaRepository;

    public Banco toDomain(BancoJpaEntity entity) {
        if (entity == null) return null;
        return Banco.builder()
                .bancoId(entity.getBancoId())
                .nombreBanco(entity.getNombreBanco())
                .codigoBanco(entity.getCodigoBanco())
                .build();
    }

    public BancoJpaEntity toEntity(Banco domain) {
        if (domain == null) return null;
        if (domain.getBancoId() != null) {
            return bancoJpaRepository.findById(domain.getBancoId())
                    .orElseGet(() -> buildEntity(domain));
        }
        return buildEntity(domain);
    }

    public BancoDTO toDTO(Banco domain) {
        if (domain == null) return null;
        return BancoDTO.builder()
                .bancoId(domain.getBancoId())
                .nombreBanco(domain.getNombreBanco())
                .codigoBanco(domain.getCodigoBanco())
                .build();
    }

    public Banco toDomain(BancoDTO dto) {
        if (dto == null) return null;
        return Banco.builder()
                .bancoId(dto.getBancoId())
                .nombreBanco(dto.getNombreBanco())
                .codigoBanco(dto.getCodigoBanco())
                .build();
    }

    private BancoJpaEntity buildEntity(Banco domain) {
        return BancoJpaEntity.builder()
                .bancoId(domain.getBancoId())
                .nombreBanco(domain.getNombreBanco())
                .codigoBanco(domain.getCodigoBanco())
                .build();
    }
}
