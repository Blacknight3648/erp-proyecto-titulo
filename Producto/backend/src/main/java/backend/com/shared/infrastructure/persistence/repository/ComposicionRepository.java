package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Composicion;

import java.util.List;
import java.util.Optional;

public interface ComposicionRepository {

    List<Composicion> findAll();

    Optional<Composicion> findById(Integer id);

    Optional<Composicion> findByCodigoComposicion(String codigoComposicion);

    boolean existsByCodigoComposicion(String codigoComposicion);

    boolean existsById(Integer id);

    List<Composicion> findByClasificacionIgnoreCase(String clasificacion);

    Composicion save(Composicion composicion);

    void deleteById(Integer id);
}