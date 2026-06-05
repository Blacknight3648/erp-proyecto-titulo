package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ColorTelaDTO;
import backend.com.shared.domain.model.ColorTela;
import backend.com.shared.infrastructure.persistence.entity.ColorTelaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ColorTelaMapper {

    public ColorTela toDomain(ColorTelaJpaEntity entity) {
        if (entity == null) return null;
        return ColorTela.builder()
                .idColor(entity.getIdColor())
                .codigoColor(entity.getCodigoColor())
                .descripcionColor(entity.getDescripcionColor())
                .esPantone(entity.getEsPantone())
                .build();
    }

    public ColorTelaJpaEntity toEntity(ColorTela domain) {
        if (domain == null) return null;
        return ColorTelaJpaEntity.builder()
                .idColor(domain.getIdColor())
                .codigoColor(domain.getCodigoColor())
                .descripcionColor(domain.getDescripcionColor())
                .esPantone(domain.getEsPantone() != null ? domain.getEsPantone() : false)
                .build();
    }

    public ColorTelaDTO toDTO(ColorTela domain) {
        if (domain == null) return null;
        return ColorTelaDTO.builder()
                .idColor(domain.getIdColor())
                .codigoColor(domain.getCodigoColor())
                .descripcionColor(domain.getDescripcionColor())
                .esPantone(domain.getEsPantone())
                .build();
    }

    public ColorTela toDomain(ColorTelaDTO dto) {
        if (dto == null) return null;
        return ColorTela.builder()
                .idColor(dto.getIdColor())
                .codigoColor(dto.getCodigoColor())
                .descripcionColor(dto.getDescripcionColor())
                .esPantone(dto.getEsPantone())
                .build();
    }
}