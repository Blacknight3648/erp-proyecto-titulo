package backend.com.gestionUsuarios.cliente.infrastructure.mapper;

import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClienteMapper {

    public Cliente toDomain(ClienteJpaEntity entity) {

        if (entity == null) {
            return null;
        }

        return Cliente.builder()
                .clienteId(entity.getClienteId())
                .nombreCliente(entity.getNombreCliente())
                .apellidoCliente(entity.getApellidoCliente())
                .runCliente(entity.getRunCliente())
                .correoCliente(entity.getCorreoCliente())
                .telefonoCliente(entity.getTelefonoCliente())
                .direccionCliente(entity.getDireccionCliente())
                .segmento(entity.getSegmento())
                .contacto(entity.getContacto())
                .activo(entity.isActivo())
                .build();
    }

    public ClienteJpaEntity toEntity(Cliente domain) {

        if (domain == null) {
            return null;
        }

        return ClienteJpaEntity.builder()
                .clienteId(domain.getClienteId())
                .nombreCliente(domain.getNombreCliente())
                .apellidoCliente(domain.getApellidoCliente())
                .runCliente(domain.getRunCliente())
                .correoCliente(domain.getCorreoCliente())
                .telefonoCliente(domain.getTelefonoCliente())
                .direccionCliente(domain.getDireccionCliente())
                .segmento(domain.getSegmento())
                .contacto(domain.getContacto())
                .activo(domain.isActivo())
                .build();
    }

    public List<Cliente> toDomainList(List<ClienteJpaEntity> entities) {
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    public List<ClienteJpaEntity> toEntityList(List<Cliente> domains) {
        return domains.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

}
