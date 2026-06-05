package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.UnidadMedida;
import backend.com.shared.infrastructure.mapper.UnidadMedidaMapper;
import backend.com.shared.infrastructure.persistence.repository.UnidadMedidaRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.UnidadMedidaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UnidadMedidaRepositoryImpl implements UnidadMedidaRepository {

    private final UnidadMedidaJpaRepository jpaRepository;
    private final UnidadMedidaMapper mapper;

    @Override
    public List<UnidadMedida> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<UnidadMedida> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<UnidadMedida> findByNombreUnidad(String nombreUnidad) {
        return jpaRepository.findByNombreUnidad(nombreUnidad).map(mapper::toDomain);
    }

    @Override
    public Optional<UnidadMedida> findByAbreviatura(String abreviatura) {
        return jpaRepository.findByAbreviatura(abreviatura).map(mapper::toDomain);
    }

    @Override
    public boolean existsByAbreviatura(String abreviatura) {
        return jpaRepository.existsByAbreviatura(abreviatura);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public UnidadMedida save(UnidadMedida unidad) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(unidad)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}