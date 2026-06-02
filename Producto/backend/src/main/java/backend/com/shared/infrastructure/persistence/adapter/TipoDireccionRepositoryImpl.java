package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.infrastructure.persistence.repository.TipoDireccionRepository;
import backend.com.shared.infrastructure.mapper.TipoDireccionMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoDireccionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TipoDireccionRepositoryImpl implements TipoDireccionRepository {

    private final TipoDireccionJpaRepository jpaRepository;
    private final TipoDireccionMapper mapper;

    @Override
    public List<TipoDireccion> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<TipoDireccion> findById(Integer id) {
        if (id == null)
            return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public TipoDireccion save(TipoDireccion tipoDireccion) {
        if (tipoDireccion.getTipoDireccionId() != null) {
            return jpaRepository.findById(tipoDireccion.getTipoDireccionId())
                    .map(entity -> {
                        entity.setDescripcion(tipoDireccion.getDescripcion());
                        return mapper.toDomain(jpaRepository.save(entity));
                    })
                    .orElseGet(() -> mapper.toDomain(jpaRepository.save(mapper.toEntity(tipoDireccion))));
        }
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tipoDireccion)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null)
            jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }
}
