package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.ClasificacionTecnica;

import java.util.List;
import java.util.Optional;

public interface ClasificacionTecnicaRepository {

    List<ClasificacionTecnica> findAll();

    Optional<ClasificacionTecnica> findById(Integer id);

    Optional<ClasificacionTecnica> findByNombreClasificacionIgnoreCase(String nombreClasificacion);

    boolean existsByNombreClasificacion(String nombreClasificacion);

    boolean existsById(Integer id);

    ClasificacionTecnica save(ClasificacionTecnica clasificacion);

    void deleteById(Integer id);
}