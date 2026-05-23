package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.exception.ClienteNotFoundException;
import backend.com.gestionUsuarios.cliente.infrastructure.mapper.ClienteMapper;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.repository.ClienteRepository;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.DatoBancarioNotFoundException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.entity.DatoBancarioJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.DatoBancarioJpaRepository;
import backend.com.shared.infrastructure.persistence.repository.DireccionJpaRepository;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
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
    private final GiroRepository giroRepository;
    private final DireccionJpaRepository direccionJpaRepository;
    private final DatoBancarioJpaRepository datoBancarioJpaRepository;
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

        ClienteJpaEntity entity = ClienteJpaEntity.builder()
                .runCliente(cliente.getRunCliente())
                .razonSocial(cliente.getRazonSocial())
                .contactoCliente(cliente.getContactoCliente())
                .correoCliente(cliente.getCorreoCliente())
                .telefonoCliente(cliente.getTelefonoCliente())
                .sigla(cliente.getSigla())
                .activo(cliente.isActivo())
                .giro(resolverGiro(cliente))
                .direccion(resolverDireccion(cliente))
                .datoBancario(resolverDatoBancario(cliente))
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
        if (cliente.getRazonSocial() != null)
            entity.setRazonSocial(cliente.getRazonSocial());
        if (cliente.getContactoCliente() != null)
            entity.setContactoCliente(cliente.getContactoCliente());
        if (cliente.getCorreoCliente() != null)
            entity.setCorreoCliente(cliente.getCorreoCliente());
        if (cliente.getTelefonoCliente() != null)
            entity.setTelefonoCliente(cliente.getTelefonoCliente());
        if (cliente.getSigla() != null)
            entity.setSigla(cliente.getSigla());
        entity.setActivo(cliente.isActivo());

        GiroJpaEntity giro = resolverGiro(cliente);
        if (giro != null)
            entity.setGiro(giro);

        DireccionJpaEntity direccion = resolverDireccion(cliente);
        if (direccion != null)
            entity.setDireccion(direccion);

        DatoBancarioJpaEntity datoBancario = resolverDatoBancario(cliente);
        if (datoBancario != null)
            entity.setDatoBancario(datoBancario);

        return clienteMapper.toDomain(clienteRepository.save(entity));
    }

    @Override
    public void eliminar(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException(clienteId);
        }
        clienteRepository.deleteById(clienteId);
    }

    private GiroJpaEntity resolverGiro(Cliente cliente) {
        if (cliente.getGiro() == null || cliente.getGiro().getGiroId() == null)
            return null;
        Long giroId = cliente.getGiro().getGiroId();
        return giroRepository.findById(giroId)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + giroId));
    }

    private DireccionJpaEntity resolverDireccion(Cliente cliente) {
        if (cliente.getDireccion() == null || cliente.getDireccion().getDireccionId() == null)
            return null;
        Long direccionId = cliente.getDireccion().getDireccionId();
        return direccionJpaRepository.findById(direccionId)
                .orElseThrow(() -> new EntityNotFoundException("Dirección no encontrada con ID: " + direccionId));
    }

    private DatoBancarioJpaEntity resolverDatoBancario(Cliente cliente) {
        if (cliente.getDatoBancario() == null || cliente.getDatoBancario().getDatoBancarioId() == null)
            return null;
        Integer datoBancarioId = cliente.getDatoBancario().getDatoBancarioId();
        return datoBancarioJpaRepository.findById(datoBancarioId)
                .orElseThrow(() -> new DatoBancarioNotFoundException(datoBancarioId));
    }
}
