package backend.com.produccion.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.com.produccion.infrastructure.persistence.entity.CosteoVersionJpaEntity;

import java.util.Optional;
import java.util.List;

@Repository
public interface CosteoVersionJpaRepository extends JpaRepository<CosteoVersionJpaEntity, Long> {

    Optional<CosteoVersionJpaEntity> findTopByCosteo_IdCosteoOrderByNumeroVersionDesc(Long costeoId);

    List<CosteoVersionJpaEntity> findByCosteo_IdCosteoOrderByNumeroVersionAsc(Long costeoId);
}
