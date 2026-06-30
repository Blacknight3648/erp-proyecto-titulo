package backend.com.gestionUsuarios.application.service.impl;

import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.gestionUsuarios.domain.repository.RoleRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.UserMapper;
import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.application.service.UserService;
import backend.com.gestionUsuarios.application.service.UserValidator;
import backend.com.gestionUsuarios.infrastructure.exception.UserNotFoundException;
import backend.com.gestionUsuarios.domain.repository.UserRepository;
import backend.com.gestionUsuarios.domain.repository.VendedorRepository;
import backend.com.comercial.infrastructure.persistence.repository.EvaluacionNegocioJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.NotaVentaJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCotizacionJpaRepository;
import backend.com.shared.application.service.HistorialEstadoService;

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
    private final VendedorRepository vendedorRepository;
    private final EvaluacionNegocioJpaRepository evaluacionNegocioRepository;
    private final NotaVentaJpaRepository notaVentaRepository;
    private final SolicitudCostosJpaRepository solicitudCostosRepository;
    private final SolicitudCotizacionJpaRepository solicitudCotizacionRepository;
    private final HistorialEstadoService historialEstadoService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
            AreaRepository areaRepository, UserMapper userMapper, UserValidator userValidator,
            VendedorRepository vendedorRepository, EvaluacionNegocioJpaRepository evaluacionNegocioRepository,
            NotaVentaJpaRepository notaVentaRepository, SolicitudCostosJpaRepository solicitudCostosRepository,
            SolicitudCotizacionJpaRepository solicitudCotizacionRepository,
            HistorialEstadoService historialEstadoService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.areaRepository = areaRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.vendedorRepository = vendedorRepository;
        this.evaluacionNegocioRepository = evaluacionNegocioRepository;
        this.notaVentaRepository = notaVentaRepository;
        this.solicitudCostosRepository = solicitudCostosRepository;
        this.solicitudCotizacionRepository = solicitudCotizacionRepository;
        this.historialEstadoService = historialEstadoService;
    }

    @Override
    @Transactional
    public User crearUsuario(CreateUserDTO dto) {
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
        userValidator.validateRun(user.getUsuarioRun());
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

        if (userActualizado.getUsuarioRun() != null && !userActualizado.getUsuarioRun().isBlank())
            userValidator.validateRun(userActualizado.getUsuarioRun());
        if (userActualizado.getUsuarioNombre() != null && !userActualizado.getUsuarioNombre().isBlank())
            user.setUsuarioNombre(userActualizado.getUsuarioNombre());
        if (userActualizado.getUsuarioApellidos() != null && !userActualizado.getUsuarioApellidos().isBlank()) 
            user.setUsuarioApellidos(userActualizado.getUsuarioApellidos());
        if (userActualizado.getUsuarioEmail() != null && !userActualizado.getUsuarioEmail().isBlank()) 
            user.setUsuarioEmail(userActualizado.getUsuarioEmail());
        if (userActualizado.getUsuarioPassword() != null && !userActualizado.getUsuarioPassword().isBlank()) {
            user.setUsuarioPassword(userActualizado.getUsuarioPassword());
        }
        if (userActualizado.getTelefono() != null && !userActualizado.getTelefono().isBlank()) 
            user.setTelefono(userActualizado.getTelefono());
        if (userActualizado.getUsuarioRun() != null && !userActualizado.getUsuarioRun().isBlank()) 
            user.setUsuarioRun(userActualizado.getUsuarioRun());
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
        return actualizarUsuario(id, dto, null);
    }

    @Override
    @Transactional
    public User actualizarUsuario(@NonNull Long id, CreateUserDTO dto, String adminUser) {
        boolean cambiandoPassword = dto.getUsuarioPassword() != null && !dto.getUsuarioPassword().isBlank();
        if (cambiandoPassword && !Boolean.TRUE.equals(dto.getPasswordChangeConsent())) {
            throw new IllegalArgumentException(
                    "Debe confirmar que cuenta con el consentimiento del colaborador para cambiar la contraseña");
        }

        Set<Role> roles = dto.getRoles() != null ? dto.getRoles().stream()
                .map(nombre -> roleRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        Set<Area> areas = dto.getAreas() != null ? dto.getAreas().stream()
                .map(nombre -> areaRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Área no encontrada: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        User userToUpdate = userMapper.toUser(dto);
        User actualizado = actualizarUsuario(id, userToUpdate, roles, areas);

        if (cambiandoPassword) {
            historialEstadoService.registrar("USUARIO", id, null, null,
                    adminUser != null && !adminUser.isBlank() ? adminUser : "ADMIN",
                    "Cambio de contraseña realizado por el administrador con consentimiento confirmado del colaborador");
        }

        return actualizado;
    }

    @Override
    @Transactional
    public void eliminarUsuario(@NonNull Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }

        vendedorRepository.findByUsuario_UsuarioId(id).ifPresent(v -> {
            evaluacionNegocioRepository.desvincularVendedor(v.getVendedorId());
            notaVentaRepository.desvincularVendedor(v.getVendedorId());
            solicitudCostosRepository.desvincularVendedor(v.getVendedorId());
            solicitudCotizacionRepository.desvincularVendedor(v.getVendedorId());
            vendedorRepository.deleteById(v.getVendedorId());
        });

        userRepository.deleteById(id);
    }

    @Override
    public User asignarRoles(@NonNull Long userId, Set<String> roles) {
        User user = obtenerUsuario(userId);

        Set<Role> fetchedRoles = roles != null ? roles.stream()
                .map(nombre -> roleRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        user.setRoles(fetchedRoles);

        return userRepository.save(user);
    }

    @Override
    public User asignarAreas(@NonNull Long userId, Set<String> areas) {
        User user = obtenerUsuario(userId);

        Set<Area> fetchedAreas = areas != null ? areas.stream()
                .map(nombre -> areaRepository.findByNombre(nombre)
                        .orElseThrow(() -> new RuntimeException("Área no encontrada: " + nombre)))
                .collect(Collectors.toSet()) : new HashSet<>();

        user.setAreas(fetchedAreas);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User toggleEnabled(@NonNull Long id) {
        User user = obtenerUsuario(id);
        user.setEnabled(!user.isEnabled());
        return userRepository.save(user);
    }
}
