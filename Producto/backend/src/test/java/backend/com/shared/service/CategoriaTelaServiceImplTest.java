package backend.com.shared.service;

import backend.com.shared.application.dto.CategoriaTelaDTO;
import backend.com.shared.application.service.impl.CategoriaTelaServiceImpl;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.CategoriaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaTelaRepository;
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
@DisplayName("CategoriaTelaServiceImpl")
class CategoriaTelaServiceImplTest {

    @Mock
    private CategoriaTelaRepository categoriaTelaRepository;

    @Mock
    private CategoriaTelaMapper mapper;

    @InjectMocks
    private CategoriaTelaServiceImpl categoriaTelaServiceImpl;

    // ---------------- HELPERS ----------------

    private CategoriaTela categoriaTela(Integer id, String codigo, String nombre) {
        return CategoriaTela.builder()
                .idCategoriaTela(id)
                .codigoCategoriaTela(codigo)
                .nombreCategoriaTela(nombre)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código y el nombre no están duplicados")
    void crear_ok() {
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Telas")
                .build();

        CategoriaTela guardada = categoriaTela(1, "TEL", "Telas");
        CategoriaTelaDTO esperado = CategoriaTelaDTO.builder()
                .idCategoriaTela(1)
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Telas")
                .build();

        when(categoriaTelaRepository.existsByCodigoCategoriaTela("TEL")).thenReturn(false);
        when(categoriaTelaRepository.existsByNombreCategoriaTela("Telas")).thenReturn(false);
        when(categoriaTelaRepository.save(any(CategoriaTela.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        CategoriaTelaDTO resultado = categoriaTelaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(categoriaTelaRepository).save(argThat(c ->
                c.getCodigoCategoriaTela().equals("TEL") && c.getNombreCategoriaTela().equals("Telas")));
    }

    @Test
    @DisplayName("crear lanza excepción si el código de categoría ya existe")
    void crear_codigoDuplicado() {
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Telas")
                .build();

        when(categoriaTelaRepository.existsByCodigoCategoriaTela("TEL")).thenReturn(true);

        assertThatThrownBy(() -> categoriaTelaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(categoriaTelaRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el nombre de categoría ya existe")
    void crear_nombreDuplicado() {
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Telas")
                .build();

        when(categoriaTelaRepository.existsByCodigoCategoriaTela("TEL")).thenReturn(false);
        when(categoriaTelaRepository.existsByNombreCategoriaTela("Telas")).thenReturn(true);

        assertThatThrownBy(() -> categoriaTelaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(categoriaTelaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica código y nombre cuando no están duplicados")
    void actualizar_ok() {
        CategoriaTela existente = categoriaTela(1, "TEL", "Telas");
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("ACC")
                .nombreCategoriaTela("Accesorios")
                .build();
        CategoriaTelaDTO esperado = CategoriaTelaDTO.builder()
                .idCategoriaTela(1)
                .codigoCategoriaTela("ACC")
                .nombreCategoriaTela("Accesorios")
                .build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.existsByCodigoCategoriaTela("ACC")).thenReturn(false);
        when(categoriaTelaRepository.existsByNombreCategoriaTela("Accesorios")).thenReturn(false);
        when(categoriaTelaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        CategoriaTelaDTO resultado = categoriaTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoCategoriaTela()).isEqualTo("ACC");
        assertThat(existente.getNombreCategoriaTela()).isEqualTo("Accesorios");
    }

    @Test
    @DisplayName("actualizar permite mantener el mismo código y nombre")
    void actualizar_sinCambios() {
        CategoriaTela existente = categoriaTela(1, "TEL", "Telas");
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Telas")
                .build();
        CategoriaTelaDTO esperado = CategoriaTelaDTO.builder()
                .idCategoriaTela(1)
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Telas")
                .build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        CategoriaTelaDTO resultado = categoriaTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(categoriaTelaRepository, never()).existsByCodigoCategoriaTela(any());
        verify(categoriaTelaRepository, never()).existsByNombreCategoriaTela(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si la categoría no existe")
    void actualizar_noExiste() {
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("ACC")
                .nombreCategoriaTela("Accesorios")
                .build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(categoriaTelaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el nuevo código ya existe")
    void actualizar_codigoDuplicado() {
        CategoriaTela existente = categoriaTela(1, "TEL", "Telas");
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("ACC")
                .nombreCategoriaTela("Telas")
                .build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.existsByCodigoCategoriaTela("ACC")).thenReturn(true);

        assertThatThrownBy(() -> categoriaTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(DuplicadoException.class);

        verify(categoriaTelaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el nuevo nombre ya existe")
    void actualizar_nombreDuplicado() {
        CategoriaTela existente = categoriaTela(1, "TEL", "Telas");
        CategoriaTelaDTO dto = CategoriaTelaDTO.builder()
                .codigoCategoriaTela("TEL")
                .nombreCategoriaTela("Accesorios")
                .build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.existsByNombreCategoriaTela("Accesorios")).thenReturn(true);

        assertThatThrownBy(() -> categoriaTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(DuplicadoException.class);

        verify(categoriaTelaRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la categoría existe")
    void obtenerPorId_ok() {
        CategoriaTela existente = categoriaTela(1, "TEL", "Telas");
        CategoriaTelaDTO esperado = CategoriaTelaDTO.builder().idCategoriaTela(1).codigoCategoriaTela("TEL").nombreCategoriaTela("Telas").build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        CategoriaTelaDTO resultado = categoriaTelaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la categoría no existe")
    void obtenerPorId_noExiste() {
        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaTelaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        CategoriaTela c1 = categoriaTela(1, "TEL", "Telas");
        CategoriaTela c2 = categoriaTela(2, "ACC", "Accesorios");
        CategoriaTelaDTO dto1 = CategoriaTelaDTO.builder().idCategoriaTela(1).codigoCategoriaTela("TEL").nombreCategoriaTela("Telas").build();
        CategoriaTelaDTO dto2 = CategoriaTelaDTO.builder().idCategoriaTela(2).codigoCategoriaTela("ACC").nombreCategoriaTela("Accesorios").build();

        when(categoriaTelaRepository.findAll()).thenReturn(List.of(c1, c2));
        when(mapper.toDTO(c1)).thenReturn(dto1);
        when(mapper.toDTO(c2)).thenReturn(dto2);

        List<CategoriaTelaDTO> resultado = categoriaTelaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la categoría existe")
    void eliminar_ok() {
        when(categoriaTelaRepository.existsById(1)).thenReturn(true);

        categoriaTelaServiceImpl.eliminar(1);

        verify(categoriaTelaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la categoría no existe")
    void eliminar_noExiste() {
        when(categoriaTelaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> categoriaTelaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(categoriaTelaRepository, never()).deleteById(any());
    }
}
