package backend.com.shared.domain.repository;

import backend.com.shared.domain.model.Comuna;

import java.util.List;
import java.util.Optional;

public interface ComunaRepository {
    List<Comuna> findAll();
    Optional<Comuna> findById(Long id);
    List<Comuna> findByRegionId(Long regionId);
    Optional<Comuna> findByNombreComuna(String nombreComuna);
    Comuna save(Comuna comuna);
    void deleteById(Long id);
    boolean existsById(Long id);
}
