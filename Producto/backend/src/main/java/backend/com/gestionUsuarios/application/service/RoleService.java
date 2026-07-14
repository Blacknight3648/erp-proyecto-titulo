package backend.com.gestionUsuarios.application.service;

import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.shared.domain.model.Permiso;
import java.util.List;
import java.util.Set;

public interface RoleService {

    Role obtenerRole(Long id);

    List<Role> listarRoles();

    Role crearRole(Role role);

    Role actualizarRole(Long id, Role role);

    /** Reemplaza el conjunto completo de permisos de un rol (permite tanto agregar como quitar). */
    Role actualizarPermisos(Long id, Set<Permiso> permisos);

    void eliminarRole(Long id);
}
