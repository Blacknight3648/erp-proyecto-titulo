package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.Articulo;
import backend.com.maestros.domain.enums.TipoArticulo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticuloRepository extends JpaRepository<Articulo, Integer> {

    Optional<Articulo> findByCodigoArticulo(String codigoArticulo);

    boolean existsByCodigoArticulo(String codigoArticulo);

    List<Articulo> findByActivoTrue();

    List<Articulo> findByTipoArticulo(TipoArticulo tipoArticulo);

    List<Articulo> findByNombreArticuloContainingIgnoreCase(String nombre);

}