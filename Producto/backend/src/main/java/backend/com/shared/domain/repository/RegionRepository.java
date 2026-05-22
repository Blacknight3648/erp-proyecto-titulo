package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.Region;

import java.util.List;
import java.util.Optional;

public interface RegionRepository {
    List<Region> findAll();
    Optional<Region> findById(Long id);
    List<Region> findByPaisId(Integer paisId);
    Optional<Region> findByNombreRegion(String nombreRegion);
    Region save(Region region);
    void deleteById(Long id);
    boolean existsById(Long id);
}
