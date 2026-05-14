package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GiroMapper {

    public Giro toDomain(GiroJpaEntity entity) {
        if (entity == null) return null;
        return Giro.builder()
                .giroId(entity.getGiroId())
                .descripcionGiro(entity.getDescripcionGiro())
                .codigoActividad(entity.getCodigoActividad())
                .tipoActividad(entity.getTipoActividad())
                .categoriaTributaria(entity.getCategoriaTributaria())
                .afectoIva(entity.getAfectoIva())
                .regimenTributario(entity.getRegimenTributario())
                .build();
    }

    public GiroJpaEntity toEntity(Giro domain) {
        if (domain == null) return null;
        return GiroJpaEntity.builder()
                .giroId(domain.getGiroId())
                .descripcionGiro(domain.getDescripcionGiro())
                .codigoActividad(domain.getCodigoActividad())
                .tipoActividad(domain.getTipoActividad())
                .categoriaTributaria(domain.getCategoriaTributaria())
                .afectoIva(domain.getAfectoIva())
                .regimenTributario(domain.getRegimenTributario())
                .build();
    }

    public GiroDTO toDTO(Giro domain) {
        if (domain == null) return null;
        return GiroDTO.builder()
                .giroId(domain.getGiroId())
                .descripcionGiro(domain.getDescripcionGiro())
                .codigoActividad(domain.getCodigoActividad())
                .tipoActividad(domain.getTipoActividad())
                .categoriaTributaria(domain.getCategoriaTributaria())
                .afectoIva(domain.getAfectoIva())
                .regimenTributario(domain.getRegimenTributario())
                .build();
    }

    public Giro toDomain(GiroDTO dto) {
        if (dto == null) return null;
        return Giro.builder()
                .giroId(dto.getGiroId())
                .descripcionGiro(dto.getDescripcionGiro())
                .codigoActividad(dto.getCodigoActividad())
                .tipoActividad(dto.getTipoActividad())
                .categoriaTributaria(dto.getCategoriaTributaria())
                .afectoIva(dto.getAfectoIva())
                .regimenTributario(dto.getRegimenTributario())
                .build();
    }

    public List<GiroDTO> toDTOList(List<Giro> giros) {
        return giros.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
