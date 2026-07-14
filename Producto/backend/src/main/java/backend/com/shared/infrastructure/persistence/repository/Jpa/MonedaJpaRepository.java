package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.MonedaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MonedaJpaRepository extends JpaRepository<MonedaJpaEntity, Integer> {

    Optional<MonedaJpaEntity> findByCodigoMoneda(String codigoMoneda);

    boolean existsByCodigoMoneda(String codigoMoneda);
}