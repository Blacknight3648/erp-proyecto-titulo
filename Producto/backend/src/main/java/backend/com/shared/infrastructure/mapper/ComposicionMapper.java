package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ComposicionDTO;
import backend.com.shared.domain.model.Composicion;
import backend.com.shared.infrastructure.persistence.entity.ComposicionJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ComposicionMapper {

    public Composicion toDomain(ComposicionJpaEntity entity) {
        if (entity == null) return null;
        return Composicion.builder()
                .idComposicion(entity.getIdComposicion())
                .codigoComposicion(entity.getCodigoComposicion())
                .descripcionComposicion(entity.getDescripcionComposicion())
                .clasificacion(entity.getClasificacion())
                .usoTipico(entity.getUsoTipico())
                .build();
    }

    public ComposicionJpaEntity toEntity(Composicion domain) {
        if (domain == null) return null;
        return ComposicionJpaEntity.builder()
                .idComposicion(domain.getIdComposicion())
                .codigoComposicion(domain.getCodigoComposicion())
                .descripcionComposicion(domain.getDescripcionComposicion())
                .clasificacion(domain.getClasificacion())
                .usoTipico(domain.getUsoTipico())
                .build();
    }

    public ComposicionDTO toDTO(Composicion domain) {
        if (domain == null) return null;
        return ComposicionDTO.builder()
                .idComposicion(domain.getIdComposicion())
                .codigoComposicion(domain.getCodigoComposicion())
                .descripcionComposicion(domain.getDescripcionComposicion())
                .clasificacion(domain.getClasificacion())
                .usoTipico(domain.getUsoTipico())
                .build();
    }

    public Composicion toDomain(ComposicionDTO dto) {
        if (dto == null) return null;
        return Composicion.builder()
                .idComposicion(dto.getIdComposicion())
                .codigoComposicion(dto.getCodigoComposicion())
                .descripcionComposicion(dto.getDescripcionComposicion())
                .clasificacion(dto.getClasificacion())
                .usoTipico(dto.getUsoTipico())
                .build();
    }
}