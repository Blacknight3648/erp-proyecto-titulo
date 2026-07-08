package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.TipoAccesorio;
import backend.com.shared.infrastructure.mapper.TipoAccesorioMapper;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoAccesorioJpaRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoAccesorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoAccesorioRepositoryImpl implements TipoAccesorioRepository {

    private final TipoAccesorioJpaRepository jpaRepository;
    private final TipoAccesorioMapper mapper;

    @Override
    public List<TipoAccesorio> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<TipoAccesorio> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        return jpaRepository.existsByCodigo(codigo);
    }

    @Override
    public boolean existsByNombreIgnoreCase(String nombre) {
        return jpaRepository.existsByNombreIgnoreCase(nombre);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public TipoAccesorio save(TipoAccesorio tipoAccesorio) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tipoAccesorio)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
