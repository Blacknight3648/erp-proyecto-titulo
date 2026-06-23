package backend.com.gestionUsuarios.domain.repository;

import backend.com.gestionUsuarios.domain.model.Area;

import java.util.List;
import java.util.Optional;

public interface AreaRepository {

    Area save(Area area);

    Optional<Area> findById(Long id);

    List<Area> findAll();

    Optional<Area> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    void deleteById(Long id);
}
