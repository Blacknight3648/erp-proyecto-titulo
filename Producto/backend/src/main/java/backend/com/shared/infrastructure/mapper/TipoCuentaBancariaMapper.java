package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.TipoCuentaBancariaDTO;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.infrastructure.persistence.entity.TipoCuentaBancariaJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoCuentaBancariaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TipoCuentaBancariaMapper {

    private final TipoCuentaBancariaJpaRepository tipoCuentaBancariaJpaRepository;

    public TipoCuentaBancaria toDomain(TipoCuentaBancariaJpaEntity entity) {
        if (entity == null) return null;
        return TipoCuentaBancaria.builder()
                .tipoCuentaId(entity.getTipoCuentaId())
                .denominacionCuenta(entity.getDenominacionCuenta())
                .build();
    }

    public TipoCuentaBancariaJpaEntity toEntity(TipoCuentaBancaria domain) {
        if (domain == null) return null;

        if (domain.getTipoCuentaId() != null) {
            return tipoCuentaBancariaJpaRepository.findById(domain.getTipoCuentaId())
                    .orElseGet(() -> {
                        TipoCuentaBancariaJpaEntity entity = new TipoCuentaBancariaJpaEntity();
                        entity.setTipoCuentaId(domain.getTipoCuentaId());
                        entity.setDenominacionCuenta(domain.getDenominacionCuenta());
                        return entity;
                    });
        }

        TipoCuentaBancariaJpaEntity entity = new TipoCuentaBancariaJpaEntity();
        entity.setTipoCuentaId(domain.getTipoCuentaId());
        entity.setDenominacionCuenta(domain.getDenominacionCuenta());
        return entity;
    }

    public TipoCuentaBancariaDTO toDTO(TipoCuentaBancaria domain) {
        if (domain == null) return null;
        return TipoCuentaBancariaDTO.builder()
                .tipoCuentaId(domain.getTipoCuentaId())
                .denominacionCuenta(domain.getDenominacionCuenta())
                .build();
    }

    public TipoCuentaBancaria toDomain(TipoCuentaBancariaDTO dto) {
        if (dto == null) return null;
        return TipoCuentaBancaria.builder()
                .tipoCuentaId(dto.getTipoCuentaId())
                .denominacionCuenta(dto.getDenominacionCuenta())
                .build();
    }
}
