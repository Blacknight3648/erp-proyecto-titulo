package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.domain.model.Articulo;

import java.util.List;
import java.util.Optional;

public interface ArticuloRepository {

    List<Articulo> findAll();

    Optional<Articulo> findById(Integer id);

    Optional<Articulo> findByCodigoArticulo(String codigoArticulo);

    boolean existsByCodigoArticulo(String codigoArticulo);

    boolean existsById(Integer id);

    List<Articulo> findByActivoTrue();

    List<Articulo> findByTipoArticulo(TipoArticulo tipoArticulo);

    List<Articulo> findByNombreArticuloContainingIgnoreCase(String nombre);

    Articulo save(Articulo articulo);

    void deleteById(Integer id);
}