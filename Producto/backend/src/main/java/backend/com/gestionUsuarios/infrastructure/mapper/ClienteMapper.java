package backend.com.gestionUsuarios.infrastructure.mapper;

import backend.com.gestionUsuarios.application.dto.ClienteDTO;
import backend.com.gestionUsuarios.domain.model.Cliente;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.shared.infrastructure.mapper.ContactoMapper;
import backend.com.shared.infrastructure.mapper.DireccionMapper;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClienteMapper {

    private final GiroMapper giroMapper;
    private final DireccionMapper direccionMapper;
    private final ContactoMapper contactoMapper;

    public Cliente toDomain(ClienteJpaEntity entity) {
        if (entity == null)
            return null;
        return Cliente.builder()
                .clienteId(entity.getClienteId())
                .razonSocial(entity.getRazonSocial())
                .runCliente(entity.getRunCliente())
                .activo(entity.isActivo())
                .sigla(entity.getSigla())
                .giro(giroMapper.toDomain(entity.getGiro()))
                .contactos(entity.getContacto() != null ? entity.getContacto().stream().map(contactoMapper::toDomain).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .direcciones(entity.getDireccion() != null ? entity.getDireccion().stream().map(direccionMapper::toDomain).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .build();
    }

    public ClienteJpaEntity toJpaEntity(Cliente domain) {
        if (domain == null)
            return null;
        return ClienteJpaEntity.builder()
                .clienteId(domain.getClienteId())
                .razonSocial(domain.getRazonSocial())
                .runCliente(domain.getRunCliente())
                .activo(domain.isActivo())
                .sigla(domain.getSigla())
                .giro(giroMapper.toEntity(domain.getGiro()))
                .contacto(domain.getContactos() != null ? domain.getContactos().stream().map(contactoMapper::toEntity).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .direccion(domain.getDirecciones() != null ? domain.getDirecciones().stream().map(direccionMapper::toEntity).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .build();
    }

    public ClienteDTO toDTO(Cliente domain) {
        if (domain == null)
            return null;
        return ClienteDTO.builder()
                .clienteId(domain.getClienteId())
                .razonSocial(domain.getRazonSocial())
                .runCliente(domain.getRunCliente())
                .activo(domain.isActivo())
                .sigla(domain.getSigla())
                .giro(giroMapper.toDTO(domain.getGiro()))
                .contactos(domain.getContactos() != null ? domain.getContactos().stream().map(contactoMapper::toDTO).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .direcciones(domain.getDirecciones() != null ? domain.getDirecciones().stream().map(direccionMapper::toDTO).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .build();
    }

    public Cliente toDomain(ClienteDTO dto) {
        if (dto == null)
            return null;
        return Cliente.builder()
                .clienteId(dto.getClienteId())
                .razonSocial(dto.getRazonSocial())
                .runCliente(dto.getRunCliente())
                .activo(dto.isActivo())
                .sigla(dto.getSigla())
                .giro(giroMapper.toDomain(dto.getGiro()))
                .contactos(dto.getContactos() != null ? dto.getContactos().stream().map(contactoMapper::toDomain).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .direcciones(dto.getDirecciones() != null ? dto.getDirecciones().stream().map(direccionMapper::toDomain).collect(Collectors.toList()) : new java.util.ArrayList<>())
                .build();
    }

    public List<ClienteDTO> toDTOList(List<Cliente> clientes) {
        return clientes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
