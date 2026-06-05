package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.UnidadMedidaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadMedidaJpaRepository extends JpaRepository<UnidadMedidaJpaEntity, Integer> {

    Optional<UnidadMedidaJpaEntity> findByNombreUnidad(String nombreUnidad);

    Optional<UnidadMedidaJpaEntity> findByAbreviatura(String abreviatura);

    boolean existsByAbreviatura(String abreviatura);
}