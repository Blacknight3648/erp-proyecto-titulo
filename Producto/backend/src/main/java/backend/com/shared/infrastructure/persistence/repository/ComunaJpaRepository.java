package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.Comuna;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComunaJpaRepository extends JpaRepository<Comuna, Long> {
    List<Comuna> findByRegion_RegionId(Long regionId);
    Optional<Comuna> findByNombreComuna(String nombreComuna);
}
