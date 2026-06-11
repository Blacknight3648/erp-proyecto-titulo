package backend.com.gestionUsuarios.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsuarioRun(String run);

    Optional<UserJpaEntity> findByUsuarioEmail(String email);

    boolean existsByUsuarioEmail(String email);

    boolean existsByUsuarioRun(String run);
}
