package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.domain.model.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface PermisoRepository extends JpaRepository<Permiso, Long> {

    Optional<Permiso> findByNombre(String nombre);

    List<Permiso> findByModulo(String modulo);

    boolean existsByNombre(String nombre);
}
