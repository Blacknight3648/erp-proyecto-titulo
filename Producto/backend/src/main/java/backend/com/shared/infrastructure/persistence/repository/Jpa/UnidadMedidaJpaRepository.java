package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.UnidadMedidaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UnidadMedidaJpaRepository extends JpaRepository<UnidadMedidaJpaEntity, Integer> {

    Optional<UnidadMedidaJpaEntity> findByNombreUnidad(String nombreUnidad);

    Optional<UnidadMedidaJpaEntity> findByAbreviatura(String abreviatura);

    boolean existsByAbreviatura(String abreviatura);
}