package backend.com.gestionUsuarios.proveedor.infrastructure.mapper;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProveedorMapper {

    public Proveedor toDomain(ProveedorJpaEntity entity) {

        if (entity == null) {
            return null;
        }

        return Proveedor.builder()
                .proveedorId(entity.getProveedorId())
                .nombreProveedor(entity.getNombreProveedor())
                .rutProveedor(entity.getRutProveedor())
                .direccionProveedor(entity.getDireccionProveedor())
                .telefonoProveedor(entity.getTelefonoProveedor())
                .emailProveedor(entity.getEmailProveedor())
                .contactoProveedor(entity.getContactoProveedor())
                .categoria(entity.getCategoria())
                .activo(entity.getActivo() != null && entity.getActivo())
                .build();
    }

    public ProveedorJpaEntity toEntity(Proveedor domain) {

        if (domain == null) {
            return null;
        }

        ProveedorJpaEntity entity = new ProveedorJpaEntity();
        entity.setProveedorId(domain.getProveedorId());
        entity.setNombreProveedor(domain.getNombreProveedor());
        entity.setRutProveedor(domain.getRutProveedor());
        entity.setDireccionProveedor(domain.getDireccionProveedor());
        entity.setTelefonoProveedor(domain.getTelefonoProveedor());
        entity.setEmailProveedor(domain.getEmailProveedor());
        entity.setContactoProveedor(domain.getContactoProveedor());
        entity.setCategoria(domain.getCategoria());
        entity.setActivo(domain.isActivo());
        return entity;
    }

    public List<Proveedor> toDomainList(List<ProveedorJpaEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<ProveedorJpaEntity> toEntityList(List<Proveedor> domains) {
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }
}
