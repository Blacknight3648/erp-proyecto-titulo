package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.SCOSPlantillaMaterialVinculoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SCOSPlantillaMaterialVinculoJpaRepository
        extends JpaRepository<SCOSPlantillaMaterialVinculoJpaEntity, Long> {
}
