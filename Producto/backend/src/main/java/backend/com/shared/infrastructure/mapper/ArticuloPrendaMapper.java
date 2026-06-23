package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ArticuloPrendaDTO;
import backend.com.shared.domain.model.ArticuloPrenda;
import backend.com.shared.infrastructure.persistence.entity.ArticuloPrendaJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ArticuloPrendaMapper {

    public ArticuloPrenda toDomain(ArticuloPrendaJpaEntity entity) {
        if (entity == null) return null;
        return ArticuloPrenda.builder()
                .id(entity.getId())
                .marca(entity.getMarca())
                .tallasDisponibles(entity.getTallasDisponibles())
                .proveedor(entity.getProveedor())
                .codigoProveedor(entity.getCodigoProveedor())
                .requiereLogoCliente(entity.getRequiereLogoCliente())
                .tieneEstampado(entity.getTieneEstampado())
                .ubicacionLogo(entity.getUbicacionLogo())
                .build();
    }

    public ArticuloPrendaJpaEntity toEntity(ArticuloPrenda domain) {
        if (domain == null) return null;
        return ArticuloPrendaJpaEntity.builder()
                .id(domain.getId())
                .marca(domain.getMarca())
                .tallasDisponibles(domain.getTallasDisponibles())
                .proveedor(domain.getProveedor())
                .codigoProveedor(domain.getCodigoProveedor())
                .requiereLogoCliente(domain.getRequiereLogoCliente() != null ? domain.getRequiereLogoCliente() : false)
                .tieneEstampado(domain.getTieneEstampado() != null ? domain.getTieneEstampado() : false)
                .ubicacionLogo(domain.getUbicacionLogo())
                .build();
    }

    public ArticuloPrendaDTO toDTO(ArticuloPrenda domain) {
        if (domain == null) return null;
        return ArticuloPrendaDTO.builder()
                .id(domain.getId())
                .marca(domain.getMarca())
                .tallasDisponibles(domain.getTallasDisponibles())
                .proveedor(domain.getProveedor())
                .codigoProveedor(domain.getCodigoProveedor())
                .requiereLogoCliente(domain.getRequiereLogoCliente())
                .tieneEstampado(domain.getTieneEstampado())
                .ubicacionLogo(domain.getUbicacionLogo())
                .build();
    }

    public ArticuloPrenda toDomain(ArticuloPrendaDTO dto) {
        if (dto == null) return null;
        return ArticuloPrenda.builder()
                .id(dto.getId())
                .marca(dto.getMarca())
                .tallasDisponibles(dto.getTallasDisponibles())
                .proveedor(dto.getProveedor())
                .codigoProveedor(dto.getCodigoProveedor())
                .requiereLogoCliente(dto.getRequiereLogoCliente())
                .tieneEstampado(dto.getTieneEstampado())
                .ubicacionLogo(dto.getUbicacionLogo())
                .build();
    }
}