package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Direccion;
import backend.com.shared.domain.repository.DireccionRepository;
import backend.com.shared.infrastructure.mapper.DireccionMapper;
import backend.com.shared.infrastructure.persistence.repository.DireccionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DireccionRepositoryImpl implements DireccionRepository {

    private final DireccionJpaRepository jpaRepository;
    private final DireccionMapper mapper;

    @Override
    public List<Direccion> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Direccion> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Direccion> findByComunaId(Long comunaId) {
        return jpaRepository.findByComuna_ComunaId(comunaId).stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Direccion save(Direccion direccion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(direccion)));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }
}
