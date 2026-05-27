package backend.com.gestionUsuarios.usuario.application.service;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.area.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.gestionUsuarios.role.infrastructure.persistence.repository.RoleRepository;
import backend.com.gestionUsuarios.usuario.infrastructure.mapper.UserMapper;
import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.gestionUsuarios.usuario.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.usuario.infrastructure.exception.UserNotFoundException;
import backend.com.gestionUsuarios.usuario.infrastructure.persistence.repository.UserRepository;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AreaRepository areaRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            AreaRepository areaRepository, UserMapper userMapper, UserValidator userValidator) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.areaRepository = areaRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
    }

    @Override
    @Transactional
    public User crearUsuario(CreateUserDTO dto) {
        // Look up roles and areas by name
        Set<Role> roles = dto.getRoles() != null ? dto.getRoles().stream()
                .map(nombre -> roleRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        Set<Area> areas = dto.getAreas() != null ? dto.getAreas().stream()
                .map(nombre -> areaRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Área no encontrada: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        User user = userMapper.toUser(dto);
        return crearUsuario(user, roles, areas);
    }

    @Override
    public User crearUsuario(User user, Set<Role> roles, Set<Area> areas) {

        userValidator.validateUniqueness(user.getUsuarioEmail(), user.getUsuarioRun());

        user.setRoles(roles != null ? roles : new HashSet<>());
        user.setAreas(areas != null ? areas : new HashSet<>());

        return userRepository.save(user);
    }

    @Override
    public List<User> listarUsuarios() {
        return userRepository.findAll();
    }

    @Override
    public User obtenerUsuario(@NonNull Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User obtenerUsuarioPorRun(String run) {
        return userRepository.findByUsuarioRun(run)
                .orElseThrow(() -> new UserNotFoundException("run", run));
    }

    @Override
    public User actualizarUsuario(@NonNull Long id, User userActualizado) {
        return actualizarUsuario(id, userActualizado, null, null);
    }

    @Override
    public User obtenerUsuarioPorEmail(String email) {
        return userRepository.findByUsuarioEmail(email)
                .orElseThrow(() -> new UserNotFoundException("email", email));
    }

    @Override
    public User actualizarUsuario(@NonNull Long id, User userActualizado, Set<Role> roles, Set<Area> areas) {

        User user = obtenerUsuario(id);

        user.setUsuarioNombre(userActualizado.getUsuarioNombre());
        user.setUsuarioApellidos(userActualizado.getUsuarioApellidos());
        user.setUsuarioEmail(userActualizado.getUsuarioEmail());
        user.setUsuarioPassword(userActualizado.getUsuarioPassword());
        user.setTelefono(userActualizado.getTelefono());
        user.setUsuarioRun(userActualizado.getUsuarioRun());
        user.setFechaNacimiento(userActualizado.getFechaNacimiento());
        user.setDireccion(userActualizado.getDireccion());
        user.setRegion(userActualizado.getRegion());
        user.setComuna(userActualizado.getComuna());
        user.setEnabled(userActualizado.isEnabled());

        if (roles != null)
            user.setRoles(roles);
        if (areas != null)
            user.setAreas(areas);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User actualizarUsuario(@NonNull Long id, CreateUserDTO dto) {
        Set<Role> roles = dto.getRoles() != null ? dto.getRoles().stream()
                .map(nombre -> roleRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        Set<Area> areas = dto.getAreas() != null ? dto.getAreas().stream()
                .map(nombre -> areaRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Área no encontrada: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        User userToUpdate = userMapper.toUser(dto);
        return actualizarUsuario(id, userToUpdate, roles, areas);
    }

    @Override
    public void eliminarUsuario(@NonNull Long id) {

        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        userRepository.deleteById(id);
    }

    @Override
    public User asignarRoles(@NonNull Long userId, Set<Role> roles) {

        User user = obtenerUsuario(userId);

        user.setRoles(roles != null ? roles : new HashSet<>());

        return userRepository.save(user);
    }

    @Override
    public User asignarAreas(@NonNull Long userId, Set<Area> areas) {

        User user = obtenerUsuario(userId);

        user.setAreas(areas != null ? areas : new HashSet<>());

        return userRepository.save(user);
    }
}