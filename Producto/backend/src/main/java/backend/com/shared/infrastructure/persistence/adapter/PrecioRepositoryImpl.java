package backend.com.shared.infrastructure.persistence.adapter;

import backend.com.shared.domain.model.Precio;
import backend.com.shared.infrastructure.mapper.PrecioMapper;
import backend.com.shared.infrastructure.persistence.repository.PrecioRepository;
import backend.com.shared.infrastructure.persistence.repository.Jpa.PrecioJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PrecioRepositoryImpl implements PrecioRepository {

    private final PrecioJpaRepository jpaRepository;
    private final PrecioMapper mapper;

    @Override
    public Optional<Precio> findById(Integer id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public boolean existsById(Integer id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public List<Precio> findByArticuloId(Integer idArticulo) {
        return jpaRepository.findByArticulo_IdArticulo(idArticulo)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Precio> findByArticuloIdAndTipoPrecio(Integer idArticulo, String tipoPrecio) {
        return jpaRepository.findByArticulo_IdArticuloAndTipoPrecio(idArticulo, tipoPrecio)
                .map(mapper::toDomain);
    }

    @Override
    public List<Precio> findByArticuloIdAndMonedaId(Integer idArticulo, Integer idMoneda) {
        return jpaRepository.findByArticulo_IdArticuloAndMoneda_IdMoneda(idArticulo, idMoneda)
                .stream().map(mapper::toDomain).toList();
    }

    @Override
    public Precio save(Precio precio) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(precio)));
    }

    @Override
    public void deleteById(Integer id) {
        if (id != null) jpaRepository.deleteById(id);
    }

    @Override
    public void deleteByArticuloId(Integer idArticulo) {
        jpaRepository.deleteByArticulo_IdArticulo(idArticulo);
    }
}