package backend.com.shared.service;

import backend.com.shared.application.dto.TipoArticuloDTO;
import backend.com.shared.application.service.impl.TipoArticuloServiceImpl;
import backend.com.shared.domain.model.TipoArticulo;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.TipoArticuloMapper;
import backend.com.shared.infrastructure.persistence.repository.TipoArticuloRepository;
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
@DisplayName("TipoArticuloServiceImpl")
class TipoArticuloServiceImplTest {

    @Mock
    private TipoArticuloRepository tipoArticuloRepository;

    @Mock
    private TipoArticuloMapper mapper;

    @InjectMocks
    private TipoArticuloServiceImpl tipoArticuloServiceImpl;

    // ---------------- HELPERS ----------------

    private TipoArticulo tipoArticulo(Integer id, String codigo, String nombre) {
        return TipoArticulo.builder()
                .idTipoArticulo(id)
                .codigo(codigo)
                .nombre(nombre)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el id y el código no están duplicados")
    void crear_ok() {
        TipoArticuloDTO dto = TipoArticuloDTO.builder()
                .idTipoArticulo(1)
                .codigo("PT")
                .nombre("Prenda Terminada")
                .build();

        TipoArticulo guardado = tipoArticulo(1, "PT", "Prenda Terminada");
        TipoArticuloDTO esperado = TipoArticuloDTO.builder()
                .idTipoArticulo(1)
                .codigo("PT")
                .nombre("Prenda Terminada")
                .build();

        when(tipoArticuloRepository.existsById(1)).thenReturn(false);
        when(tipoArticuloRepository.existsByCodigo("PT")).thenReturn(false);
        when(tipoArticuloRepository.save(any(TipoArticulo.class))).thenReturn(guardado);
        when(mapper.toDTO(guardado)).thenReturn(esperado);

        TipoArticuloDTO resultado = tipoArticuloServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(tipoArticuloRepository).save(any(TipoArticulo.class));
    }

    @Test
    @DisplayName("crear lanza excepción si el id ya existe")
    void crear_idDuplicado() {
        TipoArticuloDTO dto = TipoArticuloDTO.builder()
                .idTipoArticulo(1)
                .codigo("PT")
                .nombre("Prenda Terminada")
                .build();

        when(tipoArticuloRepository.existsById(1)).thenReturn(true);

        assertThatThrownBy(() -> tipoArticuloServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(tipoArticuloRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el código ya existe")
    void crear_codigoDuplicado() {
        TipoArticuloDTO dto = TipoArticuloDTO.builder()
                .idTipoArticulo(1)
                .codigo("PT")
                .nombre("Prenda Terminada")
                .build();

        when(tipoArticuloRepository.existsById(1)).thenReturn(false);
        when(tipoArticuloRepository.existsByCodigo("PT")).thenReturn(true);

        assertThatThrownBy(() -> tipoArticuloServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(tipoArticuloRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica el nombre cuando el tipo existe")
    void actualizar_ok() {
        TipoArticulo existente = tipoArticulo(1, "PT", "Prenda Terminada");
        TipoArticuloDTO dto = TipoArticuloDTO.builder()
                .nombre("Prenda Modificada")
                .build();
        TipoArticuloDTO esperado = TipoArticuloDTO.builder()
                .idTipoArticulo(1)
                .codigo("PT")
                .nombre("Prenda Modificada")
                .build();

        when(tipoArticuloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(tipoArticuloRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        TipoArticuloDTO resultado = tipoArticuloServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombre()).isEqualTo("Prenda Modificada");
    }

    @Test
    @DisplayName("actualizar lanza excepción si el tipo no existe")
    void actualizar_noExiste() {
        TipoArticuloDTO dto = TipoArticuloDTO.builder().nombre("Prenda Modificada").build();

        when(tipoArticuloRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoArticuloServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tipoArticuloRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el tipo existe")
    void obtenerPorId_ok() {
        TipoArticulo existente = tipoArticulo(1, "PT", "Prenda Terminada");
        TipoArticuloDTO esperado = TipoArticuloDTO.builder()
                .idTipoArticulo(1)
                .codigo("PT")
                .nombre("Prenda Terminada")
                .build();

        when(tipoArticuloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        TipoArticuloDTO resultado = tipoArticuloServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si el tipo no existe")
    void obtenerPorId_noExiste() {
        when(tipoArticuloRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoArticuloServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        TipoArticulo t1 = tipoArticulo(1, "PT", "Prenda Terminada");
        TipoArticulo t2 = tipoArticulo(2, "MP", "Materia Prima");
        TipoArticuloDTO dto1 = TipoArticuloDTO.builder().idTipoArticulo(1).codigo("PT").nombre("Prenda Terminada").build();
        TipoArticuloDTO dto2 = TipoArticuloDTO.builder().idTipoArticulo(2).codigo("MP").nombre("Materia Prima").build();

        when(tipoArticuloRepository.findAll()).thenReturn(List.of(t1, t2));
        when(mapper.toDTO(t1)).thenReturn(dto1);
        when(mapper.toDTO(t2)).thenReturn(dto2);

        List<TipoArticuloDTO> resultado = tipoArticuloServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el tipo existe")
    void eliminar_ok() {
        when(tipoArticuloRepository.existsById(1)).thenReturn(true);

        tipoArticuloServiceImpl.eliminar(1);

        verify(tipoArticuloRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el tipo no existe")
    void eliminar_noExiste() {
        when(tipoArticuloRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> tipoArticuloServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(tipoArticuloRepository, never()).deleteById(any());
    }
}
