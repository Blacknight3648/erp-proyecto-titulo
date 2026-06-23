package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Precio;

import java.util.List;
import java.util.Optional;

public interface PrecioRepository {

    Optional<Precio> findById(Integer id);

    boolean existsById(Integer id);

    List<Precio> findByArticuloId(Integer idArticulo);

    Optional<Precio> findByArticuloIdAndTipoPrecio(Integer idArticulo, String tipoPrecio);

    List<Precio> findByArticuloIdAndMonedaId(Integer idArticulo, Integer idMoneda);

    Precio save(Precio precio);

    void deleteById(Integer id);

    void deleteByArticuloId(Integer idArticulo);
}