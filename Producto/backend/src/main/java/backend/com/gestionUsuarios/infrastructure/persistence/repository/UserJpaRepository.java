package backend.com.gestionUsuarios.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByUsuarioRun(String run);

    Optional<UserJpaEntity> findByUsuarioEmail(String email);

    boolean existsByUsuarioEmail(String email);

    boolean existsByUsuarioRun(String run);
}
