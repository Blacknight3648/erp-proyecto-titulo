package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.RegistroAvanceJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegistroAvanceJpaRepository extends JpaRepository<RegistroAvanceJpaEntity, Long> {
    List<RegistroAvanceJpaEntity> findByOrdenTrabajoIdOrderByFechaAsc(Long ordenTrabajoId);
}
