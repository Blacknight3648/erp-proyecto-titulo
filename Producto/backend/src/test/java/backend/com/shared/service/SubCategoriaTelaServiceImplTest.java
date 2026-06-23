package backend.com.shared.service;

import backend.com.shared.application.dto.SubCategoriaTelaDTO;
import backend.com.shared.application.service.impl.SubCategoriaTelaServiceImpl;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.domain.model.SubCategoriaTela;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.SubCategoriaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.CategoriaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaTelaRepository;
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
@DisplayName("SubCategoriaTelaServiceImpl")
class SubCategoriaTelaServiceImplTest {

    @Mock
    private SubCategoriaTelaRepository subCategoriaTelaRepository;

    @Mock
    private CategoriaTelaRepository categoriaTelaRepository;

    @Mock
    private SubCategoriaTelaMapper mapper;

    @InjectMocks
    private SubCategoriaTelaServiceImpl subCategoriaTelaServiceImpl;

    // ---------------- HELPERS ----------------

    private CategoriaTela categoriaTela(Integer id, String nombre) {
        return CategoriaTela.builder()
                .idCategoriaTela(id)
                .nombreCategoriaTela(nombre)
                .build();
    }

    private SubCategoriaTela subCategoriaTela(Integer id, String codigo, String nombre, CategoriaTela categoriaTela) {
        return SubCategoriaTela.builder()
                .idSubCategoriaTela(id)
                .codigoSubCategoriaTela(codigo)
                .nombreSubCategoriaTela(nombre)
                .categoriaTela(categoriaTela)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado y la categoría existe")
    void crear_ok() {
        CategoriaTela categoria = categoriaTela(1, "Telas");
        SubCategoriaTelaDTO dto = SubCategoriaTelaDTO.builder()
                .codigoSubCategoriaTela("ALG")
                .nombreSubCategoriaTela("Algodón")
                .idCategoriaTela(1)
                .build();

        SubCategoriaTela guardada = subCategoriaTela(1, "ALG", "Algodón", categoria);
        SubCategoriaTelaDTO esperado = SubCategoriaTelaDTO.builder()
                .idSubCategoriaTela(1)
                .codigoSubCategoriaTela("ALG")
                .nombreSubCategoriaTela("Algodón")
                .idCategoriaTela(1)
                .build();

        when(subCategoriaTelaRepository.existsByCodigoSubCategoriaTela("ALG")).thenReturn(false);
        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(subCategoriaTelaRepository.save(any(SubCategoriaTela.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        SubCategoriaTelaDTO resultado = subCategoriaTelaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(subCategoriaTelaRepository).save(argThat(s ->
                s.getCodigoSubCategoriaTela().equals("ALG")
                        && s.getNombreSubCategoriaTela().equals("Algodón")
                        && s.getCategoriaTela() == categoria));
    }

    @Test
    @DisplayName("crear lanza excepción si el código de subcategoría ya existe")
    void crear_duplicado() {
        SubCategoriaTelaDTO dto = SubCategoriaTelaDTO.builder()
                .codigoSubCategoriaTela("ALG")
                .nombreSubCategoriaTela("Algodón")
                .idCategoriaTela(1)
                .build();

        when(subCategoriaTelaRepository.existsByCodigoSubCategoriaTela("ALG")).thenReturn(true);

        assertThatThrownBy(() -> subCategoriaTelaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(subCategoriaTelaRepository, never()).save(any());
        verify(categoriaTelaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("crear lanza excepción si la categoría padre no existe")
    void crear_categoriaNoExiste() {
        SubCategoriaTelaDTO dto = SubCategoriaTelaDTO.builder()
                .codigoSubCategoriaTela("ALG")
                .nombreSubCategoriaTela("Algodón")
                .idCategoriaTela(99)
                .build();

        when(subCategoriaTelaRepository.existsByCodigoSubCategoriaTela("ALG")).thenReturn(false);
        when(categoriaTelaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subCategoriaTelaServiceImpl.crear(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(subCategoriaTelaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando la subcategoría y la nueva categoría existen")
    void actualizar_ok() {
        CategoriaTela categoriaAntigua = categoriaTela(1, "Telas");
        CategoriaTela categoriaNueva = categoriaTela(2, "Accesorios");
        SubCategoriaTela existente = subCategoriaTela(1, "ALG", "Algodón", categoriaAntigua);
        SubCategoriaTelaDTO dto = SubCategoriaTelaDTO.builder()
                .codigoSubCategoriaTela("BOT")
                .nombreSubCategoriaTela("Botones")
                .idCategoriaTela(2)
                .build();
        SubCategoriaTelaDTO esperado = SubCategoriaTelaDTO.builder()
                .idSubCategoriaTela(1)
                .codigoSubCategoriaTela("BOT")
                .nombreSubCategoriaTela("Botones")
                .idCategoriaTela(2)
                .build();

        when(subCategoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.findById(2)).thenReturn(Optional.of(categoriaNueva));
        when(subCategoriaTelaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        SubCategoriaTelaDTO resultado = subCategoriaTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoSubCategoriaTela()).isEqualTo("BOT");
        assertThat(existente.getNombreSubCategoriaTela()).isEqualTo("Botones");
        assertThat(existente.getCategoriaTela()).isEqualTo(categoriaNueva);
    }

    @Test
    @DisplayName("actualizar lanza excepción si la subcategoría no existe")
    void actualizar_noExiste() {
        SubCategoriaTelaDTO dto = SubCategoriaTelaDTO.builder()
                .codigoSubCategoriaTela("BOT")
                .nombreSubCategoriaTela("Botones")
                .idCategoriaTela(2)
                .build();

        when(subCategoriaTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subCategoriaTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(subCategoriaTelaRepository, never()).save(any());
        verifyNoInteractions(categoriaTelaRepository);
    }

    @Test
    @DisplayName("actualizar lanza excepción si la nueva categoría padre no existe")
    void actualizar_categoriaNoExiste() {
        CategoriaTela categoriaAntigua = categoriaTela(1, "Telas");
        SubCategoriaTela existente = subCategoriaTela(1, "ALG", "Algodón", categoriaAntigua);
        SubCategoriaTelaDTO dto = SubCategoriaTelaDTO.builder()
                .codigoSubCategoriaTela("BOT")
                .nombreSubCategoriaTela("Botones")
                .idCategoriaTela(99)
                .build();

        when(subCategoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subCategoriaTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(subCategoriaTelaRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la subcategoría existe")
    void obtenerPorId_ok() {
        CategoriaTela categoria = categoriaTela(1, "Telas");
        SubCategoriaTela existente = subCategoriaTela(1, "ALG", "Algodón", categoria);
        SubCategoriaTelaDTO esperado = SubCategoriaTelaDTO.builder().idSubCategoriaTela(1).codigoSubCategoriaTela("ALG").build();

        when(subCategoriaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        SubCategoriaTelaDTO resultado = subCategoriaTelaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la subcategoría no existe")
    void obtenerPorId_noExiste() {
        when(subCategoriaTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subCategoriaTelaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        CategoriaTela categoria = categoriaTela(1, "Telas");
        SubCategoriaTela s1 = subCategoriaTela(1, "ALG", "Algodón", categoria);
        SubCategoriaTelaDTO dto1 = SubCategoriaTelaDTO.builder().idSubCategoriaTela(1).codigoSubCategoriaTela("ALG").build();

        when(subCategoriaTelaRepository.findAll()).thenReturn(List.of(s1));
        when(mapper.toDTO(s1)).thenReturn(dto1);

        List<SubCategoriaTelaDTO> resultado = subCategoriaTelaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarPorCategoriaTela ----------------

    @Test
    @DisplayName("listarPorCategoriaTela mapea correctamente las subcategorías de la categoría")
    void listarPorCategoriaTela_ok() {
        CategoriaTela categoria = categoriaTela(1, "Telas");
        SubCategoriaTela s1 = subCategoriaTela(1, "ALG", "Algodón", categoria);
        SubCategoriaTelaDTO dto1 = SubCategoriaTelaDTO.builder().idSubCategoriaTela(1).codigoSubCategoriaTela("ALG").idCategoriaTela(1).build();

        when(subCategoriaTelaRepository.findByCategoriaTelajId(1)).thenReturn(List.of(s1));
        when(mapper.toDTO(s1)).thenReturn(dto1);

        List<SubCategoriaTelaDTO> resultado = subCategoriaTelaServiceImpl.listarPorCategoriaTela(1);

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la subcategoría existe")
    void eliminar_ok() {
        when(subCategoriaTelaRepository.existsById(1)).thenReturn(true);

        subCategoriaTelaServiceImpl.eliminar(1);

        verify(subCategoriaTelaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la subcategoría no existe")
    void eliminar_noExiste() {
        when(subCategoriaTelaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> subCategoriaTelaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(subCategoriaTelaRepository, never()).deleteById(any());
    }
}
