package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.CamposPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CamposPlantillaJpaRepository extends JpaRepository<CamposPlantillaJpaEntity, Long> {

    Optional<CamposPlantillaJpaEntity> findByNombreCampoIgnoreCase(String nombreCampo);

    boolean existsByNombreCampoIgnoreCase(String nombreCampo);

    List<CamposPlantillaJpaEntity> findAllByActivoTrue();
}
