package backend.com.shared.service;

import backend.com.shared.application.dto.MonedaDTO;
import backend.com.shared.application.service.impl.MonedaServiceImpl;
import backend.com.shared.domain.model.Moneda;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.MonedaMapper;
import backend.com.shared.infrastructure.persistence.repository.MonedaRepository;
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
@DisplayName("MonedaServiceImpl")
class MonedaServiceImplTest {

    @Mock
    private MonedaRepository monedaRepository;

    @Mock
    private MonedaMapper monedaMapper;

    @InjectMocks
    private MonedaServiceImpl monedaServiceImpl;

    // ---------------- HELPERS ----------------

    private Moneda moneda(Integer id, String codigo, String nombre, String simbolo) {
        return Moneda.builder()
                .idMoneda(id)
                .codigoMoneda(codigo)
                .nombreMoneda(nombre)
                .simbolo(simbolo)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        MonedaDTO dto = MonedaDTO.builder()
                .codigoMoneda("CLP")
                .nombreMoneda("Peso Chileno")
                .simbolo("$")
                .build();

        Moneda guardada = moneda(1, "CLP", "Peso Chileno", "$");
        MonedaDTO esperado = MonedaDTO.builder()
                .idMoneda(1)
                .codigoMoneda("CLP")
                .nombreMoneda("Peso Chileno")
                .simbolo("$")
                .build();

        when(monedaRepository.existsByCodigoMoneda("CLP")).thenReturn(false);
        when(monedaRepository.save(any(Moneda.class))).thenReturn(guardada);
        when(monedaMapper.toDTO(guardada)).thenReturn(esperado);

        MonedaDTO resultado = monedaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(monedaRepository).save(any(Moneda.class));
    }

    @Test
    @DisplayName("crear lanza excepción si el código de moneda ya existe")
    void crear_duplicado() {
        MonedaDTO dto = MonedaDTO.builder()
                .codigoMoneda("CLP")
                .nombreMoneda("Peso Chileno")
                .simbolo("$")
                .build();

        when(monedaRepository.existsByCodigoMoneda("CLP")).thenReturn(true);

        assertThatThrownBy(() -> monedaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(monedaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando la moneda existe")
    void actualizar_ok() {
        Moneda existente = moneda(1, "CLP", "Peso Chileno", "$");
        MonedaDTO dto = MonedaDTO.builder()
                .codigoMoneda("USD")
                .nombreMoneda("Dólar Americano")
                .simbolo("US$")
                .build();
        MonedaDTO esperado = MonedaDTO.builder()
                .idMoneda(1)
                .codigoMoneda("USD")
                .nombreMoneda("Dólar Americano")
                .simbolo("US$")
                .build();

        when(monedaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(monedaRepository.save(existente)).thenReturn(existente);
        when(monedaMapper.toDTO(existente)).thenReturn(esperado);

        MonedaDTO resultado = monedaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoMoneda()).isEqualTo("USD");
        assertThat(existente.getNombreMoneda()).isEqualTo("Dólar Americano");
        assertThat(existente.getSimbolo()).isEqualTo("US$");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la moneda no existe")
    void actualizar_noExiste() {
        MonedaDTO dto = MonedaDTO.builder()
                .codigoMoneda("USD")
                .nombreMoneda("Dólar Americano")
                .simbolo("US$")
                .build();

        when(monedaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> monedaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(monedaRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la moneda existe")
    void obtenerPorId_ok() {
        Moneda existente = moneda(1, "CLP", "Peso Chileno", "$");
        MonedaDTO esperado = MonedaDTO.builder()
                .idMoneda(1)
                .codigoMoneda("CLP")
                .nombreMoneda("Peso Chileno")
                .simbolo("$")
                .build();

        when(monedaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(monedaMapper.toDTO(existente)).thenReturn(esperado);

        MonedaDTO resultado = monedaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la moneda no existe")
    void obtenerPorId_noExiste() {
        when(monedaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> monedaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        Moneda m1 = moneda(1, "CLP", "Peso Chileno", "$");
        Moneda m2 = moneda(2, "USD", "Dólar Americano", "US$");
        MonedaDTO dto1 = MonedaDTO.builder().idMoneda(1).codigoMoneda("CLP").nombreMoneda("Peso Chileno").simbolo("$").build();
        MonedaDTO dto2 = MonedaDTO.builder().idMoneda(2).codigoMoneda("USD").nombreMoneda("Dólar Americano").simbolo("US$").build();

        when(monedaRepository.findAll()).thenReturn(List.of(m1, m2));
        when(monedaMapper.toDTO(m1)).thenReturn(dto1);
        when(monedaMapper.toDTO(m2)).thenReturn(dto2);

        List<MonedaDTO> resultado = monedaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la moneda existe")
    void eliminar_ok() {
        when(monedaRepository.existsById(1)).thenReturn(true);

        monedaServiceImpl.eliminar(1);

        verify(monedaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la moneda no existe")
    void eliminar_noExiste() {
        when(monedaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> monedaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(monedaRepository, never()).deleteById(any());
    }
}
