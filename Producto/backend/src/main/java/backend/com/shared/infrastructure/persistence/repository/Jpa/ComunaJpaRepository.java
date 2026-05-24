package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ComunaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComunaJpaRepository extends JpaRepository<ComunaJpaEntity, Long> {
    List<ComunaJpaEntity> findByRegion_RegionId(Long regionId);

    Optional<ComunaJpaEntity> findByNombreComuna(String nombreComuna);
}
