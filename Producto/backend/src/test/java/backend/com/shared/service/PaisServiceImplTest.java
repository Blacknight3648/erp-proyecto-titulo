package backend.com.shared.service;

import backend.com.shared.application.dto.PaisDTO;
import backend.com.shared.application.service.impl.PaisServiceImpl;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.PaisNotFoundException;
import backend.com.shared.infrastructure.mapper.PaisMapper;
import backend.com.shared.infrastructure.persistence.repository.PaisRepository;
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
@DisplayName("PaisServiceImpl")
class PaisServiceImplTest {

    @Mock
    private PaisRepository paisRepository;

    @Mock
    private PaisMapper paisMapper;

    @InjectMocks
    private PaisServiceImpl paisServiceImpl;

    // ---------------- HELPERS ----------------

    private Pais pais(Long id, String nombre) {
        return Pais.builder()
                .idPais(id)
                .nombrePais(nombre)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        Pais p1 = pais(1L, "Chile");
        Pais p2 = pais(2L, "Argentina");
        PaisDTO dto1 = PaisDTO.builder().idPais(1L).nombrePais("Chile").build();
        PaisDTO dto2 = PaisDTO.builder().idPais(2L).nombrePais("Argentina").build();

        when(paisRepository.findAll()).thenReturn(List.of(p1, p2));
        when(paisMapper.toDTO(p1)).thenReturn(dto1);
        when(paisMapper.toDTO(p2)).thenReturn(dto2);

        List<PaisDTO> resultado = paisServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el país existe")
    void obtenerPorId_ok() {
        Pais existente = pais(1L, "Chile");
        PaisDTO esperado = PaisDTO.builder().idPais(1L).nombrePais("Chile").build();

        when(paisRepository.findById(1)).thenReturn(Optional.of(existente));
        when(paisMapper.toDTO(existente)).thenReturn(esperado);

        Optional<PaisDTO> resultado = paisServiceImpl.obtenerPorId(1);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si el país no existe")
    void obtenerPorId_noExiste() {
        when(paisRepository.findById(1)).thenReturn(Optional.empty());

        Optional<PaisDTO> resultado = paisServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEmpty();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el nombre no está duplicado")
    void crear_ok() {
        PaisDTO dto = PaisDTO.builder().nombrePais(" Chile ").build();

        Pais guardado = pais(1L, "Chile");
        PaisDTO esperado = PaisDTO.builder().idPais(1L).nombrePais("Chile").build();

        when(paisRepository.findByNombrePais(" Chile ")).thenReturn(Optional.empty());
        when(paisRepository.save(any(Pais.class))).thenReturn(guardado);
        when(paisMapper.toDTO(guardado)).thenReturn(esperado);

        PaisDTO resultado = paisServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(paisRepository).save(argThat(p -> p.getNombrePais().equals("Chile")));
    }

    @Test
    @DisplayName("crear lanza excepción si el nombre del país ya existe")
    void crear_duplicado() {
        PaisDTO dto = PaisDTO.builder().nombrePais("Chile").build();

        when(paisRepository.findByNombrePais("Chile")).thenReturn(Optional.of(pais(1L, "Chile")));

        assertThatThrownBy(() -> paisServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(paisRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica el nombre cuando el país existe")
    void actualizar_ok() {
        Pais existente = pais(1L, "Chile");
        PaisDTO dto = PaisDTO.builder().nombrePais(" Chile Modificado ").build();
        PaisDTO esperado = PaisDTO.builder().idPais(1L).nombrePais("Chile Modificado").build();

        when(paisRepository.findById(1)).thenReturn(Optional.of(existente));
        when(paisRepository.findByNombrePais(" Chile Modificado ")).thenReturn(Optional.empty());
        when(paisRepository.save(existente)).thenReturn(existente);
        when(paisMapper.toDTO(existente)).thenReturn(esperado);

        PaisDTO resultado = paisServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombrePais()).isEqualTo("Chile Modificado");
    }

    @Test
    @DisplayName("actualizar lanza excepción si el nombre ya existe en otro país")
    void actualizar_duplicado() {
        Pais existente = pais(1L, "Chile");
        Pais otro = pais(2L, "Argentina");
        PaisDTO dto = PaisDTO.builder().nombrePais("Argentina").build();

        when(paisRepository.findById(1)).thenReturn(Optional.of(existente));
        when(paisRepository.findByNombrePais("Argentina")).thenReturn(Optional.of(otro));

        assertThatThrownBy(() -> paisServiceImpl.actualizar(1, dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(paisRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el país no existe")
    void actualizar_noExiste() {
        PaisDTO dto = PaisDTO.builder().nombrePais("Chile").build();

        when(paisRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paisServiceImpl.actualizar(1, dto))
                .isInstanceOf(PaisNotFoundException.class);

        verify(paisRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el país existe")
    void eliminar_ok() {
        when(paisRepository.existsById(1)).thenReturn(true);

        paisServiceImpl.eliminar(1);

        verify(paisRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el país no existe")
    void eliminar_noExiste() {
        when(paisRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> paisServiceImpl.eliminar(1))
                .isInstanceOf(PaisNotFoundException.class);

        verify(paisRepository, never()).deleteById(any());
    }
}
