package backend.com.gestionUsuarios.infrastructure.persistence.adapter;

import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.gestionUsuarios.domain.repository.RoleRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.RoleMapper;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.RoleJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.RoleJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleJpaRepository jpaRepository;
    private final RoleMapper mapper;

    @Override
    public Role save(Role role) {
        if (role == null) {
            throw new ValidationException("El rol no puede ser nulo");
        }
        RoleJpaEntity entity = mapper.toJpaEntity(role);
        RoleJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Role> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Role> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Role> findByNombre(String nombre) {
        if (nombre == null) return Optional.empty();
        return jpaRepository.findByNombre(nombre).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNombre(String nombre) {
        if (nombre == null) return false;
        return jpaRepository.existsByNombre(nombre);
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
