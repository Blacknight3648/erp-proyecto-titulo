package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.ConfiguracionPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ConfiguracionPlantillaJpaRepository extends JpaRepository<ConfiguracionPlantillaJpaEntity, Long> {
    Optional<ConfiguracionPlantillaJpaEntity> findByNombrePrenda(String nombrePrenda);
}
