package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.exception.ClienteNotFoundException;
import backend.com.gestionUsuarios.cliente.infrastructure.mapper.ClienteMapper;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.repository.ClienteRepository;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.SiglaJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import backend.com.shared.infrastructure.persistence.repository.SiglaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final SiglaRepository siglaRepository;
    private final GiroRepository giroRepository;
    private final ClienteMapper clienteMapper;
    private final ClienteValidator clienteValidator;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll().stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long clienteId) {
        return clienteRepository.findById(clienteId).map(clienteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorRun(String runCliente) {
        return clienteRepository.findByRunCliente(runCliente).map(clienteMapper::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorRazonSocial(String razonSocial) {
        return clienteRepository.buscarPorRazonSocial(razonSocial).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivos() {
        return clienteRepository.findByActivoTrue().stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerInactivos() {
        return clienteRepository.findByActivoFalse().stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorSiglaId(Long siglaId) {
        return clienteRepository.obtenerPorSiglaId(siglaId).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorGiroId(Long giroId) {
        return clienteRepository.obtenerPorGiroId(giroId).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorDescripcionSigla(String descripcionSigla) {
        return clienteRepository.obtenerPorDescripcionSigla(descripcionSigla)
                .stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro) {
        return clienteRepository.obtenerPorDescripcionGiro(descripcionGiro)
                .stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivosPorSigla(Long siglaId) {
        return clienteRepository.obtenerActivosPorSigla(siglaId).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivosPorGiro(Long giroId) {
        return clienteRepository.obtenerActivosPorGiro(giroId).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    public Cliente crear(Cliente cliente) {
        clienteValidator.validateUniqueness(cliente.getRunCliente());

        SiglaJpaEntity sigla = resolverSigla(cliente);
        GiroJpaEntity giro = resolverGiro(cliente);

        ClienteJpaEntity entity = ClienteJpaEntity.builder()
                .runCliente(cliente.getRunCliente())
                .razonSocial(cliente.getRazonSocial())
                .direccionCliente(cliente.getDireccionCliente())
                .contactoCliente(cliente.getContactoCliente())
                .correoCliente(cliente.getCorreoCliente())
                .telefonoCliente(cliente.getTelefonoCliente())
                .activo(cliente.isActivo())
                .sigla(sigla)
                .giro(giro)
                .build();

        return clienteMapper.toDomain(clienteRepository.save(entity));
    }

    @Override
    public Cliente actualizar(Long clienteId, Cliente cliente) {
        ClienteJpaEntity entity = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));

        if (cliente.getRunCliente() != null && !cliente.getRunCliente().equals(entity.getRunCliente())) {
            clienteValidator.validateUniqueness(cliente.getRunCliente());
            entity.setRunCliente(cliente.getRunCliente());
        }
        if (cliente.getRazonSocial() != null) entity.setRazonSocial(cliente.getRazonSocial());
        if (cliente.getDireccionCliente() != null) entity.setDireccionCliente(cliente.getDireccionCliente());
        if (cliente.getContactoCliente() != null) entity.setContactoCliente(cliente.getContactoCliente());
        if (cliente.getCorreoCliente() != null) entity.setCorreoCliente(cliente.getCorreoCliente());
        if (cliente.getTelefonoCliente() != null) entity.setTelefonoCliente(cliente.getTelefonoCliente());
        entity.setActivo(cliente.isActivo());

        SiglaJpaEntity sigla = resolverSigla(cliente);
        if (sigla != null) entity.setSigla(sigla);

        GiroJpaEntity giro = resolverGiro(cliente);
        if (giro != null) entity.setGiro(giro);

        return clienteMapper.toDomain(clienteRepository.save(entity));
    }

    @Override
    public void eliminar(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException(clienteId);
        }
        clienteRepository.deleteById(clienteId);
    }

    private SiglaJpaEntity resolverSigla(Cliente cliente) {
        if (cliente.getSigla() == null || cliente.getSigla().getSiglaId() == null) return null;
        Long siglaId = cliente.getSigla().getSiglaId();
        return siglaRepository.findById(siglaId)
                .orElseThrow(() -> new EntityNotFoundException("Sigla no encontrada con ID: " + siglaId));
    }

    private GiroJpaEntity resolverGiro(Cliente cliente) {
        if (cliente.getGiro() == null || cliente.getGiro().getGiroId() == null) return null;
        Long giroId = cliente.getGiro().getGiroId();
        return giroRepository.findById(giroId)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + giroId));
    }
}
