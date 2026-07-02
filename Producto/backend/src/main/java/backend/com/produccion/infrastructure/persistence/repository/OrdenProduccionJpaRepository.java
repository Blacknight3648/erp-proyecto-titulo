package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenProduccionJpaRepository extends JpaRepository<OrdenProduccionJpaEntity, Long> {
    List<OrdenProduccionJpaEntity> findByNotaVentaId(Long notaVentaId);
}
