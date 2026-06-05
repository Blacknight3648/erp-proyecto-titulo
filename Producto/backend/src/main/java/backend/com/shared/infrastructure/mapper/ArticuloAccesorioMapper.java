package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ArticuloAccesorioDTO;
import backend.com.shared.domain.model.ArticuloAccesorio;
import backend.com.shared.infrastructure.persistence.entity.ArticuloAccesorioJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class ArticuloAccesorioMapper {

    public ArticuloAccesorio toDomain(ArticuloAccesorioJpaEntity entity) {
        if (entity == null) return null;
        return ArticuloAccesorio.builder()
                .id(entity.getId())
                .subtipoAccesorio(entity.getSubtipoAccesorio())
                .tallasDisponibles(entity.getTallasDisponibles())
                .proveedor(entity.getProveedor())
                .codigoProveedor(entity.getCodigoProveedor())
                .requiereLogoCliente(entity.getRequiereLogoCliente())
                .build();
    }

    public ArticuloAccesorioJpaEntity toEntity(ArticuloAccesorio domain) {
        if (domain == null) return null;
        return ArticuloAccesorioJpaEntity.builder()
                .id(domain.getId())
                .subtipoAccesorio(domain.getSubtipoAccesorio())
                .tallasDisponibles(domain.getTallasDisponibles())
                .proveedor(domain.getProveedor())
                .codigoProveedor(domain.getCodigoProveedor())
                .requiereLogoCliente(domain.getRequiereLogoCliente() != null ? domain.getRequiereLogoCliente() : false)
                .build();
    }

    public ArticuloAccesorioDTO toDTO(ArticuloAccesorio domain) {
        if (domain == null) return null;
        return ArticuloAccesorioDTO.builder()
                .id(domain.getId())
                .subtipoAccesorio(domain.getSubtipoAccesorio())
                .tallasDisponibles(domain.getTallasDisponibles())
                .proveedor(domain.getProveedor())
                .codigoProveedor(domain.getCodigoProveedor())
                .requiereLogoCliente(domain.getRequiereLogoCliente())
                .build();
    }

    public ArticuloAccesorio toDomain(ArticuloAccesorioDTO dto) {
        if (dto == null) return null;
        return ArticuloAccesorio.builder()
                .id(dto.getId())
                .subtipoAccesorio(dto.getSubtipoAccesorio())
                .tallasDisponibles(dto.getTallasDisponibles())
                .proveedor(dto.getProveedor())
                .codigoProveedor(dto.getCodigoProveedor())
                .requiereLogoCliente(dto.getRequiereLogoCliente())
                .build();
    }
}