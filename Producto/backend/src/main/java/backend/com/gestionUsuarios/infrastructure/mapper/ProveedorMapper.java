package backend.com.gestionUsuarios.infrastructure.mapper;

import backend.com.gestionUsuarios.application.dto.ProveedorDTO;
import backend.com.gestionUsuarios.domain.model.Proveedor;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.mapper.ContactoMapper;
import backend.com.shared.infrastructure.mapper.DireccionMapper;
import backend.com.shared.infrastructure.mapper.DatoBancarioMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProveedorMapper {

        private final GiroMapper giroMapper;
        private final ContactoMapper contactoMapper;
        private final DireccionMapper direccionMapper;
        private final DatoBancarioMapper datoBancarioMapper;

        public Proveedor toDomain(ProveedorJpaEntity entity) {
                if (entity == null)
                        return null;
                return Proveedor.builder()
                                .proveedorId(entity.getProveedorId())
                                .runProveedor(entity.getRunProveedor())
                                .razonSocialProveedor(entity.getRazonSocialProveedor())
                                .horarioAtencion(entity.getHorarioAtencion())
                                .sigla(entity.getSigla())
                                .tipoProveedor(entity.getTipoProveedor())
                                .activo(entity.getActivo() != null && entity.getActivo())
                                .contactos(entity.getContactos() != null ? entity.getContactos().stream()
                                                .map(contactoMapper::toDomain)
                                                .collect(Collectors.toList()) : null)
                                .direccion(entity.getDirecciones() != null ? entity.getDirecciones().stream()
                                                .map(direccionMapper::toDomain)
                                                .collect(Collectors.toList()) : null)
                                .datosBancarios(entity.getDatosBancarios() != null ? entity.getDatosBancarios().stream()
                                                .map(datoBancarioMapper::toDomain)
                                                .collect(Collectors.toList()) : null)
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
                entity.setSigla(domain.getSigla());
                entity.setActivo(domain.isActivo());
                entity.setGiro(giroMapper.toEntity(domain.getGiro()));
                entity.setContactos(domain.getContactos() != null ? domain.getContactos().stream()
                                .map(contactoMapper::toEntity)
                                .collect(Collectors.toList()) : null);
                entity.setDirecciones(domain.getDireccion() != null ? domain.getDireccion().stream()
                                .map(direccionMapper::toEntity)
                                .collect(Collectors.toList()) : null);
                entity.setDatosBancarios(domain.getDatosBancarios() != null ? domain.getDatosBancarios().stream()
                                .map(datoBancarioMapper::toEntity)
                                .collect(Collectors.toList()) : null);
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
                                .sigla(domain.getSigla())
                                .activo(domain.isActivo())
                                .giro(giroMapper.toDTO(domain.getGiro()))
                                .contactos(domain.getContactos() != null ? domain.getContactos().stream()
                                                .map(contactoMapper::toDTO)
                                                .collect(Collectors.toList()) : null)
                                .direccion(domain.getDireccion() != null ? domain.getDireccion().stream()
                                                .map(direccionMapper::toDTO)
                                                .collect(Collectors.toList()) : null)
                                .datosBancarios(domain.getDatosBancarios() != null ? domain.getDatosBancarios().stream()
                                                .map(datoBancarioMapper::toDTO)
                                                .collect(Collectors.toList()) : null)
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
                                .sigla(dto.getSigla())
                                .activo(dto.isActivo())
                                .giro(giroMapper.toDomain(dto.getGiro()))
                                .contactos(dto.getContactos() != null ? dto.getContactos().stream()
                                                .map(contactoMapper::toDomain)
                                                .collect(Collectors.toList()) : null)
                                .direccion(dto.getDireccion() != null ? dto.getDireccion().stream()
                                                .map(direccionMapper::toDomain)
                                                .collect(Collectors.toList()) : null)
                                .datosBancarios(dto.getDatosBancarios() != null ? dto.getDatosBancarios().stream()
                                                .map(datoBancarioMapper::toDomain)
                                                .collect(Collectors.toList()) : null)
                                .build();
        }

        public List<ProveedorDTO> toDTOList(List<Proveedor> proveedores) {
                return proveedores.stream().map(this::toDTO).collect(Collectors.toList());
        }

}
