package backend.com.gestionUsuarios.usuario.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.usuario.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsuarioRun(String run);

    Optional<User> findByUsuarioEmail(String email);

    boolean existsByUsuarioEmail(String email);

    boolean existsByUsuarioRun(String run);

}