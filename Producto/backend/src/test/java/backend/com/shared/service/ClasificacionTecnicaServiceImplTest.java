package backend.com.shared.service;

import backend.com.shared.application.dto.ClasificacionTecnicaDTO;
import backend.com.shared.application.service.impl.ClasificacionTecnicaServiceImpl;
import backend.com.shared.domain.model.ClasificacionTecnica;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ClasificacionTecnicaMapper;
import backend.com.shared.infrastructure.persistence.repository.ClasificacionTecnicaRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClasificacionTecnicaServiceImpl")
class ClasificacionTecnicaServiceImplTest {

    @Mock
    private ClasificacionTecnicaRepository clasificacionRepository;

    @Mock
    private ClasificacionTecnicaMapper mapper;

    @InjectMocks
    private ClasificacionTecnicaServiceImpl clasificacionTecnicaServiceImpl;

    // ---------------- HELPERS ----------------

    private ClasificacionTecnica clasificacion(Integer id, String nombre) {
        return ClasificacionTecnica.builder()
                .idClasificacionTecnica(id)
                .nombreClasificacion(nombre)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el nombre no está duplicado")
    void crear_ok() {
        ClasificacionTecnicaDTO dto = ClasificacionTecnicaDTO.builder()
                .nombreClasificacion("Resistencia al fuego")
                .build();

        ClasificacionTecnica guardada = clasificacion(1, "Resistencia al fuego");
        ClasificacionTecnicaDTO esperado = ClasificacionTecnicaDTO.builder()
                .idClasificacionTecnica(1)
                .nombreClasificacion("Resistencia al fuego")
                .build();

        when(clasificacionRepository.existsByNombreClasificacion("Resistencia al fuego")).thenReturn(false);
        when(clasificacionRepository.save(any(ClasificacionTecnica.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        ClasificacionTecnicaDTO resultado = clasificacionTecnicaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(clasificacionRepository).save(any(ClasificacionTecnica.class));
    }

    @Test
    @DisplayName("crear lanza excepción si el nombre de clasificación ya existe")
    void crear_duplicado() {
        ClasificacionTecnicaDTO dto = ClasificacionTecnicaDTO.builder()
                .nombreClasificacion("Resistencia al fuego")
                .build();

        when(clasificacionRepository.existsByNombreClasificacion("Resistencia al fuego")).thenReturn(true);

        assertThatThrownBy(() -> clasificacionTecnicaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(clasificacionRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica el nombre cuando la clasificación existe")
    void actualizar_ok() {
        ClasificacionTecnica existente = clasificacion(1, "Resistencia al fuego");
        ClasificacionTecnicaDTO dto = ClasificacionTecnicaDTO.builder()
                .nombreClasificacion("Resistencia al agua")
                .build();
        ClasificacionTecnicaDTO esperado = ClasificacionTecnicaDTO.builder()
                .idClasificacionTecnica(1)
                .nombreClasificacion("Resistencia al agua")
                .build();

        when(clasificacionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(clasificacionRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ClasificacionTecnicaDTO resultado = clasificacionTecnicaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreClasificacion()).isEqualTo("Resistencia al agua");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la clasificación no existe")
    void actualizar_noExiste() {
        ClasificacionTecnicaDTO dto = ClasificacionTecnicaDTO.builder()
                .nombreClasificacion("Resistencia al agua")
                .build();

        when(clasificacionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clasificacionTecnicaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(clasificacionRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la clasificación existe")
    void obtenerPorId_ok() {
        ClasificacionTecnica existente = clasificacion(1, "Resistencia al fuego");
        ClasificacionTecnicaDTO esperado = ClasificacionTecnicaDTO.builder()
                .idClasificacionTecnica(1)
                .nombreClasificacion("Resistencia al fuego")
                .build();

        when(clasificacionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ClasificacionTecnicaDTO resultado = clasificacionTecnicaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la clasificación no existe")
    void obtenerPorId_noExiste() {
        when(clasificacionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clasificacionTecnicaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        ClasificacionTecnica c1 = clasificacion(1, "Resistencia al fuego");
        ClasificacionTecnica c2 = clasificacion(2, "Resistencia al agua");
        ClasificacionTecnicaDTO dto1 = ClasificacionTecnicaDTO.builder().idClasificacionTecnica(1).nombreClasificacion("Resistencia al fuego").build();
        ClasificacionTecnicaDTO dto2 = ClasificacionTecnicaDTO.builder().idClasificacionTecnica(2).nombreClasificacion("Resistencia al agua").build();

        when(clasificacionRepository.findAll()).thenReturn(List.of(c1, c2));
        when(mapper.toDTO(c1)).thenReturn(dto1);
        when(mapper.toDTO(c2)).thenReturn(dto2);

        List<ClasificacionTecnicaDTO> resultado = clasificacionTecnicaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la clasificación existe")
    void eliminar_ok() {
        when(clasificacionRepository.existsById(1)).thenReturn(true);

        clasificacionTecnicaServiceImpl.eliminar(1);

        verify(clasificacionRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la clasificación no existe")
    void eliminar_noExiste() {
        when(clasificacionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> clasificacionTecnicaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(clasificacionRepository, never()).deleteById(any());
    }
}
