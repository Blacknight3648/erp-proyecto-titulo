package backend.com.gestionUsuarios.cliente.infrastructure.mapper;

import backend.com.gestionUsuarios.cliente.application.dto.ClienteDTO;
import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
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
                .contactos(entity.getContacto().stream().map(contactoMapper::toDomain).collect(Collectors.toList()))
                .direcciones(
                        entity.getDireccion().stream().map(direccionMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public ClienteJpaEntity toEntity(Cliente domain) {
        if (domain == null)
            return null;
        return ClienteJpaEntity.builder()
                .clienteId(domain.getClienteId())
                .razonSocial(domain.getRazonSocial())
                .runCliente(domain.getRunCliente())
                .activo(domain.isActivo())
                .sigla(domain.getSigla())
                .giro(giroMapper.toEntity(domain.getGiro()))
                .contacto(domain.getContactos().stream().map(contactoMapper::toEntity).collect(Collectors.toList()))
                .direccion(
                        domain.getDirecciones().stream().map(direccionMapper::toEntity).collect(Collectors.toList()))
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
                .contactos(domain.getContactos().stream().map(contactoMapper::toDTO).collect(Collectors.toList()))
                .direcciones(domain.getDirecciones().stream().map(direccionMapper::toDTO).collect(Collectors.toList()))
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
                .contactos(dto.getContactos().stream().map(contactoMapper::toDomain).collect(Collectors.toList()))
                .direcciones(dto.getDirecciones().stream().map(direccionMapper::toDomain).collect(Collectors.toList()))
                .build();
    }

    public List<ClienteDTO> toDTOList(List<Cliente> clientes) {
        return clientes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}