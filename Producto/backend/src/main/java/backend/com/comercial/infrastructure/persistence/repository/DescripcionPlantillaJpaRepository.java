package backend.com.comercial.infrastructure.persistence.repository;

import backend.com.comercial.infrastructure.persistence.entity.DescripcionPlantillaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DescripcionPlantillaJpaRepository extends JpaRepository<DescripcionPlantillaJpaEntity, Long> {

    List<DescripcionPlantillaJpaEntity> findByIdSCOS(Long idSCOS);

    void deleteByIdSCOS(Long idSCOS);
}
