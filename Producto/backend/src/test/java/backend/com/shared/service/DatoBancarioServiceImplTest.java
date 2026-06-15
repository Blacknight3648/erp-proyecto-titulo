package backend.com.shared.service;

import backend.com.shared.application.dto.BancoDTO;
import backend.com.shared.application.dto.DatoBancarioDTO;
import backend.com.shared.application.dto.TipoCuentaBancariaDTO;
import backend.com.shared.application.service.impl.DatoBancarioServiceImpl;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.domain.model.DatoBancario;
import backend.com.shared.domain.model.TipoCuentaBancaria;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.DatoBancarioNotFoundException;
import backend.com.shared.exception.TipoCuentaBancariaNotFoundException;
import backend.com.shared.infrastructure.mapper.DatoBancarioMapper;
import backend.com.shared.infrastructure.persistence.repository.BancoRepository;
import backend.com.shared.infrastructure.persistence.repository.DatoBancarioRepository;
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
@DisplayName("DatoBancarioServiceImpl")
class DatoBancarioServiceImplTest {

    @Mock
    private DatoBancarioRepository datoBancarioRepository;

    @Mock
    private BancoRepository bancoRepository;

    @Mock
    private TipoCuentaBancariaRepository tipoCuentaBancariaRepository;

    @Mock
    private DatoBancarioMapper datoBancarioMapper;

    @InjectMocks
    private DatoBancarioServiceImpl datoBancarioServiceImpl;

    // ---------------- HELPERS ----------------

    private Banco banco(Integer id, String nombre, String codigo) {
        return Banco.builder()
                .bancoId(id)
                .nombreBanco(nombre)
                .codigoBanco(codigo)
                .build();
    }

    private TipoCuentaBancaria tipoCuenta(Integer id, String denominacion) {
        return TipoCuentaBancaria.builder()
                .tipoCuentaId(id)
                .denominacionCuenta(denominacion)
                .build();
    }

    private DatoBancario datoBancario(Integer id, String numeroCuenta, Banco banco, TipoCuentaBancaria tipoCuenta) {
        return DatoBancario.builder()
                .datoBancarioId(id)
                .numeroCuenta(numeroCuenta)
                .banco(banco)
                .tipoCuentaBancaria(tipoCuenta)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        DatoBancario d1 = datoBancario(1, "123456", banco(1, "Banco de Chile", "001"), tipoCuenta(1, "Cuenta Corriente"));
        DatoBancarioDTO dto1 = DatoBancarioDTO.builder().datoBancarioId(1).numeroCuenta("123456").build();

        when(datoBancarioRepository.findAll()).thenReturn(List.of(d1));
        when(datoBancarioMapper.toDTO(d1)).thenReturn(dto1);

        List<DatoBancarioDTO> resultado = datoBancarioServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el dato bancario existe")
    void obtenerPorId_ok() {
        DatoBancario existente = datoBancario(1, "123456", banco(1, "Banco de Chile", "001"), tipoCuenta(1, "Cuenta Corriente"));
        DatoBancarioDTO esperado = DatoBancarioDTO.builder().datoBancarioId(1).numeroCuenta("123456").build();

        when(datoBancarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(datoBancarioMapper.toDTO(existente)).thenReturn(esperado);

        Optional<DatoBancarioDTO> resultado = datoBancarioServiceImpl.obtenerPorId(1);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el dato bancario no existe")
    void obtenerPorId_noExiste() {
        when(datoBancarioRepository.findById(1)).thenReturn(Optional.empty());

        Optional<DatoBancarioDTO> resultado = datoBancarioServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEmpty();
    }

    // ---------------- listarPorBanco ----------------

    @Test
    @DisplayName("listarPorBanco mapea correctamente los datos bancarios del banco")
    void listarPorBanco_ok() {
        DatoBancario d1 = datoBancario(1, "123456", banco(1, "Banco de Chile", "001"), tipoCuenta(1, "Cuenta Corriente"));
        DatoBancarioDTO dto1 = DatoBancarioDTO.builder().datoBancarioId(1).numeroCuenta("123456").build();

        when(bancoRepository.existsById(1)).thenReturn(true);
        when(datoBancarioRepository.findByBancoId(1)).thenReturn(List.of(d1));
        when(datoBancarioMapper.toDTO(d1)).thenReturn(dto1);

        List<DatoBancarioDTO> resultado = datoBancarioServiceImpl.listarPorBanco(1);

        assertThat(resultado).containsExactly(dto1);
    }

    @Test
    @DisplayName("listarPorBanco lanza excepción si el banco no existe")
    void listarPorBanco_bancoNoExiste() {
        when(bancoRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> datoBancarioServiceImpl.listarPorBanco(1))
                .isInstanceOf(BancoNotFoundException.class);

        verify(datoBancarioRepository, never()).findByBancoId(any());
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el banco y el tipo de cuenta existen")
    void crear_ok() {
        Banco bancoChile = banco(1, "Banco de Chile", "001");
        TipoCuentaBancaria cuentaCorriente = tipoCuenta(1, "Cuenta Corriente");
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta(" 123456 ")
                .banco(BancoDTO.builder().bancoId(1).build())
                .tipoCuentaBancaria(TipoCuentaBancariaDTO.builder().tipoCuentaId(1).build())
                .build();

        DatoBancario guardado = datoBancario(1, "123456", bancoChile, cuentaCorriente);
        DatoBancarioDTO esperado = DatoBancarioDTO.builder().datoBancarioId(1).numeroCuenta("123456").build();

        when(bancoRepository.findById(1)).thenReturn(Optional.of(bancoChile));
        when(tipoCuentaBancariaRepository.findById(1)).thenReturn(Optional.of(cuentaCorriente));
        when(datoBancarioRepository.save(any(DatoBancario.class))).thenReturn(guardado);
        when(datoBancarioMapper.toDTO(guardado)).thenReturn(esperado);

        DatoBancarioDTO resultado = datoBancarioServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(datoBancarioRepository).save(argThat(d ->
                d.getNumeroCuenta().equals("123456") && d.getBanco() == bancoChile && d.getTipoCuentaBancaria() == cuentaCorriente));
    }

    @Test
    @DisplayName("crear lanza excepción si no se especifica el banco")
    void crear_sinBanco() {
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta("123456")
                .tipoCuentaBancaria(TipoCuentaBancariaDTO.builder().tipoCuentaId(1).build())
                .build();

        assertThatThrownBy(() -> datoBancarioServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verifyNoInteractions(bancoRepository, tipoCuentaBancariaRepository, datoBancarioRepository);
    }

    @Test
    @DisplayName("crear lanza excepción si no se especifica el tipo de cuenta")
    void crear_sinTipoCuenta() {
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta("123456")
                .banco(BancoDTO.builder().bancoId(1).build())
                .build();

        assertThatThrownBy(() -> datoBancarioServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verifyNoInteractions(bancoRepository, tipoCuentaBancariaRepository, datoBancarioRepository);
    }

    @Test
    @DisplayName("crear lanza excepción si el banco no existe")
    void crear_bancoNoExiste() {
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta("123456")
                .banco(BancoDTO.builder().bancoId(99).build())
                .tipoCuentaBancaria(TipoCuentaBancariaDTO.builder().tipoCuentaId(1).build())
                .build();

        when(bancoRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> datoBancarioServiceImpl.crear(dto))
                .isInstanceOf(BancoNotFoundException.class);

        verify(datoBancarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el tipo de cuenta no existe")
    void crear_tipoCuentaNoExiste() {
        Banco bancoChile = banco(1, "Banco de Chile", "001");
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta("123456")
                .banco(BancoDTO.builder().bancoId(1).build())
                .tipoCuentaBancaria(TipoCuentaBancariaDTO.builder().tipoCuentaId(99).build())
                .build();

        when(bancoRepository.findById(1)).thenReturn(Optional.of(bancoChile));
        when(tipoCuentaBancariaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> datoBancarioServiceImpl.crear(dto))
                .isInstanceOf(TipoCuentaBancariaNotFoundException.class);

        verify(datoBancarioRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el dato bancario existe")
    void actualizar_ok() {
        Banco bancoChile = banco(1, "Banco de Chile", "001");
        Banco bancoEstado = banco(2, "BancoEstado", "012");
        TipoCuentaBancaria cuentaCorriente = tipoCuenta(1, "Cuenta Corriente");
        TipoCuentaBancaria cuentaVista = tipoCuenta(2, "Cuenta Vista");
        DatoBancario existente = datoBancario(1, "123456", bancoChile, cuentaCorriente);
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta(" 654321 ")
                .banco(BancoDTO.builder().bancoId(2).build())
                .tipoCuentaBancaria(TipoCuentaBancariaDTO.builder().tipoCuentaId(2).build())
                .build();
        DatoBancarioDTO esperado = DatoBancarioDTO.builder().datoBancarioId(1).numeroCuenta("654321").build();

        when(datoBancarioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(bancoRepository.findById(2)).thenReturn(Optional.of(bancoEstado));
        when(tipoCuentaBancariaRepository.findById(2)).thenReturn(Optional.of(cuentaVista));
        when(datoBancarioRepository.save(existente)).thenReturn(existente);
        when(datoBancarioMapper.toDTO(existente)).thenReturn(esperado);

        DatoBancarioDTO resultado = datoBancarioServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNumeroCuenta()).isEqualTo("654321");
        assertThat(existente.getBanco()).isEqualTo(bancoEstado);
        assertThat(existente.getTipoCuentaBancaria()).isEqualTo(cuentaVista);
    }

    @Test
    @DisplayName("actualizar lanza excepción si el dato bancario no existe")
    void actualizar_noExiste() {
        DatoBancarioDTO dto = DatoBancarioDTO.builder()
                .numeroCuenta("654321")
                .banco(BancoDTO.builder().bancoId(1).build())
                .tipoCuentaBancaria(TipoCuentaBancariaDTO.builder().tipoCuentaId(1).build())
                .build();

        when(datoBancarioRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> datoBancarioServiceImpl.actualizar(1, dto))
                .isInstanceOf(DatoBancarioNotFoundException.class);

        verify(datoBancarioRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el dato bancario existe")
    void eliminar_ok() {
        when(datoBancarioRepository.existsById(1)).thenReturn(true);

        datoBancarioServiceImpl.eliminar(1);

        verify(datoBancarioRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el dato bancario no existe")
    void eliminar_noExiste() {
        when(datoBancarioRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> datoBancarioServiceImpl.eliminar(1))
                .isInstanceOf(DatoBancarioNotFoundException.class);

        verify(datoBancarioRepository, never()).deleteById(any());
    }
}
