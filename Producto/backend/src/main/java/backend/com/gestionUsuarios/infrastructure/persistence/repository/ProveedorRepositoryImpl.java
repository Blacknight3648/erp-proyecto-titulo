package backend.com.gestionUsuarios.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.domain.model.Proveedor;
import backend.com.gestionUsuarios.infrastructure.mapper.ProveedorMapper;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProveedorRepositoryImpl implements ProveedorRepository {

    private final ProveedorJpaRepository jpaRepository;
    private final ProveedorMapper mapper;

    @Override
    public List<Proveedor> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Proveedor> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Proveedor> findByRunProveedor(String runProveedor) {
        return jpaRepository.findByRunProveedor(runProveedor).map(mapper::toDomain);
    }

    @Override
    public boolean existsByRunProveedor(String runProveedor) {
        return jpaRepository.existsByRunProveedor(runProveedor);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<Proveedor> buscarPorRazonSocial(String razonSocial) {
        return jpaRepository.buscarPorRazonSocial(razonSocial).stream().map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerPorSigla(String sigla) {
        return jpaRepository.obtenerPorSigla(sigla).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerPorGiroId(Long giroId) {
        return jpaRepository.obtenerPorGiroId(giroId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerPorDescripcionGiro(String descripcionGiro) {
        return jpaRepository.obtenerPorDescripcionGiro(descripcionGiro).stream().map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerActivos() {
        return jpaRepository.findByActivoTrue().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerInactivos() {
        return jpaRepository.findByActivoFalse().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerActivosPorSigla(String sigla) {
        return jpaRepository.obtenerActivosPorSigla(sigla).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Proveedor> obtenerActivosPorGiro(Long giroId) {
        return jpaRepository.obtenerActivosPorGiro(giroId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Proveedor save(Proveedor proveedor) {
        if (proveedor == null) {
            throw new IllegalArgumentException("El proveedor no puede ser nulo");
        }
        ProveedorJpaEntity entity = mapper.toEntity(proveedor);
        if (entity == null) {
            throw new IllegalStateException("Error al mapear proveedor a entidad");
        }
        return mapper.toDomain(jpaRepository.save(entity));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) {
            jpaRepository.deleteById(id);
        }
    }
}
