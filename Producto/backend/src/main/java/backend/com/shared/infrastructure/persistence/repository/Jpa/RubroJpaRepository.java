package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.RubroJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RubroJpaRepository extends JpaRepository<RubroJpaEntity, Long> {

    List<RubroJpaEntity> findAllByOrderByNombreRubroAsc();

    Optional<RubroJpaEntity> findByNombreRubro(String nombreRubro);

    Optional<RubroJpaEntity> findBySiglaRubro(String siglaRubro);

}
