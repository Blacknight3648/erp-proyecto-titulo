package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Composicion;
import backend.com.shared.infrastructure.mapper.ComposicionMapper;
import backend.com.shared.infrastructure.persistence.repository.ComposicionRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ComposicionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ComposicionRepositoryImpl implements ComposicionRepository {

    private final ComposicionJpaRepository jpaRepository;
    private final ComposicionMapper mapper;

    @Override
    public List<Composicion> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Composicion> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Composicion> findByCodigoComposicion(String codigoComposicion) {
        return jpaRepository.findByCodigoComposicion(codigoComposicion).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoComposicion(String codigoComposicion) {
        return jpaRepository.existsByCodigoComposicion(codigoComposicion);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<Composicion> findByClasificacionIgnoreCase(String clasificacion) {
        return jpaRepository.findByClasificacionIgnoreCase(clasificacion)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Composicion save(Composicion composicion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(composicion)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}