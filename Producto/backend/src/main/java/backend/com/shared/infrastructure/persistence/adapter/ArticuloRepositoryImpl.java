package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.infrastructure.mapper.ArticuloMapper;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.ArticuloJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ArticuloRepositoryImpl implements ArticuloRepository {

    private final ArticuloJpaRepository jpaRepository;
    private final ArticuloMapper mapper;

    @Override
    public List<Articulo> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Articulo> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Articulo> findByCodigoArticulo(String codigoArticulo) {
        return jpaRepository.findByCodigoArticulo(codigoArticulo).map(mapper::toDomain);
    }

    @Override
    public boolean existsByCodigoArticulo(String codigoArticulo) {
        return jpaRepository.existsByCodigoArticulo(codigoArticulo);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<Articulo> findByActivoTrue() {
        return jpaRepository.findByActivoTrue().stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Articulo> findByTipoArticulo(TipoArticulo tipoArticulo) {
        return jpaRepository.findByTipoArticulo_Codigo(tipoArticulo).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Articulo> findByNombreArticuloContainingIgnoreCase(String nombre) {
        return jpaRepository.findByNombreArticuloContainingIgnoreCase(nombre)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Articulo save(Articulo articulo) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(articulo)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}