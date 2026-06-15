package backend.com.shared.service;

import backend.com.shared.application.dto.UnidadMedidaDTO;
import backend.com.shared.application.service.impl.UnidadMedidaServiceImpl;
import backend.com.shared.domain.model.UnidadMedida;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.UnidadMedidaMapper;
import backend.com.shared.infrastructure.persistence.repository.UnidadMedidaRepository;
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
@DisplayName("UnidadMedidaServiceImpl")
class UnidadMedidaServiceImplTest {

    @Mock
    private UnidadMedidaRepository unidadMedidaRepository;

    @Mock
    private UnidadMedidaMapper mapper;

    @InjectMocks
    private UnidadMedidaServiceImpl unidadMedidaServiceImpl;

    // ---------------- HELPERS ----------------

    private UnidadMedida unidadMedida(Integer id, String nombre, String abreviatura) {
        return UnidadMedida.builder()
                .idUnidadMedida(id)
                .nombreUnidad(nombre)
                .abreviatura(abreviatura)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando la abreviatura no está duplicada")
    void crear_ok() {
        UnidadMedidaDTO dto = UnidadMedidaDTO.builder()
                .nombreUnidad("Metro")
                .abreviatura("m")
                .build();

        UnidadMedida guardada = unidadMedida(1, "Metro", "m");
        UnidadMedidaDTO esperado = UnidadMedidaDTO.builder()
                .idUnidadMedida(1)
                .nombreUnidad("Metro")
                .abreviatura("m")
                .build();

        when(unidadMedidaRepository.existsByAbreviatura("m")).thenReturn(false);
        when(unidadMedidaRepository.save(any(UnidadMedida.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        UnidadMedidaDTO resultado = unidadMedidaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(unidadMedidaRepository).save(any(UnidadMedida.class));
    }

    @Test
    @DisplayName("crear lanza excepción si la abreviatura ya existe")
    void crear_duplicado() {
        UnidadMedidaDTO dto = UnidadMedidaDTO.builder()
                .nombreUnidad("Metro")
                .abreviatura("m")
                .build();

        when(unidadMedidaRepository.existsByAbreviatura("m")).thenReturn(true);

        assertThatThrownBy(() -> unidadMedidaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(unidadMedidaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica nombre y abreviatura cuando la unidad existe")
    void actualizar_ok() {
        UnidadMedida existente = unidadMedida(1, "Metro", "m");
        UnidadMedidaDTO dto = UnidadMedidaDTO.builder()
                .nombreUnidad("Kilogramo")
                .abreviatura("kg")
                .build();
        UnidadMedidaDTO esperado = UnidadMedidaDTO.builder()
                .idUnidadMedida(1)
                .nombreUnidad("Kilogramo")
                .abreviatura("kg")
                .build();

        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(unidadMedidaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        UnidadMedidaDTO resultado = unidadMedidaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreUnidad()).isEqualTo("Kilogramo");
        assertThat(existente.getAbreviatura()).isEqualTo("kg");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la unidad no existe")
    void actualizar_noExiste() {
        UnidadMedidaDTO dto = UnidadMedidaDTO.builder()
                .nombreUnidad("Kilogramo")
                .abreviatura("kg")
                .build();

        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> unidadMedidaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(unidadMedidaRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la unidad existe")
    void obtenerPorId_ok() {
        UnidadMedida existente = unidadMedida(1, "Metro", "m");
        UnidadMedidaDTO esperado = UnidadMedidaDTO.builder()
                .idUnidadMedida(1)
                .nombreUnidad("Metro")
                .abreviatura("m")
                .build();

        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        UnidadMedidaDTO resultado = unidadMedidaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la unidad no existe")
    void obtenerPorId_noExiste() {
        when(unidadMedidaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> unidadMedidaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        UnidadMedida u1 = unidadMedida(1, "Metro", "m");
        UnidadMedida u2 = unidadMedida(2, "Kilogramo", "kg");
        UnidadMedidaDTO dto1 = UnidadMedidaDTO.builder().idUnidadMedida(1).nombreUnidad("Metro").abreviatura("m").build();
        UnidadMedidaDTO dto2 = UnidadMedidaDTO.builder().idUnidadMedida(2).nombreUnidad("Kilogramo").abreviatura("kg").build();

        when(unidadMedidaRepository.findAll()).thenReturn(List.of(u1, u2));
        when(mapper.toDTO(u1)).thenReturn(dto1);
        when(mapper.toDTO(u2)).thenReturn(dto2);

        List<UnidadMedidaDTO> resultado = unidadMedidaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la unidad existe")
    void eliminar_ok() {
        when(unidadMedidaRepository.existsById(1)).thenReturn(true);

        unidadMedidaServiceImpl.eliminar(1);

        verify(unidadMedidaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la unidad no existe")
    void eliminar_noExiste() {
        when(unidadMedidaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> unidadMedidaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(unidadMedidaRepository, never()).deleteById(any());
    }
}
