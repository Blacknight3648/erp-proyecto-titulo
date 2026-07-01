package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.application.dto.SCOSPlantillaMaterialVinculoDTO;
import backend.com.comercial.domain.model.SCOSPlantillaMaterialVinculo;
import backend.com.comercial.infrastructure.persistence.entity.SCOSPlantillaMaterialVinculoJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SCOSPlantillaMaterialVinculoMapper {

    public SCOSPlantillaMaterialVinculo toDomain(SCOSPlantillaMaterialVinculoJpaEntity entity) {
        if (entity == null) return null;
        return SCOSPlantillaMaterialVinculo.builder()
                .id(entity.getIdSCOSPlantillaMaterialVinculo())
                .materialType(entity.getMaterialType())
                .materialId(entity.getMaterialId())
                .cantidad(entity.getCantidad())
                .build();
    }

    public SCOSPlantillaMaterialVinculoJpaEntity toEntity(SCOSPlantillaMaterialVinculo domain) {
        if (domain == null) return null;
        return SCOSPlantillaMaterialVinculoJpaEntity.builder()
                .idSCOSPlantillaMaterialVinculo(domain.getId())
                .materialType(domain.getMaterialType())
                .materialId(domain.getMaterialId())
                .cantidad(domain.getCantidad())
                .build();
    }

    public SCOSPlantillaMaterialVinculoDTO toDTO(SCOSPlantillaMaterialVinculo domain) {
        if (domain == null) return null;
        return SCOSPlantillaMaterialVinculoDTO.builder()
                .idSCOSPlantillaMaterialVinculo(domain.getId())
                .materialType(domain.getMaterialType())
                .materialId(domain.getMaterialId())
                .cantidad(domain.getCantidad())
                .build();
    }

    public SCOSPlantillaMaterialVinculo toDomain(SCOSPlantillaMaterialVinculoDTO dto) {
        if (dto == null) return null;
        return SCOSPlantillaMaterialVinculo.builder()
                .id(dto.getIdSCOSPlantillaMaterialVinculo())
                .materialType(dto.getMaterialType())
                .materialId(dto.getMaterialId())
                .cantidad(dto.getCantidad())
                .build();
    }
}
