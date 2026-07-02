package backend.com.produccion.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;

import java.util.Optional;
import java.util.List;

public interface CosteoVersionJpaRepository extends JpaRepository<CosteoVersionJpaEntity, Long> {

    Optional<CosteoVersionJpaEntity> findTopByCosteo_IdCosteoOrderByNumeroVersionDesc(Long costeoId);

    List<CosteoVersionJpaEntity> findByCosteo_IdCosteoOrderByNumeroVersionAsc(Long costeoId);
}
