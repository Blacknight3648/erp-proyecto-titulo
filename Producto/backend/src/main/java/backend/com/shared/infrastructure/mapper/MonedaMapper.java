package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.MonedaDTO;
import backend.com.shared.domain.model.Moneda;
import backend.com.shared.infrastructure.persistence.entity.MonedaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class MonedaMapper {

    public Moneda toDomain(MonedaJpaEntity entity) {
        if (entity == null) return null;
        return Moneda.builder()
                .idMoneda(entity.getIdMoneda())
                .codigoMoneda(entity.getCodigoMoneda())
                .nombreMoneda(entity.getNombreMoneda())
                .simbolo(entity.getSimbolo())
                .build();
    }

    public MonedaJpaEntity toEntity(Moneda domain) {
        if (domain == null) return null;
        return MonedaJpaEntity.builder()
                .idMoneda(domain.getIdMoneda())
                .codigoMoneda(domain.getCodigoMoneda())
                .nombreMoneda(domain.getNombreMoneda())
                .simbolo(domain.getSimbolo())
                .build();
    }

    public MonedaDTO toDTO(Moneda domain) {
        if (domain == null) return null;
        return MonedaDTO.builder()
                .idMoneda(domain.getIdMoneda())
                .codigoMoneda(domain.getCodigoMoneda())
                .nombreMoneda(domain.getNombreMoneda())
                .simbolo(domain.getSimbolo())
                .build();
    }

    public Moneda toDomain(MonedaDTO dto) {
        if (dto == null) return null;
        return Moneda.builder()
                .idMoneda(dto.getIdMoneda())
                .codigoMoneda(dto.getCodigoMoneda())
                .nombreMoneda(dto.getNombreMoneda())
                .simbolo(dto.getSimbolo())
                .build();
    }
}