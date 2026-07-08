package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Rubro;
import backend.com.shared.infrastructure.persistence.repository.RubroRepository;
import backend.com.shared.infrastructure.mapper.RubroMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.RubroJpaRepository;
import backend.com.shared.infrastructure.persistence.entity.RubroJpaEntity;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RubroRepositoryImpl implements RubroRepository {

    private final RubroJpaRepository jpaRepository;
    private final RubroMapper mapper;

    @Override
    public List<Rubro> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Rubro> findById(Long id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Rubro> findByNombreRubro(String nombreRubro) {
        if (nombreRubro == null)
            return Optional.empty();
        return jpaRepository.findByNombreRubro(nombreRubro).map(mapper::toDomain);
    }

    @Override
    public Optional<Rubro> findBySiglaRubro(String siglaRubro) {
        if (siglaRubro == null)
            return Optional.empty();
        return jpaRepository.findBySiglaRubro(siglaRubro).map(mapper::toDomain);
    }

    @Override
    public Rubro save(Rubro rubro) {
        RubroJpaEntity entity = mapper.toEntity(rubro);
        RubroJpaEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public void deleteById(Long id) {
        if (id == null)
            return;
        jpaRepository.deleteById(id);
    }

    @Override
    public List<Rubro> findAllByOrderByNombreRubroAsc() {
        return jpaRepository.findAllByOrderByNombreRubroAsc().stream()
                .map(mapper::toDomain)
                .toList();
    }
}
