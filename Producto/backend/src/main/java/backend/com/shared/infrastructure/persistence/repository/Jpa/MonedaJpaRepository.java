package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.MonedaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MonedaJpaRepository extends JpaRepository<MonedaJpaEntity, Integer> {

    Optional<MonedaJpaEntity> findByCodigoMoneda(String codigoMoneda);

    boolean existsByCodigoMoneda(String codigoMoneda);
}