package backend.com.gestionUsuarios.proveedor.infrastructure.mapper;

import backend.com.gestionUsuarios.proveedor.application.dto.ProveedorDTO;
import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.mapper.SiglaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProveedorMapper {

    private final GiroMapper giroMapper;
    private final SiglaMapper siglaMapper;

    public Proveedor toDomain(ProveedorJpaEntity entity) {
        if (entity == null) return null;
        return Proveedor.builder()
                .proveedorId(entity.getProveedorId())
                .runProveedor(entity.getRunProveedor())
                .razonSocialProveedor(entity.getRazonSocialProveedor())
                .direccionProveedor(entity.getDireccionProveedor())
                .telefonoProveedor(entity.getTelefonoProveedor())
                .emailProveedor(entity.getEmailProveedor())
                .contactoProveedor(entity.getContactoProveedor())
                .tipoProveedor(entity.getTipoProveedor())
                .activo(entity.getActivo() != null && entity.getActivo())
                .sigla(siglaMapper.toDomain(entity.getSigla()))
                .giro(giroMapper.toDomain(entity.getGiro()))
                .build();
    }

    public ProveedorJpaEntity toEntity(Proveedor domain) {
        if (domain == null) return null;
        ProveedorJpaEntity entity = new ProveedorJpaEntity();
        entity.setProveedorId(domain.getProveedorId());
        entity.setRunProveedor(domain.getRunProveedor());
        entity.setRazonSocialProveedor(domain.getRazonSocialProveedor());
        entity.setDireccionProveedor(domain.getDireccionProveedor());
        entity.setTelefonoProveedor(domain.getTelefonoProveedor());
        entity.setEmailProveedor(domain.getEmailProveedor());
        entity.setContactoProveedor(domain.getContactoProveedor());
        entity.setTipoProveedor(domain.getTipoProveedor());
        entity.setActivo(domain.isActivo());
        entity.setSigla(siglaMapper.toEntity(domain.getSigla()));
        entity.setGiro(giroMapper.toEntity(domain.getGiro()));
        return entity;
    }

    public ProveedorDTO toDTO(Proveedor domain) {
        if (domain == null) return null;
        return ProveedorDTO.builder()
                .proveedorId(domain.getProveedorId())
                .runProveedor(domain.getRunProveedor())
                .razonSocialProveedor(domain.getRazonSocialProveedor())
                .direccionProveedor(domain.getDireccionProveedor())
                .telefonoProveedor(domain.getTelefonoProveedor())
                .emailProveedor(domain.getEmailProveedor())
                .contactoProveedor(domain.getContactoProveedor())
                .tipoProveedor(domain.getTipoProveedor())
                .activo(domain.isActivo())
                .sigla(siglaMapper.toDTO(domain.getSigla()))
                .giro(giroMapper.toDTO(domain.getGiro()))
                .build();
    }

    public Proveedor toDomain(ProveedorDTO dto) {
        if (dto == null) return null;
        return Proveedor.builder()
                .proveedorId(dto.getProveedorId())
                .runProveedor(dto.getRunProveedor())
                .razonSocialProveedor(dto.getRazonSocialProveedor())
                .direccionProveedor(dto.getDireccionProveedor())
                .telefonoProveedor(dto.getTelefonoProveedor())
                .emailProveedor(dto.getEmailProveedor())
                .contactoProveedor(dto.getContactoProveedor())
                .tipoProveedor(dto.getTipoProveedor())
                .activo(dto.isActivo())
                .sigla(siglaMapper.toDomain(dto.getSigla()))
                .giro(giroMapper.toDomain(dto.getGiro()))
                .build();
    }

    public List<ProveedorDTO> toDTOList(List<Proveedor> proveedores) {
        return proveedores.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
