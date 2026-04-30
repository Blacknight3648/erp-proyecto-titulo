package backend.com.gestionUsuarios.vendedor.infrastructure.mapper;

import backend.com.gestionUsuarios.vendedor.domain.model.Vendedor;
import backend.com.gestionUsuarios.vendedor.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendedorMapper {

    public Vendedor toDomain(VendedorJpaEntity entity) {
        if (entity == null)
            return null;

        return Vendedor.builder()
                .id(entity.getIdVendedor())
                .usuarioId(entity.getUsuario() != null ? entity.getUsuario().getUsuarioId() : null)
                .codigoVendedor(entity.getCodigoVendedor())
                .activo(entity.getActivo())
                .creadoEn(entity.getCreadoEn())
                .actualizadoEn(entity.getActualizadoEn())
                .nombreCompleto(entity.getUsuario() != null
                        ? entity.getUsuario().getUsuarioNombre() + " " + entity.getUsuario().getUsuarioApellidos()
                        : null)
                .build();
    }

    public VendedorDTO toDTO(VendedorJpaEntity entity) {
        if (entity == null)
            return null;

        return VendedorDTO.builder()
                .id(entity.getIdVendedor())
                .usuarioId(entity.getUsuario() != null ? entity.getUsuario().getUsuarioId() : null)
                .nombreUsuario(entity.getUsuario() != null ? entity.getUsuario().getUsuarioNombre() : null)
                .apellidosUsuario(entity.getUsuario() != null ? entity.getUsuario().getUsuarioApellidos() : null)
                .codigoVendedor(entity.getCodigoVendedor())
                .activo(entity.getActivo())
                .creadoEn(entity.getCreadoEn())
                .actualizadoEn(entity.getActualizadoEn())
                .build();
    }

    public List<VendedorDTO> toDTOList(List<VendedorJpaEntity> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
