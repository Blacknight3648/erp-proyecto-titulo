package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.DireccionDTO;
import backend.com.shared.domain.model.Direccion;
import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DireccionMapper {

    private final TipoDireccionMapper tipoDireccionMapper;
    private final ComunaMapper comunaMapper;

    public Direccion toDomain(DireccionJpaEntity entity) {
        if (entity == null)
            return null;
        return Direccion.builder()
                .direccionId(entity.getDireccionId())
                .calle(entity.getCalle())
                .numero(entity.getNumero())
                .depto(entity.getDepto())
                .tipoDireccion(tipoDireccionMapper.toDomain(entity.getTipoDireccion()))
                .comuna(comunaMapper.toDomain(entity.getComuna()))
                .build();
    }

    public DireccionJpaEntity toEntity(Direccion domain) {
        if (domain == null)
            return null;
        DireccionJpaEntity entity = new DireccionJpaEntity();
        entity.setDireccionId(domain.getDireccionId());
        entity.setCalle(domain.getCalle());
        entity.setNumero(domain.getNumero());
        entity.setDepto(domain.getDepto());
        entity.setTipoDireccion(tipoDireccionMapper.toEntity(domain.getTipoDireccion()));
        entity.setComuna(comunaMapper.toEntity(domain.getComuna()));
        return entity;
    }

    public DireccionDTO toDTO(Direccion domain) {
        if (domain == null)
            return null;
        return DireccionDTO.builder()
                .direccionId(domain.getDireccionId())
                .calle(domain.getCalle())
                .numero(domain.getNumero())
                .depto(domain.getDepto())
                .tipoDireccion(tipoDireccionMapper.toDTO(domain.getTipoDireccion()))
                .comuna(comunaMapper.toDTO(domain.getComuna()))
                .build();
    }

    public Direccion toDomain(DireccionDTO dto) {
        if (dto == null)
            return null;
        return Direccion.builder()
                .direccionId(dto.getDireccionId())
                .calle(dto.getCalle())
                .numero(dto.getNumero())
                .depto(dto.getDepto())
                .tipoDireccion(tipoDireccionMapper.toDomain(dto.getTipoDireccion()))
                .comuna(comunaMapper.toDomain(dto.getComuna()))
                .build();
    }

}