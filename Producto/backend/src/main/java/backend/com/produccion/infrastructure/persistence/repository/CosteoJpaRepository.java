package backend.com.produccion.infrastructure.persistence.repository;

import backend.com.produccion.infrastructure.persistence.entity.CosteoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CosteoJpaRepository extends JpaRepository<CosteoJpaEntity, Long> {
    Optional<CosteoJpaEntity> findBySolicitudCostosId(Long solicitudCostosId);
    java.util.List<CosteoJpaEntity> findAllBySolicitudCostosId(Long solicitudCostosId);
    Optional<CosteoJpaEntity> findByNotaVentaId(Long notaVentaId);
    void deleteBySolicitudCostosId(Long solicitudCostosId);
}
