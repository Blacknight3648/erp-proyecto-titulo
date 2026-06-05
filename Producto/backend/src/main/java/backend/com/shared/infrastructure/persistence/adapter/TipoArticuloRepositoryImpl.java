package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.TipoArticulo;
import backend.com.shared.infrastructure.mapper.TipoArticuloMapper;
import backend.com.shared.infrastructure.persistence.repository.TipoArticuloRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.TipoArticuloJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TipoArticuloRepositoryImpl implements TipoArticuloRepository {

    private final TipoArticuloJpaRepository jpaRepository;
    private final TipoArticuloMapper mapper;

    @Override
    public List<TipoArticulo> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<TipoArticulo> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<TipoArticulo> findByCodigo(String codigo) {
        if (codigo == null) return Optional.empty();
        backend.com.shared.domain.enums.TipoArticulo codigoEnum =
                backend.com.shared.domain.enums.TipoArticulo.valueOf(codigo);
        return jpaRepository.findByCodigo(codigoEnum).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public boolean existsByCodigo(String codigo) {
        if (codigo == null) return false;
        try {
            backend.com.shared.domain.enums.TipoArticulo codigoEnum =
                    backend.com.shared.domain.enums.TipoArticulo.valueOf(codigo);
            return jpaRepository.findByCodigo(codigoEnum).isPresent();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public TipoArticulo save(TipoArticulo tipoArticulo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(tipoArticulo)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
