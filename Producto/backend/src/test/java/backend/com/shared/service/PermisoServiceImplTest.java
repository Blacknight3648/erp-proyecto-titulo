package backend.com.shared.service;

import backend.com.shared.application.dto.PermisoDTO;
import backend.com.shared.application.service.impl.PermisoServiceImpl;
import backend.com.shared.domain.model.Permiso;
import backend.com.shared.infrastructure.persistence.repository.PermisoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PermisoServiceImpl")
class PermisoServiceImplTest {

    @Mock
    private PermisoRepository permisoRepository;

    @InjectMocks
    private PermisoServiceImpl permisoServiceImpl;

    // ---------------- HELPERS ----------------

    private Permiso permiso(Long id, String nombre, String descripcion, String modulo) {
        return Permiso.builder()
                .id(id)
                .nombre(nombre)
                .descripcion(descripcion)
                .modulo(modulo)
                .build();
    }

    // ---------------- crearPermiso ----------------

    @Test
    @DisplayName("crearPermiso guarda el permiso cuando el nombre no existe")
    void crearPermiso_ok() {
        Permiso nuevo = permiso(null, "ver_usuarios", "Permite ver usuarios", "usuarios");
        Permiso guardado = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");

        when(permisoRepository.existsByNombre("ver_usuarios")).thenReturn(false);
        when(permisoRepository.save(nuevo)).thenReturn(guardado);

        Permiso resultado = permisoServiceImpl.crearPermiso(nuevo);

        assertThat(resultado).isEqualTo(guardado);
    }

    @Test
    @DisplayName("crearPermiso lanza excepción si el nombre ya existe")
    void crearPermiso_duplicado() {
        Permiso nuevo = permiso(null, "ver_usuarios", "Permite ver usuarios", "usuarios");

        when(permisoRepository.existsByNombre("ver_usuarios")).thenReturn(true);

        assertThatThrownBy(() -> permisoServiceImpl.crearPermiso(nuevo))
                .isInstanceOf(IllegalArgumentException.class);

        verify(permisoRepository, never()).save(any());
    }

    // ---------------- listarPermisos ----------------

    @Test
    @DisplayName("listarPermisos retorna todos los permisos")
    void listarPermisos_ok() {
        Permiso p1 = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");

        when(permisoRepository.findAll()).thenReturn(List.of(p1));

        List<Permiso> resultado = permisoServiceImpl.listarPermisos();

        assertThat(resultado).containsExactly(p1);
    }

    // ---------------- listarPermisosPorModulo ----------------

    @Test
    @DisplayName("listarPermisosPorModulo retorna los permisos del módulo")
    void listarPermisosPorModulo_ok() {
        Permiso p1 = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");

        when(permisoRepository.findByModulo("usuarios")).thenReturn(List.of(p1));

        List<Permiso> resultado = permisoServiceImpl.listarPermisosPorModulo("usuarios");

        assertThat(resultado).containsExactly(p1);
    }

    // ---------------- obtenerPermisoPorId ----------------

    @Test
    @DisplayName("obtenerPermisoPorId retorna el permiso cuando existe")
    void obtenerPermisoPorId_ok() {
        Permiso p1 = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");

        when(permisoRepository.findById(1L)).thenReturn(Optional.of(p1));

        Optional<Permiso> resultado = permisoServiceImpl.obtenerPermisoPorId(1L);

        assertThat(resultado).contains(p1);
    }

    @Test
    @DisplayName("obtenerPermisoPorId retorna vacío si no existe")
    void obtenerPermisoPorId_noExiste() {
        when(permisoRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Permiso> resultado = permisoServiceImpl.obtenerPermisoPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- obtenerPermisoPorNombre ----------------

    @Test
    @DisplayName("obtenerPermisoPorNombre retorna el permiso cuando existe")
    void obtenerPermisoPorNombre_ok() {
        Permiso p1 = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");

        when(permisoRepository.findByNombre("ver_usuarios")).thenReturn(Optional.of(p1));

        Optional<Permiso> resultado = permisoServiceImpl.obtenerPermisoPorNombre("ver_usuarios");

        assertThat(resultado).contains(p1);
    }

    @Test
    @DisplayName("obtenerPermisoPorNombre retorna vacío si no existe")
    void obtenerPermisoPorNombre_noExiste() {
        when(permisoRepository.findByNombre("inexistente")).thenReturn(Optional.empty());

        Optional<Permiso> resultado = permisoServiceImpl.obtenerPermisoPorNombre("inexistente");

        assertThat(resultado).isEmpty();
    }

    // ---------------- actualizarPermiso ----------------

    @Test
    @DisplayName("actualizarPermiso modifica los datos cuando el permiso existe")
    void actualizarPermiso_ok() {
        Permiso existente = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");
        Permiso actualizado = permiso(null, "editar_usuarios", "Permite editar usuarios", "usuarios");
        Permiso guardado = permiso(1L, "editar_usuarios", "Permite editar usuarios", "usuarios");

        when(permisoRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(permisoRepository.save(existente)).thenReturn(guardado);

        Permiso resultado = permisoServiceImpl.actualizarPermiso(1L, actualizado);

        assertThat(resultado).isEqualTo(guardado);
        assertThat(existente.getNombre()).isEqualTo("editar_usuarios");
        assertThat(existente.getDescripcion()).isEqualTo("Permite editar usuarios");
        assertThat(existente.getModulo()).isEqualTo("usuarios");
    }

    @Test
    @DisplayName("actualizarPermiso lanza excepción si el permiso no existe")
    void actualizarPermiso_noExiste() {
        Permiso actualizado = permiso(null, "editar_usuarios", "Permite editar usuarios", "usuarios");

        when(permisoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> permisoServiceImpl.actualizarPermiso(1L, actualizado))
                .isInstanceOf(IllegalArgumentException.class);

        verify(permisoRepository, never()).save(any());
    }

    // ---------------- eliminarPermiso ----------------

    @Test
    @DisplayName("eliminarPermiso elimina correctamente cuando el permiso existe")
    void eliminarPermiso_ok() {
        when(permisoRepository.existsById(1L)).thenReturn(true);

        permisoServiceImpl.eliminarPermiso(1L);

        verify(permisoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminarPermiso lanza excepción si el permiso no existe")
    void eliminarPermiso_noExiste() {
        when(permisoRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> permisoServiceImpl.eliminarPermiso(1L))
                .isInstanceOf(IllegalArgumentException.class);

        verify(permisoRepository, never()).deleteById(any());
    }

    // ---------------- obtenerPermisosAgrupadosPorModulo ----------------

    @Test
    @DisplayName("obtenerPermisosAgrupadosPorModulo mapea correctamente la lista completa")
    void obtenerPermisosAgrupadosPorModulo_ok() {
        Permiso p1 = permiso(1L, "ver_usuarios", "Permite ver usuarios", "usuarios");
        Permiso p2 = permiso(2L, "editar_usuarios", "Permite editar usuarios", "usuarios");

        when(permisoRepository.findAll()).thenReturn(List.of(p1, p2));

        List<PermisoDTO> resultado = permisoServiceImpl.obtenerPermisosAgrupadosPorModulo();

        assertThat(resultado).extracting(PermisoDTO::getId).containsExactly(1L, 2L);
        assertThat(resultado).extracting(PermisoDTO::getNombre).containsExactly("ver_usuarios", "editar_usuarios");
    }
}
