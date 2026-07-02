package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.DescripcionPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DescripcionPlantillaJpaRepository extends JpaRepository<DescripcionPlantillaJpaEntity, Long> {

    List<DescripcionPlantillaJpaEntity> findByIdSCOS(Long idSCOS);

    void deleteByIdSCOS(Long idSCOS);
}
