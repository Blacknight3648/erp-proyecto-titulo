package backend.com.gestionUsuarios.domain.repository;

import backend.com.gestionUsuarios.domain.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {

    Role save(Role role);

    Optional<Role> findById(Long id);

    List<Role> findAll();

    Optional<Role> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    void deleteById(Long id);
}
