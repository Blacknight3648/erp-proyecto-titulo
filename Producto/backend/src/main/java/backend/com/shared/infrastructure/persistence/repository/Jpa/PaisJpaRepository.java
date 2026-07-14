package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.PaisJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaisJpaRepository extends JpaRepository<PaisJpaEntity, Integer> {
    Optional<PaisJpaEntity> findByNombrePais(String nombrePais);
}
