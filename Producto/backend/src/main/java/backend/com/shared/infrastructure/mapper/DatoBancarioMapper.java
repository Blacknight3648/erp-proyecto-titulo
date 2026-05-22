package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.DatoBancario;
import backend.com.shared.infrastructure.persistence.entity.DatoBancarioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatoBancarioMapper {

    private final BancoMapper bancoMapper;
    private final TipoCuentaBancariaMapper tipoCuentaBancariaMapper;

    public DatoBancario toDomain(DatoBancarioJpaEntity entity) {
        if (entity == null) return null;
        return DatoBancario.builder()
                .datoBancarioId(entity.getDatoBancarioId())
                .numeroCuenta(entity.getNumeroCuenta())
                .banco(bancoMapper.toDomain(entity.getBanco()))
                .tipoCuentaBancaria(tipoCuentaBancariaMapper.toDomain(entity.getTipoCuentaBancaria()))
                .build();
    }

    public DatoBancarioJpaEntity toEntity(DatoBancario domain) {
        if (domain == null) return null;
        DatoBancarioJpaEntity entity = new DatoBancarioJpaEntity();
        entity.setDatoBancarioId(domain.getDatoBancarioId());
        entity.setNumeroCuenta(domain.getNumeroCuenta());
        entity.setBanco(bancoMapper.toEntity(domain.getBanco()));
        entity.setTipoCuentaBancaria(tipoCuentaBancariaMapper.toEntity(domain.getTipoCuentaBancaria()));
        return entity;
    }
}
