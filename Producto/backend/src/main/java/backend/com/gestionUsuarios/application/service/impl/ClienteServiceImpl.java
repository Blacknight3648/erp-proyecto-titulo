package backend.com.gestionUsuarios.application.service.impl;

import backend.com.comercial.infrastructure.persistence.repository.EvaluacionNegocioJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.NotaVentaJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCotizacionJpaRepository;
import backend.com.gestionUsuarios.application.service.ClienteService;
import backend.com.gestionUsuarios.application.service.ClienteValidator;
import backend.com.gestionUsuarios.domain.model.Cliente;
import backend.com.gestionUsuarios.domain.repository.ClienteRepository;
import backend.com.gestionUsuarios.infrastructure.exception.ClienteNotFoundException;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GiroMapper;
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
    private final GiroMapper giroMapper;
    private final ClienteValidator clienteValidator;
    private final EvaluacionNegocioJpaRepository evaluacionNegocioRepository;
    private final NotaVentaJpaRepository notaVentaRepository;
    private final SolicitudCostosJpaRepository solicitudCostosRepository;
    private final SolicitudCotizacionJpaRepository solicitudCotizacionRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorRun(String runCliente) {
        return clienteRepository.findByRunCliente(runCliente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarPorRazonSocial(String razonSocial) {
        return clienteRepository.buscarPorRazonSocial(razonSocial);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorSigla(String sigla) {
        return clienteRepository.obtenerPorSigla(sigla);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivosPorSigla(String sigla) {
        return clienteRepository.obtenerActivosPorSigla(sigla);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivos() {
        return clienteRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerInactivos() {
        return clienteRepository.findByActivoFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorGiroId(Long giroId) {
        return clienteRepository.obtenerPorGiroId(giroId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro) {
        return clienteRepository.obtenerPorDescripcionGiro(descripcionGiro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> obtenerActivosPorGiro(Long giroId) {
        return clienteRepository.obtenerActivosPorGiro(giroId);
    }

    @Override
    public Cliente crear(Cliente cliente) {
        clienteValidator.validateUniqueness(cliente.getRunCliente());

        cliente.setGiro(resolverGiro(cliente.getGiro()));
        cliente.setClienteId(null);

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long clienteId, Cliente cliente) {
        Cliente existente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ClienteNotFoundException(clienteId));

        if (cliente.getRunCliente() != null && !cliente.getRunCliente().equals(existente.getRunCliente())) {
            clienteValidator.validateUniqueness(cliente.getRunCliente());
            existente.setRunCliente(cliente.getRunCliente());
        }
        if (cliente.getRazonSocial() != null)
            existente.setRazonSocial(cliente.getRazonSocial());
        if (cliente.getSigla() != null)
            existente.setSigla(cliente.getSigla());
        existente.setActivo(cliente.isActivo());

        if (cliente.getContactos() != null) {
            existente.setContactos(cliente.getContactos());
        }
        if (cliente.getDirecciones() != null) {
            existente.setDirecciones(cliente.getDirecciones());
        }

        Giro giro = resolverGiro(cliente.getGiro());
        if (giro != null)
            existente.setGiro(giro);

        return clienteRepository.save(existente);
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

    private Giro resolverGiro(Giro giro) {
        if (giro == null || giro.getGiroId() == null)
            return null;
        Long giroId = giro.getGiroId();
        return giroRepository.findById(giroId)
                .map(giroMapper::toDomain)
                .orElseThrow(() -> new EntityNotFoundException("Giro no encontrado con ID: " + giroId));
    }
}
