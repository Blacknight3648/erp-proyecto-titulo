package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.Direccion;

import java.util.List;
import java.util.Optional;

public interface DireccionRepository {
    List<Direccion> findAll();
    Optional<Direccion> findById(Long id);
    List<Direccion> findByComunaId(Long comunaId);
    Direccion save(Direccion direccion);
    void deleteById(Long id);
    boolean existsById(Long id);
}
