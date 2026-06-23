package backend.com.gestionUsuarios.infrastructure.mapper;

import backend.com.gestionUsuarios.domain.model.Vendedor;
import backend.com.gestionUsuarios.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class VendedorMapper {

    public Vendedor toDomain(VendedorJpaEntity entity) {
        if (entity == null)
            return null;

        return Vendedor.builder()
                .vendedorId(entity.getIdVendedor())
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

    public VendedorJpaEntity toJpaEntity(Vendedor domain) {
        if (domain == null)
            return null;

        VendedorJpaEntity entity = new VendedorJpaEntity();
        entity.setIdVendedor(domain.getVendedorId());
        entity.setCodigoVendedor(domain.getCodigoVendedor());
        entity.setActivo(domain.getActivo());
        return entity;
    }

    public VendedorDTO toDTO(Vendedor domain) {
        if (domain == null)
            return null;

        return VendedorDTO.builder()
                .vendedorId(domain.getVendedorId())
                .usuarioId(domain.getUsuarioId())
                .codigoVendedor(domain.getCodigoVendedor())
                .activo(domain.getActivo())
                .creadoEn(domain.getCreadoEn())
                .actualizadoEn(domain.getActualizadoEn())
                .nombreUsuario(domain.getNombreCompleto())
                .build();
    }

    public VendedorDTO toDTO(VendedorJpaEntity entity) {
        if (entity == null)
            return null;

        return VendedorDTO.builder()
                .vendedorId(entity.getIdVendedor())
                .usuarioId(entity.getUsuario() != null ? entity.getUsuario().getUsuarioId() : null)
                .nombreUsuario(entity.getUsuario() != null ? entity.getUsuario().getUsuarioNombre() : null)
                .apellidosUsuario(entity.getUsuario() != null ? entity.getUsuario().getUsuarioApellidos() : null)
                .codigoVendedor(entity.getCodigoVendedor())
                .activo(entity.getActivo())
                .creadoEn(entity.getCreadoEn())
                .actualizadoEn(entity.getActualizadoEn())
                .build();
    }

    public List<VendedorDTO> toDTOList(List<Vendedor> domains) {
        return domains.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
