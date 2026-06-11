package backend.com.gestionUsuarios.infrastructure.persistence.adapter;

import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.AreaMapper;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.AreaJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.AreaJpaRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AreaRepositoryImpl implements AreaRepository {

    private final AreaJpaRepository jpaRepository;
    private final AreaMapper mapper;

    @Override
    public Area save(Area area) {
        if (area == null) {
            throw new ValidationException("El área no puede ser nula");
        }
        AreaJpaEntity entity = mapper.toJpaEntity(area);
        AreaJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Area> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Area> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Area> findByNombre(String nombre) {
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
