package backend.com.gestionUsuarios.cliente.infrastructure.mapper;

import backend.com.gestionUsuarios.cliente.application.dto.ClienteDTO;
import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.shared.application.dto.DatoBancarioResponse;
import backend.com.shared.application.dto.DireccionResponse;
import backend.com.shared.infrastructure.mapper.DatoBancarioMapper;
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
    private final DatoBancarioMapper datoBancarioMapper;

    public Cliente toDomain(ClienteJpaEntity entity) {
        if (entity == null)
            return null;
        return Cliente.builder()
                .clienteId(entity.getClienteId())
                .razonSocial(entity.getRazonSocial())
                .runCliente(entity.getRunCliente())
                .correoCliente(entity.getCorreoCliente())
                .telefonoCliente(entity.getTelefonoCliente())
                .contactoCliente(entity.getContactoCliente())
                .activo(entity.isActivo())
                .sigla(entity.getSigla())
                .giro(giroMapper.toDomain(entity.getGiro()))
                .direccion(direccionMapper.toDomain(entity.getDireccion()))
                .datoBancario(datoBancarioMapper.toDomain(entity.getDatoBancario()))
                .build();
    }

    public ClienteJpaEntity toEntity(Cliente domain) {
        if (domain == null)
            return null;
        return ClienteJpaEntity.builder()
                .clienteId(domain.getClienteId())
                .razonSocial(domain.getRazonSocial())
                .runCliente(domain.getRunCliente())
                .correoCliente(domain.getCorreoCliente())
                .telefonoCliente(domain.getTelefonoCliente())
                .contactoCliente(domain.getContactoCliente())
                .activo(domain.isActivo())
                .sigla(domain.getSigla())
                .giro(giroMapper.toEntity(domain.getGiro()))
                .direccion(direccionMapper.toEntity(domain.getDireccion()))
                .datoBancario(datoBancarioMapper.toEntity(domain.getDatoBancario()))
                .build();
    }

    public ClienteDTO toDTO(Cliente domain) {
        if (domain == null)
            return null;
        return ClienteDTO.builder()
                .clienteId(domain.getClienteId())
                .razonSocial(domain.getRazonSocial())
                .runCliente(domain.getRunCliente())
                .correoCliente(domain.getCorreoCliente())
                .telefonoCliente(domain.getTelefonoCliente())
                .contactoCliente(domain.getContactoCliente())
                .activo(domain.isActivo())
                .sigla(domain.getSigla())
                .giro(giroMapper.toDTO(domain.getGiro()))
                .direccion(DireccionResponse.fromDomain(domain.getDireccion()))
                .datoBancario(DatoBancarioResponse.fromDomain(domain.getDatoBancario()))
                .build();
    }

    public Cliente toDomain(ClienteDTO dto) {
        if (dto == null)
            return null;
        return Cliente.builder()
                .clienteId(dto.getClienteId())
                .razonSocial(dto.getRazonSocial())
                .runCliente(dto.getRunCliente())
                .correoCliente(dto.getCorreoCliente())
                .telefonoCliente(dto.getTelefonoCliente())
                .contactoCliente(dto.getContactoCliente())
                .activo(dto.isActivo())
                .sigla(dto.getSigla())
                .giro(giroMapper.toDomain(dto.getGiro()))
                .direccion(dto.getDireccion() != null
                        ? backend.com.shared.domain.model.Direccion.builder()
                                .direccionId(dto.getDireccion().getDireccionId())
                                .build()
                        : null)
                .datoBancario(dto.getDatoBancario() != null
                        ? backend.com.shared.domain.model.DatoBancario.builder()
                                .datoBancarioId(dto.getDatoBancario().getDatoBancarioId())
                                .build()
                        : null)
                .build();
    }

    public List<ClienteDTO> toDTOList(List<Cliente> clientes) {
        return clientes.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
