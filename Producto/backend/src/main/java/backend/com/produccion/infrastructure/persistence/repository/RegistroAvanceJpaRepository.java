package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.RegistroAvanceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroAvanceJpaRepository extends JpaRepository<RegistroAvanceJpaEntity, Long> {
    List<RegistroAvanceJpaEntity> findByOrdenTrabajoIdOrderByFechaAsc(Long ordenTrabajoId);
}
