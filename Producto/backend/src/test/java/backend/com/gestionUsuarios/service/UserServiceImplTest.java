package backend.com.gestionUsuarios.service;

import backend.com.comercial.infrastructure.persistence.repository.EvaluacionNegocioJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.NotaVentaJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCostosJpaRepository;
import backend.com.comercial.infrastructure.persistence.repository.SolicitudCotizacionJpaRepository;
import backend.com.gestionUsuarios.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.application.service.UserValidator;
import backend.com.gestionUsuarios.application.service.impl.UserServiceImpl;
import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.domain.model.Vendedor;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.domain.repository.RoleRepository;
import backend.com.gestionUsuarios.domain.repository.UserRepository;
import backend.com.gestionUsuarios.domain.repository.VendedorRepository;
import backend.com.gestionUsuarios.infrastructure.exception.UserNotFoundException;
import backend.com.gestionUsuarios.infrastructure.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - UserServiceImpl")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AreaRepository areaRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserValidator userValidator;
    @Mock
    private VendedorRepository vendedorRepository;
    @Mock
    private EvaluacionNegocioJpaRepository evaluacionNegocioRepository;
    @Mock
    private NotaVentaJpaRepository notaVentaRepository;
    @Mock
    private SolicitudCostosJpaRepository solicitudCostosRepository;
    @Mock
    private SolicitudCotizacionJpaRepository solicitudCotizacionRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User crearUserMock(Long id, String email) {
        User user = new User();
        user.setUsuarioId(id);
        user.setUsuarioEmail(email);
        user.setUsuarioRun("11111111-1");
        user.setEnabled(true);
        return user;
    }

    @Nested
    @DisplayName("Consultas de Usuario")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los usuarios")
        void listarUsuarios() {
            when(userRepository.findAll()).thenReturn(List.of(crearUserMock(1L, "test@test.com")));
            List<User> result = userService.listarUsuarios();
            assertEquals(1, result.size());
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("Debe obtener usuario por ID")
        void obtenerUsuario() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(crearUserMock(1L, "test@test.com")));
            User result = userService.obtenerUsuario(1L);
            assertNotNull(result);
            assertEquals("test@test.com", result.getUsuarioEmail());
        }

        @Test
        @DisplayName("Debe lanzar UserNotFoundException al buscar por ID inexistente")
        void obtenerUsuario_NoExiste() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(UserNotFoundException.class, () -> userService.obtenerUsuario(99L));
        }

        @Test
        @DisplayName("Debe obtener usuario por Email")
        void obtenerUsuarioPorEmail() {
            when(userRepository.findByUsuarioEmail("test@test.com")).thenReturn(Optional.of(crearUserMock(1L, "test@test.com")));
            User result = userService.obtenerUsuarioPorEmail("test@test.com");
            assertNotNull(result);
        }
    }

    @Nested
    @DisplayName("Mutaciones de Usuario")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear usuario con DTO exitosamente")
        void crearUsuarioConDTO() {
            CreateUserDTO dto = new CreateUserDTO();
            dto.setUsuarioEmail("nuevo@test.com");
            dto.setUsuarioRun("11111111-1");
            
            User userMapped = crearUserMock(null, "nuevo@test.com");
            User saved = crearUserMock(1L, "nuevo@test.com");

            when(userMapper.toUser(dto)).thenReturn(userMapped);
            doNothing().when(userValidator).validateRun(anyString());
            doNothing().when(userValidator).validateUniqueness(anyString(), anyString());
            when(userRepository.save(any(User.class))).thenReturn(saved);

            User result = userService.crearUsuario(dto);

            assertNotNull(result.getUsuarioId());
            verify(userRepository).save(userMapped);
        }

        @Test
        @DisplayName("Debe actualizar usuario exitosamente")
        void actualizarUsuario() {
            User existente = crearUserMock(1L, "test@test.com");
            User input = crearUserMock(null, "update@test.com");
            input.setUsuarioNombre("Juan");

            when(userRepository.findById(1L)).thenReturn(Optional.of(existente));
            doNothing().when(userValidator).validateRun(anyString());
            when(userRepository.save(any(User.class))).thenReturn(existente);

            User result = userService.actualizarUsuario(1L, input);

            assertEquals("Juan", result.getUsuarioNombre());
            assertEquals("update@test.com", result.getUsuarioEmail());
            verify(userRepository).save(existente);
        }

        @Test
        @DisplayName("Debe eliminar usuario y desvincular vendedor asociado")
        void eliminarUsuario() {
            when(userRepository.existsById(1L)).thenReturn(true);
            
            Vendedor vendedor = new Vendedor();
            vendedor.setVendedorId(100L);
            when(vendedorRepository.findByUsuario_UsuarioId(1L)).thenReturn(Optional.of(vendedor));

            assertDoesNotThrow(() -> userService.eliminarUsuario(1L));

            verify(evaluacionNegocioRepository).desvincularVendedor(100L);
            verify(notaVentaRepository).desvincularVendedor(100L);
            verify(solicitudCostosRepository).desvincularVendedor(100L);
            verify(solicitudCotizacionRepository).desvincularVendedor(100L);
            verify(vendedorRepository).deleteById(100L);
            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe cambiar el estado enabled de un usuario")
        void toggleEnabled() {
            User existente = crearUserMock(1L, "test@test.com");
            assertTrue(existente.isEnabled());

            when(userRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(userRepository.save(existente)).thenReturn(existente);

            User result = userService.toggleEnabled(1L);

            assertFalse(result.isEnabled());
            verify(userRepository).save(existente);
        }
    }
}
