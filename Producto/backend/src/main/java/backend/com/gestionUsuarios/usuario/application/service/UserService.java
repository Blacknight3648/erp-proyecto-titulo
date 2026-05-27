package backend.com.gestionUsuarios.usuario.application.service;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.gestionUsuarios.usuario.application.dto.CreateUserDTO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Set;

public interface UserService {

    User crearUsuario(CreateUserDTO dto);

    User crearUsuario(User user, Set<Role> roles, Set<Area> areas);

    User actualizarUsuario(@NonNull Long id, User userActualizado);

    List<User> listarUsuarios();

    User obtenerUsuario(@NonNull Long id);

    User obtenerUsuarioPorRun(String run);

    User obtenerUsuarioPorEmail(String email);

    User actualizarUsuario(@NonNull Long id, User userActualizado, Set<Role> roles, Set<Area> areas);

    User actualizarUsuario(@NonNull Long id, CreateUserDTO dto);

    void eliminarUsuario(@NonNull Long id);

    User asignarRoles(@NonNull Long userId, Set<Role> roles);

    User asignarAreas(@NonNull Long userId, Set<Area> areas);
}