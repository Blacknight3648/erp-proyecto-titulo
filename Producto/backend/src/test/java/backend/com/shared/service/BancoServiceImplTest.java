package backend.com.shared.service;

import backend.com.shared.application.dto.BancoDTO;
import backend.com.shared.application.service.impl.BancoServiceImpl;
import backend.com.shared.domain.model.Banco;
import backend.com.shared.exception.BancoNotFoundException;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.infrastructure.mapper.BancoMapper;
import backend.com.shared.infrastructure.persistence.repository.BancoRepository;
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
@DisplayName("BancoServiceImpl")
class BancoServiceImplTest {

    @Mock
    private BancoRepository bancoRepository;

    @Mock
    private BancoMapper bancoMapper;

    @InjectMocks
    private BancoServiceImpl bancoServiceImpl;

    // ---------------- HELPERS ----------------

    private Banco banco(Integer id, String nombre, String codigo) {
        return Banco.builder()
                .bancoId(id)
                .nombreBanco(nombre)
                .codigoBanco(codigo)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        Banco b1 = banco(1, "Banco de Chile", "BCH");
        Banco b2 = banco(2, "Banco Estado", "BES");
        BancoDTO dto1 = BancoDTO.builder().bancoId(1).nombreBanco("Banco de Chile").codigoBanco("BCH").build();
        BancoDTO dto2 = BancoDTO.builder().bancoId(2).nombreBanco("Banco Estado").codigoBanco("BES").build();

        when(bancoRepository.findAll()).thenReturn(List.of(b1, b2));
        when(bancoMapper.toDTO(b1)).thenReturn(dto1);
        when(bancoMapper.toDTO(b2)).thenReturn(dto2);

        List<BancoDTO> resultado = bancoServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el banco existe")
    void obtenerPorId_ok() {
        Banco existente = banco(1, "Banco de Chile", "BCH");
        BancoDTO esperado = BancoDTO.builder().bancoId(1).nombreBanco("Banco de Chile").codigoBanco("BCH").build();

        when(bancoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(bancoMapper.toDTO(existente)).thenReturn(esperado);

        Optional<BancoDTO> resultado = bancoServiceImpl.obtenerPorId(1);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el banco no existe")
    void obtenerPorId_noExiste() {
        when(bancoRepository.findById(1)).thenReturn(Optional.empty());

        Optional<BancoDTO> resultado = bancoServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEmpty();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente normalizando el código a mayúsculas")
    void crear_ok() {
        BancoDTO dto = BancoDTO.builder()
                .nombreBanco("  Banco de Chile  ")
                .codigoBanco(" bch ")
                .build();

        Banco guardado = banco(1, "Banco de Chile", "BCH");
        BancoDTO esperado = BancoDTO.builder().bancoId(1).nombreBanco("Banco de Chile").codigoBanco("BCH").build();

        when(bancoRepository.findByCodigoBanco("BCH")).thenReturn(Optional.empty());
        when(bancoRepository.save(any(Banco.class))).thenReturn(guardado);
        when(bancoMapper.toDTO(guardado)).thenReturn(esperado);

        BancoDTO resultado = bancoServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(bancoRepository).save(argThat(b ->
                b.getNombreBanco().equals("Banco de Chile") && b.getCodigoBanco().equals("BCH")));
    }

    @Test
    @DisplayName("crear lanza excepción si el código de banco ya existe")
    void crear_duplicado() {
        BancoDTO dto = BancoDTO.builder()
                .nombreBanco("Banco de Chile")
                .codigoBanco("BCH")
                .build();

        when(bancoRepository.findByCodigoBanco("BCH")).thenReturn(Optional.of(banco(1, "Banco de Chile", "BCH")));

        assertThatThrownBy(() -> bancoServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(bancoRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el banco existe")
    void actualizar_ok() {
        Banco existente = banco(1, "Banco de Chile", "BCH");
        BancoDTO dto = BancoDTO.builder()
                .nombreBanco("Banco Estado")
                .codigoBanco("BES")
                .build();
        BancoDTO esperado = BancoDTO.builder().bancoId(1).nombreBanco("Banco Estado").codigoBanco("BES").build();

        when(bancoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(bancoRepository.findByCodigoBanco("BES")).thenReturn(Optional.empty());
        when(bancoRepository.save(existente)).thenReturn(existente);
        when(bancoMapper.toDTO(existente)).thenReturn(esperado);

        BancoDTO resultado = bancoServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreBanco()).isEqualTo("Banco Estado");
        assertThat(existente.getCodigoBanco()).isEqualTo("BES");
    }

    @Test
    @DisplayName("actualizar permite mantener el mismo código de banco")
    void actualizar_mismoCodigo() {
        Banco existente = banco(1, "Banco de Chile", "BCH");
        BancoDTO dto = BancoDTO.builder()
                .nombreBanco("Banco de Chile S.A.")
                .codigoBanco("BCH")
                .build();
        BancoDTO esperado = BancoDTO.builder().bancoId(1).nombreBanco("Banco de Chile S.A.").codigoBanco("BCH").build();

        when(bancoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(bancoRepository.findByCodigoBanco("BCH")).thenReturn(Optional.of(existente));
        when(bancoRepository.save(existente)).thenReturn(existente);
        when(bancoMapper.toDTO(existente)).thenReturn(esperado);

        BancoDTO resultado = bancoServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("actualizar lanza excepción si el código de banco ya existe en otro banco")
    void actualizar_duplicado() {
        Banco existente = banco(1, "Banco de Chile", "BCH");
        BancoDTO dto = BancoDTO.builder()
                .nombreBanco("Banco de Chile")
                .codigoBanco("BES")
                .build();

        when(bancoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(bancoRepository.findByCodigoBanco("BES")).thenReturn(Optional.of(banco(2, "Banco Estado", "BES")));

        assertThatThrownBy(() -> bancoServiceImpl.actualizar(1, dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(bancoRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el banco no existe")
    void actualizar_noExiste() {
        BancoDTO dto = BancoDTO.builder()
                .nombreBanco("Banco de Chile")
                .codigoBanco("BCH")
                .build();

        when(bancoRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bancoServiceImpl.actualizar(1, dto))
                .isInstanceOf(BancoNotFoundException.class);

        verify(bancoRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el banco existe")
    void eliminar_ok() {
        when(bancoRepository.existsById(1)).thenReturn(true);

        bancoServiceImpl.eliminar(1);

        verify(bancoRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el banco no existe")
    void eliminar_noExiste() {
        when(bancoRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> bancoServiceImpl.eliminar(1))
                .isInstanceOf(BancoNotFoundException.class);

        verify(bancoRepository, never()).deleteById(any());
    }
}
