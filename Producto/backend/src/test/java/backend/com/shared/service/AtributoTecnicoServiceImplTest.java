package backend.com.shared.service;

import backend.com.shared.application.dto.AtributoTecnicoDTO;
import backend.com.shared.application.service.impl.AtributoTecnicoServiceImpl;
import backend.com.shared.domain.model.AtributoTecnico;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.AtributoTecnicoMapper;
import backend.com.shared.infrastructure.persistence.repository.AtributoTecnicoRepository;
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
@DisplayName("AtributoTecnicoServiceImpl")
class AtributoTecnicoServiceImplTest {

    @Mock
    private AtributoTecnicoRepository atributoRepository;

    @Mock
    private AtributoTecnicoMapper mapper;

    @InjectMocks
    private AtributoTecnicoServiceImpl atributoTecnicoServiceImpl;

    // ---------------- HELPERS ----------------

    private AtributoTecnico atributo(Integer id, String codigo, String clasificacion, String descripcionTecnica, String impactoErp) {
        return AtributoTecnico.builder()
                .idAtributo(id)
                .codigoAtributo(codigo)
                .clasificacion(clasificacion)
                .descripcionTecnica(descripcionTecnica)
                .impactoErp(impactoErp)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        AtributoTecnicoDTO dto = AtributoTecnicoDTO.builder()
                .codigoAtributo("ATR-01")
                .clasificacion("Físico")
                .descripcionTecnica("Resistencia a la tracción")
                .impactoErp("Afecta el costo de producción")
                .build();

        AtributoTecnico guardado = atributo(1, "ATR-01", "Físico", "Resistencia a la tracción", "Afecta el costo de producción");
        AtributoTecnicoDTO esperado = AtributoTecnicoDTO.builder()
                .idAtributo(1)
                .codigoAtributo("ATR-01")
                .clasificacion("Físico")
                .descripcionTecnica("Resistencia a la tracción")
                .impactoErp("Afecta el costo de producción")
                .build();

        when(atributoRepository.existsByCodigoAtributo("ATR-01")).thenReturn(false);
        when(atributoRepository.save(any(AtributoTecnico.class))).thenReturn(guardado);
        when(mapper.toDTO(guardado)).thenReturn(esperado);

        AtributoTecnicoDTO resultado = atributoTecnicoServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(atributoRepository).save(argThat(a ->
                a.getCodigoAtributo().equals("ATR-01")
                        && a.getClasificacion().equals("Físico")
                        && a.getDescripcionTecnica().equals("Resistencia a la tracción")
                        && a.getImpactoErp().equals("Afecta el costo de producción")));
    }

    @Test
    @DisplayName("crear lanza excepción si el código de atributo ya existe")
    void crear_duplicado() {
        AtributoTecnicoDTO dto = AtributoTecnicoDTO.builder()
                .codigoAtributo("ATR-01")
                .descripcionTecnica("Resistencia a la tracción")
                .build();

        when(atributoRepository.existsByCodigoAtributo("ATR-01")).thenReturn(true);

        assertThatThrownBy(() -> atributoTecnicoServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(atributoRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el atributo existe")
    void actualizar_ok() {
        AtributoTecnico existente = atributo(1, "ATR-01", "Físico", "Resistencia a la tracción", "Afecta el costo de producción");
        AtributoTecnicoDTO dto = AtributoTecnicoDTO.builder()
                .codigoAtributo("ATR-02")
                .clasificacion("Químico")
                .descripcionTecnica("Resistencia al desteñido")
                .impactoErp("Afecta el control de calidad")
                .build();
        AtributoTecnicoDTO esperado = AtributoTecnicoDTO.builder()
                .idAtributo(1)
                .codigoAtributo("ATR-02")
                .clasificacion("Químico")
                .descripcionTecnica("Resistencia al desteñido")
                .impactoErp("Afecta el control de calidad")
                .build();

        when(atributoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(atributoRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        AtributoTecnicoDTO resultado = atributoTecnicoServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoAtributo()).isEqualTo("ATR-02");
        assertThat(existente.getClasificacion()).isEqualTo("Químico");
        assertThat(existente.getDescripcionTecnica()).isEqualTo("Resistencia al desteñido");
        assertThat(existente.getImpactoErp()).isEqualTo("Afecta el control de calidad");
    }

    @Test
    @DisplayName("actualizar lanza excepción si el atributo no existe")
    void actualizar_noExiste() {
        AtributoTecnicoDTO dto = AtributoTecnicoDTO.builder()
                .codigoAtributo("ATR-02")
                .descripcionTecnica("Resistencia al desteñido")
                .build();

        when(atributoRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> atributoTecnicoServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(atributoRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el atributo existe")
    void obtenerPorId_ok() {
        AtributoTecnico existente = atributo(1, "ATR-01", "Físico", "Resistencia a la tracción", "Afecta el costo de producción");
        AtributoTecnicoDTO esperado = AtributoTecnicoDTO.builder().idAtributo(1).codigoAtributo("ATR-01").build();

        when(atributoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        AtributoTecnicoDTO resultado = atributoTecnicoServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si el atributo no existe")
    void obtenerPorId_noExiste() {
        when(atributoRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> atributoTecnicoServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        AtributoTecnico a1 = atributo(1, "ATR-01", "Físico", "Resistencia a la tracción", "Afecta el costo de producción");
        AtributoTecnicoDTO dto1 = AtributoTecnicoDTO.builder().idAtributo(1).codigoAtributo("ATR-01").build();

        when(atributoRepository.findAll()).thenReturn(List.of(a1));
        when(mapper.toDTO(a1)).thenReturn(dto1);

        List<AtributoTecnicoDTO> resultado = atributoTecnicoServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarPorClasificacion ----------------

    @Test
    @DisplayName("listarPorClasificacion mapea correctamente los atributos de la clasificación")
    void listarPorClasificacion_ok() {
        AtributoTecnico a1 = atributo(1, "ATR-01", "Físico", "Resistencia a la tracción", "Afecta el costo de producción");
        AtributoTecnicoDTO dto1 = AtributoTecnicoDTO.builder().idAtributo(1).codigoAtributo("ATR-01").clasificacion("Físico").build();

        when(atributoRepository.findByClasificacionIgnoreCase("Físico")).thenReturn(List.of(a1));
        when(mapper.toDTO(a1)).thenReturn(dto1);

        List<AtributoTecnicoDTO> resultado = atributoTecnicoServiceImpl.listarPorClasificacion("Físico");

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el atributo existe")
    void eliminar_ok() {
        when(atributoRepository.existsById(1)).thenReturn(true);

        atributoTecnicoServiceImpl.eliminar(1);

        verify(atributoRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el atributo no existe")
    void eliminar_noExiste() {
        when(atributoRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> atributoTecnicoServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(atributoRepository, never()).deleteById(any());
    }
}
