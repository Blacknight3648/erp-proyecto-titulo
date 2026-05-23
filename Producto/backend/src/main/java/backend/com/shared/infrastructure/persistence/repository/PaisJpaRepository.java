package backend.com.shared.infrastructure.persistence.repository;

import backend.com.shared.infrastructure.persistence.entity.PaisJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaisJpaRepository extends JpaRepository<PaisJpaEntity, Integer> {
    Optional<PaisJpaEntity> findByNombrePais(String nombrePais);
}
