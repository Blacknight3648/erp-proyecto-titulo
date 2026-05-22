package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Pais;
import backend.com.shared.domain.repository.PaisRepository;
import backend.com.shared.infrastructure.mapper.PaisMapper;
import backend.com.shared.infrastructure.persistence.repository.PaisJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PaisRepositoryImpl implements PaisRepository {

    private final PaisJpaRepository jpaRepository;
    private final PaisMapper mapper;

    @Override
    public List<Pais> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Pais> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Pais> findByNombrePais(String nombrePais) {
        return jpaRepository.findByNombrePais(nombrePais).map(mapper::toDomain);
    }

    @Override
    public Pais save(Pais pais) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(pais)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }
}
