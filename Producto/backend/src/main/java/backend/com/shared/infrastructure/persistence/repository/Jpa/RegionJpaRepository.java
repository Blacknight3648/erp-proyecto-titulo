package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.RegionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegionJpaRepository extends JpaRepository<RegionJpaEntity, Long> {
    List<RegionJpaEntity> findByPais_PaisId(Integer paisId);

    Optional<RegionJpaEntity> findByNombreRegion(String nombreRegion);
}
