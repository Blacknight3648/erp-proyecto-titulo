package backend.com.shared.service;

import backend.com.shared.application.dto.ColorTelaDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.impl.ColorTelaServiceImpl;
import backend.com.shared.domain.model.ColorTela;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ColorTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.ColorTelaRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ColorTelaServiceImpl")
class ColorTelaServiceImplTest {

    @Mock
    private ColorTelaRepository colorTelaRepository;

    @Mock
    private ColorTelaMapper mapper;

    @Mock
    private CodigoGeneratorService codigoGeneratorService;

    @InjectMocks
    private ColorTelaServiceImpl colorTelaServiceImpl;

    // ---------------- HELPERS ----------------

    private ColorTela colorTela(Integer id, String codigo, String descripcion, Boolean esPantone) {
        return ColorTela.builder()
                .idColor(id)
                .codigoColor(codigo)
                .descripcionColor(descripcion)
                .esPantone(esPantone)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        ColorTelaDTO dto = ColorTelaDTO.builder()
                .codigoColor("ROJ")
                .descripcionColor("Rojo")
                .esPantone(true)
                .build();

        ColorTela guardado = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO esperado = ColorTelaDTO.builder()
                .idColor(1)
                .codigoColor("ROJ")
                .descripcionColor("Rojo")
                .esPantone(true)
                .build();

        when(codigoGeneratorService.generarPorAbreviatura(eq("PANT-"), eq("Rojo"), any())).thenReturn("ROJ");
        when(colorTelaRepository.save(any(ColorTela.class))).thenReturn(guardado);
        when(mapper.toDTO(guardado)).thenReturn(esperado);

        ColorTelaDTO resultado = colorTelaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(colorTelaRepository).save(argThat(c ->
                c.getCodigoColor().equals("ROJ")
                        && c.getDescripcionColor().equals("Rojo")
                        && c.getEsPantone().equals(true)));
    }

    @Test
    @DisplayName("crear asigna esPantone en falso cuando no se especifica")
    void crear_sinEsPantone() {
        ColorTelaDTO dto = ColorTelaDTO.builder()
                .codigoColor("AZL")
                .descripcionColor("Azul")
                .build();

        ColorTela guardado = colorTela(2, "AZL", "Azul", false);
        ColorTelaDTO esperado = ColorTelaDTO.builder()
                .idColor(2)
                .codigoColor("AZL")
                .descripcionColor("Azul")
                .esPantone(false)
                .build();

        when(codigoGeneratorService.generarPorAbreviatura(eq(""), eq("Azul"), any())).thenReturn("AZL");
        when(colorTelaRepository.save(any(ColorTela.class))).thenReturn(guardado);
        when(mapper.toDTO(guardado)).thenReturn(esperado);

        ColorTelaDTO resultado = colorTelaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(colorTelaRepository).save(argThat(c -> c.getEsPantone().equals(false)));
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el color existe")
    void actualizar_ok() {
        ColorTela existente = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO dto = ColorTelaDTO.builder()
                .codigoColor("VRD")
                .descripcionColor("Verde")
                .esPantone(false)
                .build();
        ColorTelaDTO esperado = ColorTelaDTO.builder()
                .idColor(1)
                .codigoColor("VRD")
                .descripcionColor("Verde")
                .esPantone(false)
                .build();

        when(colorTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(colorTelaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ColorTelaDTO resultado = colorTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoColor()).isEqualTo("VRD");
        assertThat(existente.getDescripcionColor()).isEqualTo("Verde");
        assertThat(existente.getEsPantone()).isFalse();
    }

    @Test
    @DisplayName("actualizar asigna esPantone en falso cuando no se especifica")
    void actualizar_sinEsPantone() {
        ColorTela existente = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO dto = ColorTelaDTO.builder()
                .codigoColor("ROJ")
                .descripcionColor("Rojo")
                .build();
        ColorTelaDTO esperado = ColorTelaDTO.builder()
                .idColor(1)
                .codigoColor("ROJ")
                .descripcionColor("Rojo")
                .esPantone(false)
                .build();

        when(colorTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(colorTelaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ColorTelaDTO resultado = colorTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getEsPantone()).isFalse();
    }

    @Test
    @DisplayName("actualizar lanza excepción si el color no existe")
    void actualizar_noExiste() {
        ColorTelaDTO dto = ColorTelaDTO.builder()
                .codigoColor("VRD")
                .descripcionColor("Verde")
                .build();

        when(colorTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> colorTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(colorTelaRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el color existe")
    void obtenerPorId_ok() {
        ColorTela existente = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO esperado = ColorTelaDTO.builder().idColor(1).codigoColor("ROJ").build();

        when(colorTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        ColorTelaDTO resultado = colorTelaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si el color no existe")
    void obtenerPorId_noExiste() {
        when(colorTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> colorTelaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        ColorTela c1 = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO dto1 = ColorTelaDTO.builder().idColor(1).codigoColor("ROJ").build();

        when(colorTelaRepository.findAll()).thenReturn(List.of(c1));
        when(mapper.toDTO(c1)).thenReturn(dto1);

        List<ColorTelaDTO> resultado = colorTelaServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- buscarPorDescripcion ----------------

    @Test
    @DisplayName("buscarPorDescripcion mapea correctamente los resultados")
    void buscarPorDescripcion_ok() {
        ColorTela c1 = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO dto1 = ColorTelaDTO.builder().idColor(1).codigoColor("ROJ").descripcionColor("Rojo").build();

        when(colorTelaRepository.findByDescripcionColorContainingIgnoreCase("roj")).thenReturn(List.of(c1));
        when(mapper.toDTO(c1)).thenReturn(dto1);

        List<ColorTelaDTO> resultado = colorTelaServiceImpl.buscarPorDescripcion("roj");

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarPantone ----------------

    @Test
    @DisplayName("listarPantone mapea correctamente los colores pantone")
    void listarPantone_ok() {
        ColorTela c1 = colorTela(1, "ROJ", "Rojo", true);
        ColorTelaDTO dto1 = ColorTelaDTO.builder().idColor(1).codigoColor("ROJ").esPantone(true).build();

        when(colorTelaRepository.findByEsPantone(true)).thenReturn(List.of(c1));
        when(mapper.toDTO(c1)).thenReturn(dto1);

        List<ColorTelaDTO> resultado = colorTelaServiceImpl.listarPantone();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el color existe")
    void eliminar_ok() {
        when(colorTelaRepository.existsById(1)).thenReturn(true);

        colorTelaServiceImpl.eliminar(1);

        verify(colorTelaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el color no existe")
    void eliminar_noExiste() {
        when(colorTelaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> colorTelaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(colorTelaRepository, never()).deleteById(any());
    }
}
