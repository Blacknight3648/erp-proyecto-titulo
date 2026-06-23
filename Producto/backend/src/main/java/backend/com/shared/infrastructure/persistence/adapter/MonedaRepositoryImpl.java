package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Moneda;
import backend.com.shared.infrastructure.mapper.MonedaMapper;
import backend.com.shared.infrastructure.persistence.repository.MonedaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.MonedaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MonedaRepositoryImpl implements MonedaRepository {

    private final MonedaJpaRepository jpaRepository;
    private final MonedaMapper mapper;

    @Override
    public List<Moneda> findAll() {
        return jpaRepository.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Moneda> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Moneda> findByCodigoMoneda(String codigoMoneda) {
        return jpaRepository.findByCodigoMoneda(codigoMoneda).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoMoneda(String codigoMoneda) {
        return jpaRepository.existsByCodigoMoneda(codigoMoneda);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public Moneda save(Moneda moneda) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(moneda)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}