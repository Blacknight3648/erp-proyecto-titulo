package backend.com.gestionUsuarios.service;

import backend.com.gestionUsuarios.application.service.impl.AreaServiceImpl;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.infrastructure.exception.AreaDuplicadaException;
import backend.com.gestionUsuarios.infrastructure.exception.AreaNotFoundException;
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
@DisplayName("Pruebas Unitarias - AreaServiceImpl")
class AreaServiceImplTest {

    @Mock
    private AreaRepository areaRepository;

    @InjectMocks
    private AreaServiceImpl areaService;

    private Area crearAreaMock(Long id, String nombre) {
        Area area = new Area();
        area.setAreaId(id);
        area.setNombre(nombre);
        area.setDescripcion("Descripción " + nombre);
        return area;
    }

    @Nested
    @DisplayName("Tests para obtenerArea")
    class ObtenerAreaTests {

        @Test
        @DisplayName("Debe retornar área cuando existe el ID")
        void obtenerArea_Exito() {
            Area areaMock = crearAreaMock(1L, "Ventas");
            when(areaRepository.findById(1L)).thenReturn(Optional.of(areaMock));

            Area result = areaService.obtenerArea(1L);

            assertNotNull(result);
            assertEquals("Ventas", result.getNombre());
            verify(areaRepository).findById(1L);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException si ID es nulo")
        void obtenerArea_IdNulo_LanzaException() {
            assertThrows(IllegalArgumentException.class, () -> areaService.obtenerArea(null));
            verifyNoInteractions(areaRepository);
        }

        @Test
        @DisplayName("Debe lanzar AreaNotFoundException si no existe el ID")
        void obtenerArea_NoExiste_LanzaException() {
            when(areaRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(AreaNotFoundException.class, () -> areaService.obtenerArea(99L));
            verify(areaRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("Tests para listarAreas")
    class ListarAreasTests {

        @Test
        @DisplayName("Debe retornar lista de áreas")
        void listarAreas_Exito() {
            when(areaRepository.findAll()).thenReturn(List.of(
                    crearAreaMock(1L, "Ventas"),
                    crearAreaMock(2L, "TI")
            ));

            List<Area> result = areaService.listarAreas();

            assertEquals(2, result.size());
            verify(areaRepository).findAll();
        }
    }

    @Nested
    @DisplayName("Tests para crearArea")
    class CrearAreaTests {

        @Test
        @DisplayName("Debe crear área exitosamente")
        void crearArea_Exito() {
            Area input = crearAreaMock(null, "Finanzas");
            Area saved = crearAreaMock(1L, "Finanzas");

            when(areaRepository.existsByNombre("Finanzas")).thenReturn(false);
            when(areaRepository.save(any(Area.class))).thenReturn(saved);

            Area result = areaService.crearArea(input);

            assertNotNull(result.getAreaId());
            assertEquals("Finanzas", result.getNombre());
            verify(areaRepository).save(input);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException si área es nula")
        void crearArea_Nula_LanzaException() {
            assertThrows(IllegalArgumentException.class, () -> areaService.crearArea(null));
            verifyNoInteractions(areaRepository);
        }

        @Test
        @DisplayName("Debe lanzar AreaDuplicadaException si nombre ya existe")
        void crearArea_Duplicada_LanzaException() {
            Area input = crearAreaMock(null, "Ventas");
            when(areaRepository.existsByNombre("Ventas")).thenReturn(true);

            assertThrows(AreaDuplicadaException.class, () -> areaService.crearArea(input));
            verify(areaRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests para actualizarArea")
    class ActualizarAreaTests {

        @Test
        @DisplayName("Debe actualizar área exitosamente")
        void actualizarArea_Exito() {
            Area existente = crearAreaMock(1L, "Ventas");
            Area input = crearAreaMock(null, "Ventas Updated");
            Area saved = crearAreaMock(1L, "Ventas Updated");

            when(areaRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(areaRepository.existsByNombre("Ventas Updated")).thenReturn(false);
            when(areaRepository.save(any(Area.class))).thenReturn(saved);

            Area result = areaService.actualizarArea(1L, input);

            assertEquals("Ventas Updated", result.getNombre());
            verify(areaRepository).save(existente);
        }

        @Test
        @DisplayName("Debe lanzar AreaDuplicadaException si el nuevo nombre ya existe en otra área")
        void actualizarArea_NombreDuplicado_LanzaException() {
            Area existente = crearAreaMock(1L, "Ventas");
            Area input = crearAreaMock(null, "Marketing");

            when(areaRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(areaRepository.existsByNombre("Marketing")).thenReturn(true);

            assertThrows(AreaDuplicadaException.class, () -> areaService.actualizarArea(1L, input));
            verify(areaRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests para eliminarArea")
    class EliminarAreaTests {

        @Test
        @DisplayName("Debe eliminar área exitosamente")
        void eliminarArea_Exito() {
            when(areaRepository.existsById(1L)).thenReturn(true);
            doNothing().when(areaRepository).deleteById(1L);

            assertDoesNotThrow(() -> areaService.eliminarArea(1L));
            verify(areaRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar AreaNotFoundException si no existe")
        void eliminarArea_NoExiste_LanzaException() {
            when(areaRepository.existsById(99L)).thenReturn(false);

            assertThrows(AreaNotFoundException.class, () -> areaService.eliminarArea(99L));
            verify(areaRepository, never()).deleteById(any());
        }
    }
}
