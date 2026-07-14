package backend.com.shared.service;

import backend.com.shared.application.dto.GramajeTelaDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.impl.GramajeTelaServiceImpl;
import backend.com.shared.domain.model.GramajeTela;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.GramajeTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.GramajeTelaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GramajeTelaServiceImpl")
class GramajeTelaServiceImplTest {

    @Mock
    private GramajeTelaRepository gramajeRepository;

    @Mock
    private GramajeTelaMapper mapper;

    @Mock
    private CodigoGeneratorService codigoGeneratorService;

    @InjectMocks
    private GramajeTelaServiceImpl gramajeTelaServiceImpl;

    // ---------------- HELPERS ----------------

    private GramajeTela gramajeTela(Integer id, String codigo, BigDecimal valorGramosM2, String categoriaVestuario) {
        return GramajeTela.builder()
                .idGramaje(id)
                .codigoGramaje(codigo)
                .valorGramosM2(valorGramosM2)
                .categoriaVestuario(categoriaVestuario)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        GramajeTelaDTO dto = GramajeTelaDTO.builder()
                .codigoGramaje("G180")
                .valorGramosM2(new BigDecimal("180.00"))
                .categoriaVestuario("Liviano")
                .build();

        GramajeTela guardado = gramajeTela(1, "G180", new BigDecimal("180.00"), "Liviano");
        GramajeTelaDTO esperado = GramajeTelaDTO.builder()
                .idGramaje(1)
                .codigoGramaje("G180")
                .valorGramosM2(new BigDecimal("180.00"))
                .categoriaVestuario("Liviano")
                .build();

        when(codigoGeneratorService.generarGramaje(eq("G-"), eq(new BigDecimal("180.00")), any())).thenReturn("G180");
        when(gramajeRepository.save(any(GramajeTela.class))).thenReturn(guardado);
        when(mapper.toDTO(guardado)).thenReturn(esperado);

        GramajeTelaDTO resultado = gramajeTelaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(gramajeRepository).save(argThat(g ->
                g.getCodigoGramaje().equals("G180")
                        && g.getValorGramosM2().equals(new BigDecimal("180.00"))
                        && g.getCategoriaVestuario().equals("Liviano")));
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el gramaje existe")
    void actualizar_ok() {
        GramajeTela existente = gramajeTela(1, "G180", new BigDecimal("180.00"), "Liviano");
        GramajeTelaDTO dto = GramajeTelaDTO.builder()
                .codigoGramaje("G240")
                .valorGramosM2(new BigDecimal("240.00"))
                .categoriaVestuario("Pesado")
                .build();
        GramajeTelaDTO esperado = GramajeTelaDTO.builder()
                .idGramaje(1)
                .codigoGramaje("G240")
                .valorGramosM2(new BigDecimal("240.00"))
                .categoriaVestuario("Pesado")
                .build();

        when(gramajeRepository.findById(1)).thenReturn(Optional.of(existente));
        when(gramajeRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        GramajeTelaDTO resultado = gramajeTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoGramaje()).isEqualTo("G240");
        assertThat(existente.getValorGramosM2()).isEqualTo(new BigDecimal("240.00"));
        assertThat(existente.getCategoriaVestuario()).isEqualTo("Pesado");
    }

    @Test
    @DisplayName("actualizar lanza excepción si el gramaje no existe")
    void actualizar_noExiste() {
        GramajeTelaDTO dto = GramajeTelaDTO.builder()
                .codigoGramaje("G240")
                .valorGramosM2(new BigDecimal("240.00"))
                .build();

        when(gramajeRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gramajeTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(gramajeRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el gramaje existe")
    void obtenerPorId_ok() {
        GramajeTela existente = gramajeTela(1, "G180", new BigDecimal("180.00"), "Liviano");
        GramajeTelaDTO esperado = GramajeTelaDTO.builder().idGramaje(1).codigoGramaje("G180").build();

        when(gramajeRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        GramajeTelaDTO resultado = gramajeTelaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si el gramaje no existe")
    void obtenerPorId_noExiste() {
        when(gramajeRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gramajeTelaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        GramajeTela g1 = gramajeTela(1, "G180", new BigDecimal("180.00"), "Liviano");
        GramajeTelaDTO dto1 = GramajeTelaDTO.builder().idGramaje(1).codigoGramaje("G180").build();

        when(gramajeRepository.findAll()).thenReturn(List.of(g1));
        when(mapper.toDTO(g1)).thenReturn(dto1);

        List<GramajeTelaDTO> resultado = gramajeTelaServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarPorCategoriaVestuario ----------------

    @Test
    @DisplayName("listarPorCategoriaVestuario mapea correctamente los gramajes de la categoría")
    void listarPorCategoriaVestuario_ok() {
        GramajeTela g1 = gramajeTela(1, "G180", new BigDecimal("180.00"), "Liviano");
        GramajeTelaDTO dto1 = GramajeTelaDTO.builder().idGramaje(1).codigoGramaje("G180").categoriaVestuario("Liviano").build();

        when(gramajeRepository.findByCategoriaVestuarioIgnoreCase("Liviano")).thenReturn(List.of(g1));
        when(mapper.toDTO(g1)).thenReturn(dto1);

        List<GramajeTelaDTO> resultado = gramajeTelaServiceImpl.listarPorCategoriaVestuario("Liviano");

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el gramaje existe")
    void eliminar_ok() {
        when(gramajeRepository.existsById(1)).thenReturn(true);

        gramajeTelaServiceImpl.eliminar(1);

        verify(gramajeRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el gramaje no existe")
    void eliminar_noExiste() {
        when(gramajeRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> gramajeTelaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(gramajeRepository, never()).deleteById(any());
    }
}
