package backend.com.maestros.repository;

import backend.com.maestros.domain.entity.catalogo.Composicion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComposicionRepository extends JpaRepository<Composicion, Integer> {

    Optional<Composicion> findByCodigoComposicion(String codigoComposicion);

    boolean existsByCodigoComposicion(String codigoComposicion);

    /** Filtrar por clasificación (ej: "NATURAL", "SINTETICO", "MIXTO"). */
    List<Composicion> findByClasificacionIgnoreCase(String clasificacion);
}
