package backend.com.gestionUsuarios.service;

import backend.com.gestionUsuarios.application.service.impl.RoleServiceImpl;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.domain.repository.RoleRepository;
import backend.com.gestionUsuarios.infrastructure.exception.RoleDuplicadoException;
import backend.com.gestionUsuarios.infrastructure.exception.RoleNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - RoleServiceImpl")
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Area crearAreaMock(Long id, String nombre) {
        Area area = new Area();
        area.setAreaId(id);
        area.setNombre(nombre);
        return area;
    }

    private Role crearRoleMock(Long id, String nombre, Area area) {
        Role role = new Role();
        role.setId(id);
        role.setNombre(nombre);
        role.setDescripcion("Descripción " + nombre);
        role.setArea(area);
        return role;
    }

    @Nested
    @DisplayName("Tests para obtenerRole")
    class ObtenerRoleTests {

        @Test
        @DisplayName("Debe retornar rol cuando existe el ID")
        void obtenerRole_Exito() {
            Role roleMock = crearRoleMock(1L, "ADMIN", null);
            when(roleRepository.findById(1L)).thenReturn(Optional.of(roleMock));

            Role result = roleService.obtenerRole(1L);

            assertNotNull(result);
            assertEquals("ADMIN", result.getNombre());
            verify(roleRepository).findById(1L);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException si ID es nulo")
        void obtenerRole_IdNulo_LanzaException() {
            assertThrows(IllegalArgumentException.class, () -> roleService.obtenerRole(null));
            verifyNoInteractions(roleRepository);
        }

        @Test
        @DisplayName("Debe lanzar RoleNotFoundException si no existe")
        void obtenerRole_NoExiste_LanzaException() {
            when(roleRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RoleNotFoundException.class, () -> roleService.obtenerRole(99L));
        }
    }

    @Nested
    @DisplayName("Tests para listarRoles")
    class ListarRolesTests {

        @Test
        @DisplayName("Debe retornar lista de roles")
        void listarRoles_Exito() {
            when(roleRepository.findAll()).thenReturn(List.of(
                    crearRoleMock(1L, "ADMIN", null),
                    crearRoleMock(2L, "USER", null)
            ));

            List<Role> result = roleService.listarRoles();

            assertEquals(2, result.size());
            verify(roleRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Tests para crearRole")
    class CrearRoleTests {

        @Test
        @DisplayName("Debe crear rol exitosamente")
        void crearRole_Exito() {
            Area area = crearAreaMock(1L, "TI");
            Role input = crearRoleMock(null, "ADMIN", area);
            Role saved = crearRoleMock(1L, "ADMIN", area);

            when(roleRepository.existsByNombre("ADMIN")).thenReturn(false);
            when(areaRepository.findById(1L)).thenReturn(Optional.of(area));
            when(roleRepository.save(any(Role.class))).thenReturn(saved);

            Role result = roleService.crearRole(input);

            assertNotNull(result.getId());
            assertEquals("ADMIN", result.getNombre());
            verify(roleRepository).save(input);
        }

        @Test
        @DisplayName("Debe lanzar RoleDuplicadoException si nombre ya existe")
        void crearRole_Duplicado_LanzaException() {
            Role input = crearRoleMock(null, "ADMIN", null);
            when(roleRepository.existsByNombre("ADMIN")).thenReturn(true);

            assertThrows(RoleDuplicadoException.class, () -> roleService.crearRole(input));
            verify(roleRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar RuntimeException si el área no es enviada")
        void crearRole_SinArea_LanzaException() {
            Role input = crearRoleMock(null, "ADMIN", null);
            when(roleRepository.existsByNombre("ADMIN")).thenReturn(false);

            assertThrows(RuntimeException.class, () -> roleService.crearRole(input));
            verify(roleRepository, never()).save(any());
        }

        @Test
        @DisplayName("Debe lanzar RuntimeException si el área no existe en BD")
        void crearRole_AreaNoExiste_LanzaException() {
            Area area = crearAreaMock(99L, "Inválida");
            Role input = crearRoleMock(null, "ADMIN", area);

            when(roleRepository.existsByNombre("ADMIN")).thenReturn(false);
            when(areaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> roleService.crearRole(input));
            verify(roleRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests para actualizarRole")
    class ActualizarRoleTests {

        @Test
        @DisplayName("Debe actualizar rol exitosamente")
        void actualizarRole_Exito() {
            Area area = crearAreaMock(1L, "TI");
            Role existente = crearRoleMock(1L, "ADMIN", area);
            Role input = crearRoleMock(null, "SUPERADMIN", area);

            when(roleRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(roleRepository.existsByNombre("SUPERADMIN")).thenReturn(false);
            when(areaRepository.findById(1L)).thenReturn(Optional.of(area));
            when(roleRepository.save(any(Role.class))).thenReturn(existente); // se guarda la misma instancia

            Role result = roleService.actualizarRole(1L, input);

            assertEquals("SUPERADMIN", result.getNombre());
            verify(roleRepository).save(existente);
        }
    }

    @Nested
    @DisplayName("Tests para eliminarRole")
    class EliminarRoleTests {

        @Test
        @DisplayName("Debe eliminar rol exitosamente")
        void eliminarRole_Exito() {
            when(roleRepository.existsById(1L)).thenReturn(true);
            doNothing().when(roleRepository).deleteById(1L);

            assertDoesNotThrow(() -> roleService.eliminarRole(1L));
            verify(roleRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar RoleNotFoundException si no existe")
        void eliminarRole_NoExiste_LanzaException() {
            when(roleRepository.existsById(99L)).thenReturn(false);

            assertThrows(RoleNotFoundException.class, () -> roleService.eliminarRole(99L));
            verify(roleRepository, never()).deleteById(any());
        }
    }
}
