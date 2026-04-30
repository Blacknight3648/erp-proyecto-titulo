package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.gestionUsuarios.cliente.infrastructure.mapper.ClienteMapper;
import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.repository.ClienteRepository;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteValidator clienteValidator;

    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente) {

        clienteValidator.validateUniqueness(cliente.getRunCliente());

        ClienteJpaEntity entity = clienteMapper.toEntity(cliente);
        @SuppressWarnings("null")
        ClienteJpaEntity saved = clienteRepository.save(entity);
        return clienteMapper.toDomain(saved);
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {

        return clienteRepository.findById(id)
                .map(entity -> {

                    entity.setNombreCliente(clienteActualizado.getNombreCliente());
                    entity.setApellidoCliente(clienteActualizado.getApellidoCliente());
                    entity.setRunCliente(clienteActualizado.getRunCliente());
                    entity.setCorreoCliente(clienteActualizado.getCorreoCliente());
                    entity.setTelefonoCliente(clienteActualizado.getTelefonoCliente());
                    entity.setDireccionCliente(clienteActualizado.getDireccionCliente());
                    entity.setSegmento(clienteActualizado.getSegmento());
                    entity.setContacto(clienteActualizado.getContacto());
                    entity.setActivo(clienteActualizado.isActivo());

                    ClienteJpaEntity saved = clienteRepository.save(entity);
                    return clienteMapper.toDomain(saved);

                })
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
    }

    @SuppressWarnings("null")
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).map(clienteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerClientePorRun(String runCliente) {
        return clienteRepository.findByRunCliente(runCliente).map(clienteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toDomain)
                .collect(Collectors.toList());
    }

    @SuppressWarnings("null")
    @Override
    @Transactional
    public void eliminarCliente(Long id) {

        if (!clienteRepository.existsById(id)) {
            throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
        }

        clienteRepository.deleteById(id);
    }
}
