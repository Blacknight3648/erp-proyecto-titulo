package backend.com.gestionUsuarios.service;

import backend.com.gestionUsuarios.application.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.application.service.impl.VendedorServiceImpl;
import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.domain.model.Vendedor;
import backend.com.gestionUsuarios.domain.repository.UserRepository;
import backend.com.gestionUsuarios.domain.repository.VendedorRepository;
import backend.com.gestionUsuarios.infrastructure.mapper.VendedorMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - VendedorServiceImpl")
class VendedorServiceImplTest {

    @Mock
    private VendedorRepository vendedorRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VendedorMapper vendedorMapper;

    @InjectMocks
    private VendedorServiceImpl vendedorService;

    private User crearUsuarioMock(Long id, String nombre, String apellido) {
        User user = new User();
        user.setUsuarioId(id);
        user.setUsuarioNombre(nombre);
        user.setUsuarioApellidos(apellido);
        return user;
    }

    private Vendedor crearVendedorMock(Long id, Long userId, String codigo) {
        Vendedor v = new Vendedor();
        v.setVendedorId(id);
        v.setUsuarioId(userId);
        v.setCodigoVendedor(codigo);
        v.setNombreCompleto("Juan Perez");
        v.setActivo(true);
        return v;
    }

    private VendedorDTO crearVendedorDTOMock(Long id, String codigo) {
        VendedorDTO dto = new VendedorDTO();
        dto.setVendedorId(id);
        dto.setCodigoVendedor(codigo);
        dto.setNombreUsuario("Juan");
        dto.setApellidosUsuario("Perez");
        return dto;
    }

    @Nested
    @DisplayName("Tests para create")
    class CreateTests {

        @Test
        @DisplayName("Debe crear un vendedor exitosamente")
        void create_Exito() {
            VendedorCreateDTO input = new VendedorCreateDTO();
            input.setUsuarioId(1L);
            input.setCodigoVendedor("VEND-1");

            User userMock = crearUsuarioMock(1L, "Juan", "Perez");
            Vendedor saved = crearVendedorMock(1L, 1L, "VEND-1");
            VendedorDTO outputDto = crearVendedorDTOMock(1L, "VEND-1");

            when(vendedorRepository.existsByCodigoVendedor("VEND-1")).thenReturn(false);
            when(vendedorRepository.findByUsuario_UsuarioId(1L)).thenReturn(Optional.empty());
            when(userRepository.findById(1L)).thenReturn(Optional.of(userMock));
            when(vendedorRepository.save(any(Vendedor.class))).thenReturn(saved);
            when(vendedorMapper.toDTO(saved)).thenReturn(outputDto);

            VendedorDTO result = vendedorService.create(input);

            assertNotNull(result);
            assertEquals("VEND-1", result.getCodigoVendedor());
            verify(vendedorRepository).save(any(Vendedor.class));
        }

        @Test
        @DisplayName("Debe lanzar RuntimeException si código ya existe")
        void create_CodigoExiste_LanzaException() {
            VendedorCreateDTO input = new VendedorCreateDTO();
            input.setCodigoVendedor("VEND-1");

            when(vendedorRepository.existsByCodigoVendedor("VEND-1")).thenReturn(true);

            assertThrows(RuntimeException.class, () -> vendedorService.create(input));
            verify(vendedorRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar RuntimeException si usuario ya es vendedor")
        void create_UsuarioYaEsVendedor_LanzaException() {
            VendedorCreateDTO input = new VendedorCreateDTO();
            input.setUsuarioId(1L);
            input.setCodigoVendedor("VEND-1");

            when(vendedorRepository.existsByCodigoVendedor("VEND-1")).thenReturn(false);
            when(vendedorRepository.findByUsuario_UsuarioId(1L)).thenReturn(Optional.of(new Vendedor()));

            assertThrows(RuntimeException.class, () -> vendedorService.create(input));
            verify(vendedorRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests para update")
    class UpdateTests {

        @Test
        @DisplayName("Debe actualizar un vendedor exitosamente")
        void update_Exito() {
            VendedorCreateDTO input = new VendedorCreateDTO();
            input.setUsuarioId(1L);
            input.setCodigoVendedor("VEND-UPD");

            Vendedor existente = crearVendedorMock(1L, 1L, "VEND-1");
            Vendedor saved = crearVendedorMock(1L, 1L, "VEND-UPD");
            VendedorDTO outputDto = crearVendedorDTOMock(1L, "VEND-UPD");

            when(vendedorRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(vendedorRepository.existsByCodigoVendedor("VEND-UPD")).thenReturn(false);
            when(vendedorRepository.save(any(Vendedor.class))).thenReturn(saved);
            when(vendedorMapper.toDTO(saved)).thenReturn(outputDto);

            VendedorDTO result = vendedorService.update(1L, input);

            assertEquals("VEND-UPD", result.getCodigoVendedor());
            verify(vendedorRepository).save(existente);
        }
    }

    @Nested
    @DisplayName("Tests de Consultas y Eliminacion")
    class ConsultasTests {

        @Test
        @DisplayName("Debe obtener por ID")
        void findById() {
            Vendedor v = crearVendedorMock(1L, 1L, "VEND-1");
            VendedorDTO dto = crearVendedorDTOMock(1L, "VEND-1");

            when(vendedorRepository.findById(1L)).thenReturn(Optional.of(v));
            when(vendedorMapper.toDTO(v)).thenReturn(dto);

            VendedorDTO result = vendedorService.findById(1L);
            assertNotNull(result);
            assertEquals("VEND-1", result.getCodigoVendedor());
        }

        @Test
        @DisplayName("Debe eliminar lógicamente un vendedor")
        void delete() {
            Vendedor v = crearVendedorMock(1L, 1L, "VEND-1");

            when(vendedorRepository.findById(1L)).thenReturn(Optional.of(v));

            vendedorService.delete(1L);

            assertFalse(v.getActivo());
            verify(vendedorRepository).save(v);
        }
    }
}
