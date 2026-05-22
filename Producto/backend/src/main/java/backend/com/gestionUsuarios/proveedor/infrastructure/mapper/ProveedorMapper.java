package backend.com.gestionUsuarios.proveedor.infrastructure.mapper;

import backend.com.gestionUsuarios.proveedor.application.dto.ProveedorDTO;
import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProveedorMapper {

    private final GiroMapper giroMapper;

    public Proveedor toDomain(ProveedorJpaEntity entity) {
        if (entity == null)
            return null;
        return Proveedor.builder()
                .proveedorId(entity.getProveedorId())
                .runProveedor(entity.getRunProveedor())
                .razonSocialProveedor(entity.getRazonSocialProveedor())
                .horarioAtencion(entity.getHorarioAtencion())
                .tipoProveedor(entity.getTipoProveedor())
                .activo(entity.getActivo() != null && entity.getActivo())
                .giro(giroMapper.toDomain(entity.getGiro()))
                .build();
    }

    public ProveedorJpaEntity toEntity(Proveedor domain) {
        if (domain == null)
            return null;
        ProveedorJpaEntity entity = new ProveedorJpaEntity();
        entity.setProveedorId(domain.getProveedorId());
        entity.setRunProveedor(domain.getRunProveedor());
        entity.setRazonSocialProveedor(domain.getRazonSocialProveedor());
        entity.setHorarioAtencion(domain.getHorarioAtencion());
        entity.setTipoProveedor(domain.getTipoProveedor());
        entity.setActivo(domain.isActivo());
        entity.setGiro(giroMapper.toEntity(domain.getGiro()));
        return entity;
    }

    public ProveedorDTO toDTO(Proveedor domain) {
        if (domain == null)
            return null;
        return ProveedorDTO.builder()
                .proveedorId(domain.getProveedorId())
                .runProveedor(domain.getRunProveedor())
                .razonSocialProveedor(domain.getRazonSocialProveedor())
                .horarioAtencion(domain.getHorarioAtencion())
                .tipoProveedor(domain.getTipoProveedor())
                .activo(domain.isActivo())
                .giro(giroMapper.toDTO(domain.getGiro()))
                .build();
    }

    public Proveedor toDomain(ProveedorDTO dto) {
        if (dto == null)
            return null;
        return Proveedor.builder()
                .proveedorId(dto.getProveedorId())
                .runProveedor(dto.getRunProveedor())
                .razonSocialProveedor(dto.getRazonSocialProveedor())
                .horarioAtencion(dto.getHorarioAtencion())
                .tipoProveedor(dto.getTipoProveedor())
                .activo(dto.isActivo())
                .giro(giroMapper.toDomain(dto.getGiro()))
                .build();
    }

    public List<ProveedorDTO> toDTOList(List<Proveedor> proveedores) {
        return proveedores.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
