package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;
import backend.com.comercial.domain.model.DescripcionPlantilla;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.model.SCOSPlantillaMaterialVinculo;
import backend.com.comercial.infrastructure.persistence.entity.DescripcionPlantillaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.CamposPlantillaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.SCOSPlantillaMaterialVinculoJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DescripcionPlantillaMapper {

    private final CamposPlantillaMapper plantillaMapper;
    private final SCOSPlantillaMaterialVinculoMapper vinculoMapper;

    public DescripcionPlantilla toDomain(DescripcionPlantillaJpaEntity entity) {
        if (entity == null) return null;
        List<SCOSPlantillaMaterialVinculo> vinculos = entity.getVinculos() == null
                ? java.util.Collections.emptyList()
                : entity.getVinculos().stream().map(vinculoMapper::toDomain).toList();
        return DescripcionPlantilla.builder()
                .idDescripcionPlantilla(entity.getIdDescripcionPlantilla())
                .idSCOS(entity.getIdSCOS())
                .plantilla(plantillaMapper.toDomain(entity.getPlantilla()))
                .valorDescripcion(entity.getValorDescripcion())
                .activo(entity.getActivo())
                .vinculos(new java.util.ArrayList<>(vinculos))
                .build();
    }

    public DescripcionPlantillaJpaEntity toEntity(DescripcionPlantilla domain) {
        if (domain == null) return null;
        CamposPlantillaJpaEntity plantilla = domain.getPlantilla() != null
                ? CamposPlantillaJpaEntity.builder()
                    .idPlantilla(domain.getPlantilla().getIdPlantilla())
                    .nombreCampo(domain.getPlantilla().getNombreCampo())
                    .build()
                : null;

        DescripcionPlantillaJpaEntity entity = DescripcionPlantillaJpaEntity.builder()
                .idDescripcionPlantilla(domain.getIdDescripcionPlantilla())
                .idSCOS(domain.getIdSCOS())
                .plantilla(plantilla)
                .valorDescripcion(domain.getValorDescripcion())
                .activo(domain.getActivo() != null ? domain.getActivo() : true)
                .vinculos(new java.util.ArrayList<>())
                .build();

        if (domain.getVinculos() != null) {
            for (SCOSPlantillaMaterialVinculo v : domain.getVinculos()) {
                SCOSPlantillaMaterialVinculoJpaEntity vEntity = vinculoMapper.toEntity(v);
                vEntity.setDescripcionPlantilla(entity);
                entity.getVinculos().add(vEntity);
            }
        }
        return entity;
    }

    public DescripcionPlantillaDTO toDTO(DescripcionPlantilla domain) {
        if (domain == null) return null;
        CamposPlantilla p = domain.getPlantilla();
        return DescripcionPlantillaDTO.builder()
                .idDescripcionPlantilla(domain.getIdDescripcionPlantilla())
                .idSCOS(domain.getIdSCOS())
                .idPlantilla(p != null ? p.getIdPlantilla() : null)
                .nombreCampo(p != null ? p.getNombreCampo() : null)
                .valorDescripcion(domain.getValorDescripcion())
                .activo(domain.getActivo())
                .vinculos(domain.getVinculos() == null ? new java.util.ArrayList<>()
                        : domain.getVinculos().stream().map(vinculoMapper::toDTO).toList())
                .build();
    }
}
