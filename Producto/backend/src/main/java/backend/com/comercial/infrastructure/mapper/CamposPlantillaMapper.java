package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.application.dto.CamposPlantillaDTO;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.infrastructure.persistence.entity.CamposPlantillaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class CamposPlantillaMapper {

    public CamposPlantilla toDomain(CamposPlantillaJpaEntity entity) {
        if (entity == null) return null;
        return CamposPlantilla.builder()
                .idPlantilla(entity.getIdPlantilla())
                .nombreCampo(entity.getNombreCampo())
                .build();
    }

    public CamposPlantillaJpaEntity toEntity(CamposPlantilla domain) {
        if (domain == null) return null;
        return CamposPlantillaJpaEntity.builder()
                .idPlantilla(domain.getIdPlantilla())
                .nombreCampo(domain.getNombreCampo())
                .build();
    }

    public CamposPlantillaDTO toDTO(CamposPlantilla domain) {
        if (domain == null) return null;
        return CamposPlantillaDTO.builder()
                .idPlantilla(domain.getIdPlantilla())
                .nombreCampo(domain.getNombreCampo())
                .build();
    }

    public CamposPlantilla toDomain(CamposPlantillaDTO dto) {
        if (dto == null) return null;
        return CamposPlantilla.builder()
                .idPlantilla(dto.getIdPlantilla())
                .nombreCampo(dto.getNombreCampo())
                .build();
    }
}
