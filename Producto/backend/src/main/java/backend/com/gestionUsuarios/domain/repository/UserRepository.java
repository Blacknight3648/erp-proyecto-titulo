package backend.com.gestionUsuarios.domain.repository;

import backend.com.gestionUsuarios.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    Optional<User> findByUsuarioRun(String run);

    Optional<User> findByUsuarioEmail(String email);

    boolean existsByUsuarioEmail(String email);

    boolean existsByUsuarioRun(String run);

    boolean existsById(Long id);

    void deleteById(Long id);
}
