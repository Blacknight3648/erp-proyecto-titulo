package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.TipoArticulo;

import java.util.List;
import java.util.Optional;

public interface TipoArticuloRepository {

    List<TipoArticulo> findAll();

    Optional<TipoArticulo> findById(Integer id);

    Optional<TipoArticulo> findByCodigo(String codigo);

    boolean existsById(Integer id);

    boolean existsByCodigo(String codigo);

    TipoArticulo save(TipoArticulo tipoArticulo);

    void deleteById(Integer id);
}
