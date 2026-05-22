package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionJpaRepository extends JpaRepository<Region, Long> {
    List<Region> findByPais_PaisId(Integer paisId);
    Optional<Region> findByNombreRegion(String nombreRegion);
}
