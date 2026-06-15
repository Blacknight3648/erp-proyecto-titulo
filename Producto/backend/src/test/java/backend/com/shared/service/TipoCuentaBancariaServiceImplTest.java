package backend.com.shared.service;

import backend.com.shared.application.dto.TipoCuentaBancariaDTO;
import backend.com.shared.application.service.impl.TipoCuentaBancariaServiceImpl;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.TipoCuentaBancariaNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoCuentaBancariaMapper;
import backend.com.shared.infrastructure.persistence.repository.TipoCuentaBancariaRepository;
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
@DisplayName("TipoCuentaBancariaServiceImpl")
class TipoCuentaBancariaServiceImplTest {

    @Mock
    private TipoCuentaBancariaRepository tipoCuentaBancariaRepository;

    @Mock
    private TipoCuentaBancariaMapper tipoCuentaBancariaMapper;

    @InjectMocks
    private TipoCuentaBancariaServiceImpl tipoCuentaBancariaServiceImpl;

    // ---------------- HELPERS ----------------

    private TipoCuentaBancaria tipoCuentaBancaria(Integer id, String denominacion) {
        return TipoCuentaBancaria.builder()
                .tipoCuentaId(id)
                .denominacionCuenta(denominacion)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        TipoCuentaBancaria t1 = tipoCuentaBancaria(1, "Cuenta Corriente");
        TipoCuentaBancaria t2 = tipoCuentaBancaria(2, "Cuenta Vista");
        TipoCuentaBancariaDTO dto1 = TipoCuentaBancariaDTO.builder().tipoCuentaId(1).denominacionCuenta("Cuenta Corriente").build();
        TipoCuentaBancariaDTO dto2 = TipoCuentaBancariaDTO.builder().tipoCuentaId(2).denominacionCuenta("Cuenta Vista").build();

        when(tipoCuentaBancariaRepository.findAll()).thenReturn(List.of(t1, t2));
        when(tipoCuentaBancariaMapper.toDTO(t1)).thenReturn(dto1);
        when(tipoCuentaBancariaMapper.toDTO(t2)).thenReturn(dto2);

        List<TipoCuentaBancariaDTO> resultado = tipoCuentaBancariaServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el tipo de cuenta existe")
    void obtenerPorId_ok() {
        TipoCuentaBancaria existente = tipoCuentaBancaria(1, "Cuenta Corriente");
        TipoCuentaBancariaDTO esperado = TipoCuentaBancariaDTO.builder().tipoCuentaId(1).denominacionCuenta("Cuenta Corriente").build();

        when(tipoCuentaBancariaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoCuentaBancariaMapper.toDTO(existente)).thenReturn(esperado);

        Optional<TipoCuentaBancariaDTO> resultado = tipoCuentaBancariaServiceImpl.obtenerPorId(1);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el tipo de cuenta no existe")
    void obtenerPorId_noExiste() {
        when(tipoCuentaBancariaRepository.findById(1)).thenReturn(Optional.empty());

        Optional<TipoCuentaBancariaDTO> resultado = tipoCuentaBancariaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEmpty();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando la denominación no está duplicada")
    void crear_ok() {
        TipoCuentaBancariaDTO dto = TipoCuentaBancariaDTO.builder()
                .denominacionCuenta(" Cuenta de Ahorro ")
                .build();

        TipoCuentaBancaria guardado = tipoCuentaBancaria(1, "Cuenta de Ahorro");
        TipoCuentaBancariaDTO esperado = TipoCuentaBancariaDTO.builder().tipoCuentaId(1).denominacionCuenta("Cuenta de Ahorro").build();

        when(tipoCuentaBancariaRepository.findAll()).thenReturn(List.of(tipoCuentaBancaria(1, "Cuenta Corriente")));
        when(tipoCuentaBancariaRepository.save(any(TipoCuentaBancaria.class))).thenReturn(guardado);
        when(tipoCuentaBancariaMapper.toDTO(guardado)).thenReturn(esperado);

        TipoCuentaBancariaDTO resultado = tipoCuentaBancariaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(tipoCuentaBancariaRepository).save(argThat(t -> t.getDenominacionCuenta().equals("Cuenta de Ahorro")));
    }

    @Test
    @DisplayName("crear lanza excepción si la denominación ya existe")
    void crear_duplicado() {
        TipoCuentaBancariaDTO dto = TipoCuentaBancariaDTO.builder()
                .denominacionCuenta("Cuenta Corriente")
                .build();

        when(tipoCuentaBancariaRepository.findAll()).thenReturn(List.of(tipoCuentaBancaria(1, "cuenta corriente")));

        assertThatThrownBy(() -> tipoCuentaBancariaServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(tipoCuentaBancariaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica la denominación cuando el tipo de cuenta existe")
    void actualizar_ok() {
        TipoCuentaBancaria existente = tipoCuentaBancaria(1, "Cuenta Corriente");
        TipoCuentaBancariaDTO dto = TipoCuentaBancariaDTO.builder()
                .denominacionCuenta("Cuenta Vista")
                .build();
        TipoCuentaBancariaDTO esperado = TipoCuentaBancariaDTO.builder().tipoCuentaId(1).denominacionCuenta("Cuenta Vista").build();

        when(tipoCuentaBancariaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoCuentaBancariaRepository.findAll()).thenReturn(List.of(existente));
        when(tipoCuentaBancariaRepository.save(existente)).thenReturn(existente);
        when(tipoCuentaBancariaMapper.toDTO(existente)).thenReturn(esperado);

        TipoCuentaBancariaDTO resultado = tipoCuentaBancariaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getDenominacionCuenta()).isEqualTo("Cuenta Vista");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la denominación ya existe en otro registro")
    void actualizar_duplicado() {
        TipoCuentaBancaria existente = tipoCuentaBancaria(1, "Cuenta Corriente");
        TipoCuentaBancaria otro = tipoCuentaBancaria(2, "Cuenta Vista");
        TipoCuentaBancariaDTO dto = TipoCuentaBancariaDTO.builder()
                .denominacionCuenta("Cuenta Vista")
                .build();

        when(tipoCuentaBancariaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoCuentaBancariaRepository.findAll()).thenReturn(List.of(existente, otro));

        assertThatThrownBy(() -> tipoCuentaBancariaServiceImpl.actualizar(1, dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(tipoCuentaBancariaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el tipo de cuenta no existe")
    void actualizar_noExiste() {
        TipoCuentaBancariaDTO dto = TipoCuentaBancariaDTO.builder()
                .denominacionCuenta("Cuenta Vista")
                .build();

        when(tipoCuentaBancariaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoCuentaBancariaServiceImpl.actualizar(1, dto))
                .isInstanceOf(TipoCuentaBancariaNotFoundException.class);

        verify(tipoCuentaBancariaRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el tipo de cuenta existe")
    void eliminar_ok() {
        when(tipoCuentaBancariaRepository.existsById(1)).thenReturn(true);

        tipoCuentaBancariaServiceImpl.eliminar(1);

        verify(tipoCuentaBancariaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el tipo de cuenta no existe")
    void eliminar_noExiste() {
        when(tipoCuentaBancariaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> tipoCuentaBancariaServiceImpl.eliminar(1))
                .isInstanceOf(TipoCuentaBancariaNotFoundException.class);

        verify(tipoCuentaBancariaRepository, never()).deleteById(any());
    }
}
