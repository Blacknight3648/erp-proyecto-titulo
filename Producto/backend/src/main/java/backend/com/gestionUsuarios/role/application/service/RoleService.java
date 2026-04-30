package backend.com.gestionUsuarios.role.application.service;

import backend.com.gestionUsuarios.role.domain.model.Role;
import java.util.List;

public interface RoleService {

    Role obtenerRole(Long id);

    List<Role> listarRoles();

    Role crearRole(Role role);

    Role actualizarRole(Long id, Role role);

    void eliminarRole(Long id);
}