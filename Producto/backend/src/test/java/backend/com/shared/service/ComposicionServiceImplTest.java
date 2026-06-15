package backend.com.shared.service;

import backend.com.shared.application.dto.ComposicionDTO;
import backend.com.shared.application.service.impl.ComposicionServiceImpl;
import backend.com.shared.domain.model.Composicion;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ComposicionMapper;
import backend.com.shared.infrastructure.persistence.repository.ComposicionRepository;
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
@DisplayName("ComposicionServiceImpl")
class ComposicionServiceImplTest {

    @Mock
    private ComposicionRepository composicionRepository;

    @Mock
    private ComposicionMapper mapper;

    @InjectMocks
    private ComposicionServiceImpl composicionServiceImpl;

    // ---------------- HELPERS ----------------

    private Composicion composicion(Integer id, String codigo, String descripcion, String clasificacion, String usoTipico) {
        return Composicion.builder()
                .idComposicion(id)
                .codigoComposicion(codigo)
                .descripcionComposicion(descripcion)
                .clasificacion(clasificacion)
                .usoTipico(usoTipico)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        ComposicionDTO dto = ComposicionDTO.builder()
                .codigoComposicion("COMP-01")
                .descripcionComposicion("Algodón 100%")
                .clasificacion("Natural")
                .usoTipico("Prendas de verano")
                .build();

        Composicion guardada = composicion(1, "COMP-01", "Algodón 100%", "Natural", "Prendas de verano");
        ComposicionDTO esperado = ComposicionDTO.builder()
                .idComposicion(1)
                .codigoComposicion("COMP-01")
                .descripcionComposicion("Algodón 100%")
                .clasificacion("Natural")
                .usoTipico("Prendas de verano")
                .build();

        when(composicionRepository.existsByCodigoComposicion("COMP-01")).thenReturn(false);
        when(composicionRepository.save(any(Composicion.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        ComposicionDTO resultado = composicionServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(composicionRepository).save(argThat(c ->
                c.getCodigoComposicion().equals("COMP-01")
                        && c.getDescripcionComposicion().equals("Algodón 100%")
                        && c.getClasificacion().equals("Natural")
                        && c.getUsoTipico().equals("Prendas de verano")));
    }

    @Test
    @DisplayName("crear lanza excepción si el código de composición ya existe")
    void crear_duplicado() {
        ComposicionDTO dto = ComposicionDTO.builder()
                .codigoComposicion("COMP-01")
                .descripcionComposicion("Algodón 100%")
                .build();

        when(composicionRepository.existsByCodigoComposicion("COMP-01")).thenReturn(true);

        assertThatThrownBy(() -> composicionServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(composicionRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando la composición existe")
    void actualizar_ok() {
        Composicion existente = composicion(1, "COMP-01", "Algodón 100%", "Natural", "Prendas de verano");
        ComposicionDTO dto = ComposicionDTO.builder()
                .codigoComposicion("COMP-02")
                .descripcionComposicion("Poliéster 100%")
                .clasificacion("Sintético")
                .usoTipico("Prendas deportivas")
                .build();
        ComposicionDTO esperado = ComposicionDTO.builder()
                .idComposicion(1)
                .codigoComposicion("COMP-02")
                .descripcionComposicion("Poliéster 100%")
                .clasificacion("Sintético")
                .usoTipico("Prendas deportivas")
                .build();

        when(composicionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(composicionRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ComposicionDTO resultado = composicionServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoComposicion()).isEqualTo("COMP-02");
        assertThat(existente.getDescripcionComposicion()).isEqualTo("Poliéster 100%");
        assertThat(existente.getClasificacion()).isEqualTo("Sintético");
        assertThat(existente.getUsoTipico()).isEqualTo("Prendas deportivas");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la composición no existe")
    void actualizar_noExiste() {
        ComposicionDTO dto = ComposicionDTO.builder()
                .codigoComposicion("COMP-02")
                .descripcionComposicion("Poliéster 100%")
                .build();

        when(composicionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> composicionServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(composicionRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la composición existe")
    void obtenerPorId_ok() {
        Composicion existente = composicion(1, "COMP-01", "Algodón 100%", "Natural", "Prendas de verano");
        ComposicionDTO esperado = ComposicionDTO.builder().idComposicion(1).codigoComposicion("COMP-01").build();

        when(composicionRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ComposicionDTO resultado = composicionServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la composición no existe")
    void obtenerPorId_noExiste() {
        when(composicionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> composicionServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        Composicion c1 = composicion(1, "COMP-01", "Algodón 100%", "Natural", "Prendas de verano");
        ComposicionDTO dto1 = ComposicionDTO.builder().idComposicion(1).codigoComposicion("COMP-01").build();

        when(composicionRepository.findAll()).thenReturn(List.of(c1));
        when(mapper.toDTO(c1)).thenReturn(dto1);

        List<ComposicionDTO> resultado = composicionServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarPorClasificacion ----------------

    @Test
    @DisplayName("listarPorClasificacion mapea correctamente las composiciones de la clasificación")
    void listarPorClasificacion_ok() {
        Composicion c1 = composicion(1, "COMP-01", "Algodón 100%", "Natural", "Prendas de verano");
        ComposicionDTO dto1 = ComposicionDTO.builder().idComposicion(1).codigoComposicion("COMP-01").clasificacion("Natural").build();

        when(composicionRepository.findByClasificacionIgnoreCase("Natural")).thenReturn(List.of(c1));
        when(mapper.toDTO(c1)).thenReturn(dto1);

        List<ComposicionDTO> resultado = composicionServiceImpl.listarPorClasificacion("Natural");

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la composición existe")
    void eliminar_ok() {
        when(composicionRepository.existsById(1)).thenReturn(true);

        composicionServiceImpl.eliminar(1);

        verify(composicionRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la composición no existe")
    void eliminar_noExiste() {
        when(composicionRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> composicionServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(composicionRepository, never()).deleteById(any());
    }
}
