package backend.com.gestionUsuarios.infrastructure.persistence.adapter;

import backend.com.gestionUsuarios.domain.model.Cliente;
import backend.com.gestionUsuarios.domain.repository.ClienteRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.ClienteMapper;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.ClienteJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClienteRepositoryImpl implements ClienteRepository {

    private final ClienteJpaRepository jpaRepository;
    private final ClienteMapper mapper;

    @Override
    public Cliente save(Cliente cliente) {
        if (cliente == null) {
            throw new ValidationException("El cliente no puede ser nulo");
        }
        ClienteJpaEntity entity = mapper.toJpaEntity(cliente);
        ClienteJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Cliente> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Cliente> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Cliente> findByRunCliente(String runCliente) {
        if (runCliente == null) return Optional.empty();
        return jpaRepository.findByRunCliente(runCliente).map(mapper::toDomain);
    }

    @Override
    public boolean existsByRunCliente(String runCliente) {
        if (runCliente == null) return false;
        return jpaRepository.existsByRunCliente(runCliente);
    }

    @Override
    public List<Cliente> buscarPorRazonSocial(String razonSocial) {
        return jpaRepository.buscarPorRazonSocial(razonSocial).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerPorSigla(String sigla) {
        return jpaRepository.obtenerPorSigla(sigla).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerPorGiroId(Long giroId) {
        return jpaRepository.obtenerPorGiroId(giroId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerPorDescripcionGiro(String descripcionGiro) {
        return jpaRepository.obtenerPorDescripcionGiro(descripcionGiro).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> findByActivoTrue() {
        return jpaRepository.findByActivoTrue().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> findByActivoFalse() {
        return jpaRepository.findByActivoFalse().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerActivosPorSigla(String sigla) {
        return jpaRepository.obtenerActivosPorSigla(sigla).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Cliente> obtenerActivosPorGiro(Long giroId) {
        return jpaRepository.obtenerActivosPorGiro(giroId).stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        if (id == null) return false;
        return jpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            jpaRepository.deleteById(id);
        }
    }
}
