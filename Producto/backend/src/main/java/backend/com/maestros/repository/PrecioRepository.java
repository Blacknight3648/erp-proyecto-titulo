package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.Precio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrecioRepository extends JpaRepository<Precio, Integer> {

    /** Todos los precios de un artículo (distintos tipos de precio y monedas). */
    List<Precio> findByArticulo_IdArticulo(Integer idArticulo);

    /** Precio de un artículo filtrado por tipo de precio (ej: "VENTA", "COSTO"). */
    Optional<Precio> findByArticulo_IdArticuloAndTipoPrecio(Integer idArticulo, String tipoPrecio);

    /** Precios de un artículo en una moneda específica. */
    List<Precio> findByArticulo_IdArticuloAndMoneda_IdMoneda(Integer idArticulo, Integer idMoneda);

    void deleteByArticulo_IdArticulo(Integer idArticulo);
}
