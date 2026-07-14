package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.OrdenTrabajoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenTrabajoJpaRepository extends JpaRepository<OrdenTrabajoJpaEntity, Long> {
    List<OrdenTrabajoJpaEntity> findByNotaVentaId(Long notaVentaId);

    List<OrdenTrabajoJpaEntity> findByOrdenProduccionId(Long ordenProduccionId);
}
