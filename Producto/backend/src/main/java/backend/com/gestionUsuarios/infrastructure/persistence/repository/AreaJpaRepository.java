package backend.com.gestionUsuarios.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.AreaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AreaJpaRepository extends JpaRepository<AreaJpaEntity, Long> {

    Optional<AreaJpaEntity> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
