package backend.com.shared.service;

import backend.com.shared.application.dto.ComunaDTO;
import backend.com.shared.application.dto.DireccionDTO;
import backend.com.shared.application.dto.TipoDireccionDTO;
import backend.com.shared.application.service.impl.DireccionServiceImpl;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.model.Direccion;
import backend.com.shared.domain.model.TipoDireccion;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ComunaNotFoundException;
import backend.com.shared.exception.DireccionNotFoundException;
import backend.com.shared.exception.TipoDireccionNotFoundException;
import backend.com.shared.infrastructure.mapper.DireccionMapper;
import backend.com.shared.infrastructure.persistence.repository.ComunaRepository;
import backend.com.shared.infrastructure.persistence.repository.DireccionRepository;
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
@DisplayName("DireccionServiceImpl")
class DireccionServiceImplTest {

    @Mock
    private DireccionRepository direccionRepository;

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private TipoDireccionRepository tipoDireccionRepository;

    @Mock
    private DireccionMapper direccionMapper;

    @InjectMocks
    private DireccionServiceImpl direccionServiceImpl;

    // ---------------- HELPERS ----------------

    private TipoDireccion tipoDireccion(Integer id, String descripcion) {
        return TipoDireccion.builder()
                .tipoDireccionId(id)
                .descripcion(descripcion)
                .build();
    }

    private Comuna comuna(Long id, String nombre) {
        return Comuna.builder()
                .comunaId(id)
                .nombreComuna(nombre)
                .build();
    }

    private Direccion direccion(Long id, String calle, String numero, String depto, TipoDireccion tipoDireccion, Comuna comuna) {
        return Direccion.builder()
                .direccionId(id)
                .calle(calle)
                .numero(numero)
                .depto(depto)
                .tipoDireccion(tipoDireccion)
                .comuna(comuna)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        Direccion d1 = direccion(1L, "Calle Falsa", "123", null, tipoDireccion(1, "Casa"), comuna(1L, "Viña del Mar"));
        DireccionDTO dto1 = DireccionDTO.builder().direccionId(1L).calle("Calle Falsa").numero("123").build();

        when(direccionRepository.findAll()).thenReturn(List.of(d1));
        when(direccionMapper.toDTO(d1)).thenReturn(dto1);

        List<DireccionDTO> resultado = direccionServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la dirección existe")
    void obtenerPorId_ok() {
        Direccion existente = direccion(1L, "Calle Falsa", "123", null, tipoDireccion(1, "Casa"), comuna(1L, "Viña del Mar"));
        DireccionDTO esperado = DireccionDTO.builder().direccionId(1L).calle("Calle Falsa").numero("123").build();

        when(direccionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(direccionMapper.toDTO(existente)).thenReturn(esperado);

        Optional<DireccionDTO> resultado = direccionServiceImpl.obtenerPorId(1L);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si la dirección no existe")
    void obtenerPorId_noExiste() {
        when(direccionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<DireccionDTO> resultado = direccionServiceImpl.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- listarPorComuna ----------------

    @Test
    @DisplayName("listarPorComuna mapea correctamente las direcciones de la comuna")
    void listarPorComuna_ok() {
        Direccion d1 = direccion(1L, "Calle Falsa", "123", null, tipoDireccion(1, "Casa"), comuna(1L, "Viña del Mar"));
        DireccionDTO dto1 = DireccionDTO.builder().direccionId(1L).calle("Calle Falsa").numero("123").build();

        when(comunaRepository.existsById(1L)).thenReturn(true);
        when(direccionRepository.findByComunaId(1L)).thenReturn(List.of(d1));
        when(direccionMapper.toDTO(d1)).thenReturn(dto1);

        List<DireccionDTO> resultado = direccionServiceImpl.listarPorComuna(1L);

        assertThat(resultado).containsExactly(dto1);
    }

    @Test
    @DisplayName("listarPorComuna lanza excepción si la comuna no existe")
    void listarPorComuna_comunaNoExiste() {
        when(comunaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> direccionServiceImpl.listarPorComuna(1L))
                .isInstanceOf(ComunaNotFoundException.class);

        verify(direccionRepository, never()).findByComunaId(any());
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el tipo de dirección y la comuna existen")
    void crear_ok() {
        TipoDireccion casa = tipoDireccion(1, "Casa");
        Comuna vina = comuna(1L, "Viña del Mar");
        DireccionDTO dto = DireccionDTO.builder()
                .calle(" Calle Falsa ")
                .numero(" 123 ")
                .depto(" 4B ")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(1).build())
                .comuna(ComunaDTO.builder().comunaId(1L).build())
                .build();

        Direccion guardado = direccion(1L, "Calle Falsa", "123", "4B", casa, vina);
        DireccionDTO esperado = DireccionDTO.builder().direccionId(1L).calle("Calle Falsa").numero("123").depto("4B").build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.of(casa));
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(vina));
        when(direccionRepository.save(any(Direccion.class))).thenReturn(guardado);
        when(direccionMapper.toDTO(guardado)).thenReturn(esperado);

        DireccionDTO resultado = direccionServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(direccionRepository).save(argThat(d ->
                d.getCalle().equals("Calle Falsa") && d.getNumero().equals("123") && d.getDepto().equals("4B")
                        && d.getTipoDireccion() == casa && d.getComuna() == vina));
    }

    @Test
    @DisplayName("crear permite depto nulo")
    void crear_sinDepto() {
        TipoDireccion casa = tipoDireccion(1, "Casa");
        Comuna vina = comuna(1L, "Viña del Mar");
        DireccionDTO dto = DireccionDTO.builder()
                .calle("Calle Falsa")
                .numero("123")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(1).build())
                .comuna(ComunaDTO.builder().comunaId(1L).build())
                .build();

        Direccion guardado = direccion(1L, "Calle Falsa", "123", null, casa, vina);
        DireccionDTO esperado = DireccionDTO.builder().direccionId(1L).calle("Calle Falsa").numero("123").build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.of(casa));
        when(comunaRepository.findById(1L)).thenReturn(Optional.of(vina));
        when(direccionRepository.save(any(Direccion.class))).thenReturn(guardado);
        when(direccionMapper.toDTO(guardado)).thenReturn(esperado);

        DireccionDTO resultado = direccionServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(direccionRepository).save(argThat(d -> d.getDepto() == null));
    }

    @Test
    @DisplayName("crear lanza excepción si no se especifica el tipo de dirección")
    void crear_sinTipoDireccion() {
        DireccionDTO dto = DireccionDTO.builder()
                .calle("Calle Falsa")
                .numero("123")
                .comuna(ComunaDTO.builder().comunaId(1L).build())
                .build();

        assertThatThrownBy(() -> direccionServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verifyNoInteractions(tipoDireccionRepository, comunaRepository, direccionRepository);
    }

    @Test
    @DisplayName("crear lanza excepción si no se especifica la comuna")
    void crear_sinComuna() {
        DireccionDTO dto = DireccionDTO.builder()
                .calle("Calle Falsa")
                .numero("123")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(1).build())
                .build();

        assertThatThrownBy(() -> direccionServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verifyNoInteractions(tipoDireccionRepository, comunaRepository, direccionRepository);
    }

    @Test
    @DisplayName("crear lanza excepción si el tipo de dirección no existe")
    void crear_tipoDireccionNoExiste() {
        DireccionDTO dto = DireccionDTO.builder()
                .calle("Calle Falsa")
                .numero("123")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(99).build())
                .comuna(ComunaDTO.builder().comunaId(1L).build())
                .build();

        when(tipoDireccionRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> direccionServiceImpl.crear(dto))
                .isInstanceOf(TipoDireccionNotFoundException.class);

        verify(direccionRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si la comuna no existe")
    void crear_comunaNoExiste() {
        TipoDireccion casa = tipoDireccion(1, "Casa");
        DireccionDTO dto = DireccionDTO.builder()
                .calle("Calle Falsa")
                .numero("123")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(1).build())
                .comuna(ComunaDTO.builder().comunaId(99L).build())
                .build();

        when(tipoDireccionRepository.findById(1)).thenReturn(Optional.of(casa));
        when(comunaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> direccionServiceImpl.crear(dto))
                .isInstanceOf(ComunaNotFoundException.class);

        verify(direccionRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando la dirección existe")
    void actualizar_ok() {
        TipoDireccion casa = tipoDireccion(1, "Casa");
        TipoDireccion oficina = tipoDireccion(2, "Oficina");
        Comuna vina = comuna(1L, "Viña del Mar");
        Comuna santiago = comuna(2L, "Santiago");
        Direccion existente = direccion(1L, "Calle Falsa", "123", null, casa, vina);
        DireccionDTO dto = DireccionDTO.builder()
                .calle(" Calle Nueva ")
                .numero(" 456 ")
                .depto(" 5C ")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(2).build())
                .comuna(ComunaDTO.builder().comunaId(2L).build())
                .build();
        DireccionDTO esperado = DireccionDTO.builder().direccionId(1L).calle("Calle Nueva").numero("456").depto("5C").build();

        when(direccionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(tipoDireccionRepository.findById(2)).thenReturn(Optional.of(oficina));
        when(comunaRepository.findById(2L)).thenReturn(Optional.of(santiago));
        when(direccionRepository.save(existente)).thenReturn(existente);
        when(direccionMapper.toDTO(existente)).thenReturn(esperado);

        DireccionDTO resultado = direccionServiceImpl.actualizar(1L, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCalle()).isEqualTo("Calle Nueva");
        assertThat(existente.getNumero()).isEqualTo("456");
        assertThat(existente.getDepto()).isEqualTo("5C");
        assertThat(existente.getTipoDireccion()).isEqualTo(oficina);
        assertThat(existente.getComuna()).isEqualTo(santiago);
    }

    @Test
    @DisplayName("actualizar lanza excepción si la dirección no existe")
    void actualizar_noExiste() {
        DireccionDTO dto = DireccionDTO.builder()
                .calle("Calle Nueva")
                .numero("456")
                .tipoDireccion(TipoDireccionDTO.builder().tipoDireccionId(1).build())
                .comuna(ComunaDTO.builder().comunaId(1L).build())
                .build();

        when(direccionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> direccionServiceImpl.actualizar(1L, dto))
                .isInstanceOf(DireccionNotFoundException.class);

        verify(direccionRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la dirección existe")
    void eliminar_ok() {
        when(direccionRepository.existsById(1L)).thenReturn(true);

        direccionServiceImpl.eliminar(1L);

        verify(direccionRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la dirección no existe")
    void eliminar_noExiste() {
        when(direccionRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> direccionServiceImpl.eliminar(1L))
                .isInstanceOf(DireccionNotFoundException.class);

        verify(direccionRepository, never()).deleteById(any());
    }
}
