package backend.com.shared.service;

import backend.com.shared.application.dto.TipoDireccionDTO;
import backend.com.shared.application.service.impl.TipoDireccionServiceImpl;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.TipoDireccionNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoDireccionMapper;
import backend.com.shared.infrastructure.persistence.repository.TipoDireccionRepository;
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
@DisplayName("TipoDireccionServiceImpl")
class TipoDireccionServiceImplTest {

    @Mock
    private TipoDireccionRepository tipoDireccionRepository;

    @Mock
    private TipoDireccionMapper tipoDireccionMapper;

    @InjectMocks
    private TipoDireccionServiceImpl tipoDireccionServiceImpl;

    // ---------------- HELPERS ----------------

    private TipoDireccion tipoDireccion(Integer id, String descripcion) {
        return TipoDireccion.builder()
                .tipoDireccionId(id)
                .descripcion(descripcion)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        TipoDireccion t1 = tipoDireccion(1, "Casa");
        TipoDireccion t2 = tipoDireccion(2, "Oficina");
        TipoDireccionDTO dto1 = TipoDireccionDTO.builder().tipoDireccionId(1).descripcion("Casa").build();
        TipoDireccionDTO dto2 = TipoDireccionDTO.builder().tipoDireccionId(2).descripcion("Oficina").build();

        when(tipoDireccionRepository.findAll()).thenReturn(List.of(t1, t2));
        when(tipoDireccionMapper.toDTO(t1)).thenReturn(dto1);
        when(tipoDireccionMapper.toDTO(t2)).thenReturn(dto2);

        List<TipoDireccionDTO> resultado = tipoDireccionServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el tipo de dirección existe")
    void obtenerPorId_ok() {
        TipoDireccion existente = tipoDireccion(1, "Casa");
        TipoDireccionDTO esperado = TipoDireccionDTO.builder().tipoDireccionId(1).descripcion("Casa").build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoDireccionMapper.toDTO(existente)).thenReturn(esperado);

        Optional<TipoDireccionDTO> resultado = tipoDireccionServiceImpl.obtenerPorId(1);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el tipo de dirección no existe")
    void obtenerPorId_noExiste() {
        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.empty());

        Optional<TipoDireccionDTO> resultado = tipoDireccionServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEmpty();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando la descripción no está duplicada")
    void crear_ok() {
        TipoDireccionDTO dto = TipoDireccionDTO.builder()
                .descripcion(" Bodega ")
                .build();

        TipoDireccion guardado = tipoDireccion(1, "Bodega");
        TipoDireccionDTO esperado = TipoDireccionDTO.builder().tipoDireccionId(1).descripcion("Bodega").build();

        when(tipoDireccionRepository.findAll()).thenReturn(List.of(tipoDireccion(1, "Casa")));
        when(tipoDireccionRepository.save(any(TipoDireccion.class))).thenReturn(guardado);
        when(tipoDireccionMapper.toDTO(guardado)).thenReturn(esperado);

        TipoDireccionDTO resultado = tipoDireccionServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(tipoDireccionRepository).save(argThat(t -> t.getDescripcion().equals("Bodega")));
    }

    @Test
    @DisplayName("crear lanza excepción si la descripción ya existe")
    void crear_duplicado() {
        TipoDireccionDTO dto = TipoDireccionDTO.builder()
                .descripcion("Casa")
                .build();

        when(tipoDireccionRepository.findAll()).thenReturn(List.of(tipoDireccion(1, "casa")));

        assertThatThrownBy(() -> tipoDireccionServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(tipoDireccionRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica la descripción cuando el tipo de dirección existe")
    void actualizar_ok() {
        TipoDireccion existente = tipoDireccion(1, "Casa");
        TipoDireccionDTO dto = TipoDireccionDTO.builder()
                .descripcion("Bodega")
                .build();
        TipoDireccionDTO esperado = TipoDireccionDTO.builder().tipoDireccionId(1).descripcion("Bodega").build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoDireccionRepository.findAll()).thenReturn(List.of(existente));
        when(tipoDireccionRepository.save(existente)).thenReturn(existente);
        when(tipoDireccionMapper.toDTO(existente)).thenReturn(esperado);

        TipoDireccionDTO resultado = tipoDireccionServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getDescripcion()).isEqualTo("Bodega");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la descripción ya existe en otro registro")
    void actualizar_duplicado() {
        TipoDireccion existente = tipoDireccion(1, "Casa");
        TipoDireccion otro = tipoDireccion(2, "Bodega");
        TipoDireccionDTO dto = TipoDireccionDTO.builder()
                .descripcion("Bodega")
                .build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoDireccionRepository.findAll()).thenReturn(List.of(existente, otro));

        assertThatThrownBy(() -> tipoDireccionServiceImpl.actualizar(1, dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(tipoDireccionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el tipo de dirección no existe")
    void actualizar_noExiste() {
        TipoDireccionDTO dto = TipoDireccionDTO.builder()
                .descripcion("Bodega")
                .build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoDireccionServiceImpl.actualizar(1, dto))
                .isInstanceOf(TipoDireccionNotFoundException.class);

        verify(tipoDireccionRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el tipo de dirección existe")
    void eliminar_ok() {
        when(tipoDireccionRepository.existsById(1)).thenReturn(true);

        tipoDireccionServiceImpl.eliminar(1);

        verify(tipoDireccionRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el tipo de dirección no existe")
    void eliminar_noExiste() {
        when(tipoDireccionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> tipoDireccionServiceImpl.eliminar(1))
                .isInstanceOf(TipoDireccionNotFoundException.class);

        verify(tipoDireccionRepository, never()).deleteById(any());
    }
}
