package backend.com.gestionUsuarios.role.infrastructure.persistence.repository;

import backend.com.gestionUsuarios.role.domain.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
}
