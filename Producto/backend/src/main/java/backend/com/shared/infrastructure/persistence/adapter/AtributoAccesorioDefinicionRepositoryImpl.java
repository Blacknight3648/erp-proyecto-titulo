package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.AtributoAccesorioDefinicion;
import backend.com.shared.infrastructure.mapper.AtributoAccesorioDefinicionMapper;
import backend.com.shared.infrastructure.persistence.repository.AtributoAccesorioDefinicionRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.AtributoAccesorioDefinicionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AtributoAccesorioDefinicionRepositoryImpl implements AtributoAccesorioDefinicionRepository {

    private final AtributoAccesorioDefinicionJpaRepository jpaRepository;
    private final AtributoAccesorioDefinicionMapper mapper;

    @Override
    public List<AtributoAccesorioDefinicion> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<AtributoAccesorioDefinicion> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<AtributoAccesorioDefinicion> findByTipoAccesorioIdOrderByOrdenAsc(Integer idTipoAccesorio) {
        return jpaRepository.findByTipoAccesorio_IdTipoAccesorioOrderByOrdenAsc(idTipoAccesorio)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public AtributoAccesorioDefinicion save(AtributoAccesorioDefinicion definicion) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(definicion)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
