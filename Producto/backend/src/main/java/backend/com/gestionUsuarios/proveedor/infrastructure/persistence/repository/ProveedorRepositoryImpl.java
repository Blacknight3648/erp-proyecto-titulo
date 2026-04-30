package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.proveedor.domain.model.Proveedor;
import backend.com.gestionUsuarios.proveedor.infrastructure.mapper.ProveedorMapper;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
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
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Proveedor> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Proveedor> findByRutProveedor(String rutProveedor) {
        return jpaRepository.findByRutProveedor(rutProveedor).map(mapper::toDomain);
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
