package backend.com.gestionUsuarios.cliente.application.service;

import backend.com.comercial.infrastructure.persistence.repository.EvaluacionNegocioJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.NotaVentaJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCotizacionJpaRepository;
import backend.com.gestionUsuarios.cliente.domain.model.Cliente;
import backend.com.gestionUsuarios.cliente.infrastructure.exception.ClienteNotFoundException;
import backend.com.gestionUsuarios.cliente.infrastructure.mapper.ClienteMapper;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.repository.ClienteRepository;
import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ContactoJpaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.DireccionJpaRepository;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ContactoMapper;
import backend.com.shared.infrastructure.mapper.DireccionMapper;
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
    private final ContactoJpaRepository contactoJpaRepository;
    private final ContactoMapper contactoMapper;
    private final DireccionMapper direccionMapper;
    private final ClienteMapper clienteMapper;
    private final ClienteValidator clienteValidator;
    private final EvaluacionNegocioJpaRepository evaluacionNegocioRepository;
    private final NotaVentaJpaRepository notaVentaRepository;
    private final SolicitudCostosJpaRepository solicitudCostosRepository;
    private final SolicitudCotizacionJpaRepository solicitudCotizacionRepository;

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
    public List<Cliente> obtenerPorSigla(String sigla) {
        return clienteRepository.obtenerPorSigla(sigla).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivosPorSigla(String sigla) {
        return clienteRepository.obtenerActivosPorSigla(sigla).stream().map(clienteMapper::toDomain).toList();
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
    public List<Cliente> obtenerPorGiroId(Long giroId) {
        return clienteRepository.obtenerPorGiroId(giroId).stream().map(clienteMapper::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro) {
        return clienteRepository.obtenerPorDescripcionGiro(descripcionGiro)
                .stream().map(clienteMapper::toDomain).toList();
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
                .sigla(cliente.getSigla())
                .activo(cliente.isActivo())
                .giro(resolverGiro(cliente))
                .contacto(cliente.getContactos().stream().map(contactoMapper::toEntity).map(contactoJpaRepository::save)
                        .toList())
                .direccion(cliente.getDirecciones().stream().map(direccionMapper::toEntity)
                        .map(direccionJpaRepository::save).toList())
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
        if (cliente.getSigla() != null)
            entity.setSigla(cliente.getSigla());
        entity.setActivo(cliente.isActivo());
        entity.setContacto(cliente.getContactos().stream().map(contactoMapper::toEntity)
                .map(contactoJpaRepository::save).toList());
        entity.setDireccion(cliente.getDirecciones().stream().map(direccionMapper::toEntity)
                .map(direccionJpaRepository::save).toList());

        GiroJpaEntity giro = resolverGiro(cliente);
        if (giro != null)
            entity.setGiro(giro);

        DireccionJpaEntity direccion = resolverDireccion(cliente);
        if (direccion != null)
            entity.setDireccion(List.of(direccion));

        return clienteMapper.toDomain(clienteRepository.save(entity));
    }

    @Override
    public void eliminar(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ClienteNotFoundException(clienteId);
        }
        notaVentaRepository.deleteByCliente_ClienteId(clienteId);
        evaluacionNegocioRepository.deleteByCliente_ClienteId(clienteId);
        solicitudCostosRepository.deleteByCliente_ClienteId(clienteId);
        solicitudCotizacionRepository.deleteByCliente_ClienteId(clienteId);
        clienteRepository.deleteById(clienteId);
    }

    private GiroJpaEntity resolverGiro(Cliente cliente) {
        if (cliente.getGiro() == null || cliente.getGiro().getGiroId() == null)
            return null;
        Long giroId = cliente.getGiro().getGiroId();
        return giroRepository.findById(giroId)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + giroId));
    }

    private ContactoJpaEntity resolverContacto(Cliente cliente) {
        if (cliente.getContactos() == null || cliente.getContactos().get(0).getContactoId() == null)
            return null;
        Long contactoId = cliente.getContactos().get(0).getContactoId();
        return contactoJpaRepository.findById(contactoId)
                .orElseThrow(() -> new EntityNotFoundException("Contacto no encontrado con ID: " + contactoId));
    }

    private DireccionJpaEntity resolverDireccion(Cliente cliente) {
        if (cliente.getDirecciones() == null || cliente.getDirecciones().get(0).getDireccionId() == null)
            return null;
        Long direccionId = cliente.getDirecciones().get(0).getDireccionId();
        return direccionJpaRepository.findById(direccionId)
                .orElseThrow(() -> new EntityNotFoundException("Dirección no encontrada con ID: " + direccionId));
    }

}
