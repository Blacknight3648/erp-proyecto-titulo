package backend.com.shared.infrastructure.mapper;

import backend.com.shared.domain.model.Direccion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DireccionMapper {

    private final TipoDireccionMapper tipoDireccionMapper;
    private final ComunaMapper comunaMapper;

    public Direccion toDomain(backend.com.shared.infrastructure.persistence.entity.Direccion entity) {
        if (entity == null) return null;
        return Direccion.builder()
                .direccionId(entity.getDireccionId())
                .calle(entity.getCalle())
                .numero(entity.getNumero())
                .depto(entity.getDepto())
                .tipoDireccion(tipoDireccionMapper.toDomain(entity.getTipoDireccion()))
                .comuna(comunaMapper.toDomain(entity.getComuna()))
                .build();
    }

    public backend.com.shared.infrastructure.persistence.entity.Direccion toEntity(Direccion domain) {
        if (domain == null) return null;
        backend.com.shared.infrastructure.persistence.entity.Direccion entity =
                new backend.com.shared.infrastructure.persistence.entity.Direccion();
        entity.setDireccionId(domain.getDireccionId());
        entity.setCalle(domain.getCalle());
        entity.setNumero(domain.getNumero());
        entity.setDepto(domain.getDepto());
        entity.setTipoDireccion(tipoDireccionMapper.toEntity(domain.getTipoDireccion()));
        entity.setComuna(comunaMapper.toEntity(domain.getComuna()));
        return entity;
    }
}
