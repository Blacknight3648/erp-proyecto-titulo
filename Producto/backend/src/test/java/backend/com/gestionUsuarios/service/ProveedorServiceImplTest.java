package backend.com.gestionUsuarios.service;

import backend.com.gestionUsuarios.application.service.ProveedorValidator;
import backend.com.gestionUsuarios.application.service.impl.ProveedorServiceImpl;
import backend.com.gestionUsuarios.domain.model.Proveedor;
import backend.com.gestionUsuarios.infrastructure.exception.ProveedorNotFoundException;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.ProveedorRepository;
import backend.com.shared.infrastructure.mapper.GiroMapper;
import backend.com.shared.infrastructure.persistence.repository.GiroRepository;
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
@DisplayName("Pruebas Unitarias - ProveedorServiceImpl")
class ProveedorServiceImplTest {

    @Mock
    private ProveedorRepository proveedorRepository;
    @Mock
    private GiroRepository giroRepository;
    @Mock
    private GiroMapper giroMapper;
    @Mock
    private ProveedorValidator proveedorValidator;

    @InjectMocks
    private ProveedorServiceImpl proveedorService;

    private Proveedor crearProveedorMock(Long id, String run) {
        Proveedor proveedor = new Proveedor();
        proveedor.setProveedorId(id);
        proveedor.setRunProveedor(run);
        proveedor.setRazonSocialProveedor("Proveedor Test");
        proveedor.setActivo(true);
        return proveedor;
    }

    @Nested
    @DisplayName("Consultas de Proveedor")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los proveedores")
        void listarTodos() {
            when(proveedorRepository.findAll()).thenReturn(List.of(crearProveedorMock(1L, "999-9")));
            List<Proveedor> result = proveedorService.listarTodos();
            assertEquals(1, result.size());
            verify(proveedorRepository).findAll();
        }

        @Test
        @DisplayName("Debe obtener proveedor por ID")
        void obtenerPorId() {
            when(proveedorRepository.findById(1L)).thenReturn(Optional.of(crearProveedorMock(1L, "999-9")));
            Optional<Proveedor> result = proveedorService.obtenerPorId(1L);
            assertTrue(result.isPresent());
            assertEquals("999-9", result.get().getRunProveedor());
        }
    }

    @Nested
    @DisplayName("Mutaciones de Proveedor")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear proveedor exitosamente sin giro")
        void crear_Exito() {
            Proveedor input = crearProveedorMock(null, "999-9");
            Proveedor saved = crearProveedorMock(1L, "999-9");

            doNothing().when(proveedorValidator).validateUniqueness("999-9");
            when(proveedorRepository.save(any(Proveedor.class))).thenReturn(saved);

            Proveedor result = proveedorService.crear(input);

            assertNotNull(result.getProveedorId());
            verify(proveedorRepository).save(input);
            verify(proveedorValidator).validateUniqueness("999-9");
        }

        @Test
        @DisplayName("Debe actualizar proveedor exitosamente")
        void actualizar_Exito() {
            Proveedor existente = crearProveedorMock(1L, "999-9");
            Proveedor input = crearProveedorMock(null, "999-9");
            input.setRazonSocialProveedor("Modificado");

            when(proveedorRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(proveedorRepository.save(any(Proveedor.class))).thenReturn(existente);

            Proveedor result = proveedorService.actualizar(1L, input);

            assertEquals("Modificado", result.getRazonSocialProveedor());
            verify(proveedorRepository).save(existente);
        }

        @Test
        @DisplayName("Debe lanzar ProveedorNotFoundException al actualizar si no existe")
        void actualizar_NoExiste_LanzaException() {
            when(proveedorRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(ProveedorNotFoundException.class, () -> proveedorService.actualizar(99L, new Proveedor()));
        }

        @Test
        @DisplayName("Debe eliminar proveedor exitosamente")
        void eliminar_Exito() {
            when(proveedorRepository.existsById(1L)).thenReturn(true);

            assertDoesNotThrow(() -> proveedorService.eliminar(1L));

            verify(proveedorRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar ProveedorNotFoundException al eliminar si no existe")
        void eliminar_NoExiste_LanzaException() {
            when(proveedorRepository.existsById(99L)).thenReturn(false);

            assertThrows(ProveedorNotFoundException.class, () -> proveedorService.eliminar(99L));
            verify(proveedorRepository, never()).deleteById(any());
        }
    }
}
