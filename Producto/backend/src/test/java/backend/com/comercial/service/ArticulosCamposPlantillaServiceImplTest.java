package backend.com.comercial.service;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.impl.ArticulosCamposPlantillaServiceImpl;
import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.repository.ArticuloCamposPlantillaRepository;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.ArticuloCamposPlantillaMapper;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
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
@DisplayName("ArticulosCamposPlantillaServiceImpl")
public class ArticulosCamposPlantillaServiceImplTest {

    @Mock
    private ArticuloCamposPlantillaRepository articuloCamposPlantillaRepository;

    @Mock
    private ArticuloRepository articuloRepository;

    @Mock
    private CamposPlantillaRepository camposPlantillaRepository;

    @Mock
    private ArticuloCamposPlantillaMapper mapper;

    @InjectMocks
    private ArticulosCamposPlantillaServiceImpl articulosCamposPlantillaServiceImpl;


    // ---------------- HELPERS ----------------

    private Articulo articulo(Integer id) {
        Articulo articulo = new Articulo();
        articulo.setIdArticulo(id);
        return articulo;
    }

    private CamposPlantilla plantilla(Long id) {
        CamposPlantilla plantilla = new CamposPlantilla();
        plantilla.setIdPlantilla(id);
        return plantilla;
    }

    private ArticuloCamposPlantilla relacion(Articulo articulo,
                                             CamposPlantilla plantilla) {
        return ArticuloCamposPlantilla.builder()
                .articulo(articulo)
                .plantilla(plantilla)
                .build();
    }

    @Test
    @DisplayName("crear guarda correctamente una relación artículo-plantilla")
    void crear_ok() {

        ArticuloCamposPlantillaDTO dto = new ArticuloCamposPlantillaDTO();
        dto.setIdArticulo(1);
        dto.setIdPlantilla(2L);

        Articulo articulo = articulo(1);
        CamposPlantilla plantilla = plantilla(2L);

        ArticuloCamposPlantilla entidad =
                relacion(articulo, plantilla);

        ArticuloCamposPlantillaDTO esperado =
                new ArticuloCamposPlantillaDTO();

        when(articuloCamposPlantillaRepository
                .existsByArticuloIdAndPlantillaId(1, 2L))
                .thenReturn(false);

        when(articuloRepository.findById(1))
                .thenReturn(Optional.of(articulo));

        when(camposPlantillaRepository.findById(2L))
                .thenReturn(Optional.of(plantilla));

        when(articuloCamposPlantillaRepository.save(any()))
                .thenReturn(entidad);

        when(mapper.toDTO(entidad))
                .thenReturn(esperado);

        ArticuloCamposPlantillaDTO resultado =
                articulosCamposPlantillaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("crear lanza excepción cuando la relación ya existe")
    void crear_duplicado() {

        ArticuloCamposPlantillaDTO dto =
                new ArticuloCamposPlantillaDTO();

        dto.setIdArticulo(1);
        dto.setIdPlantilla(2L);

        when(articuloCamposPlantillaRepository
                .existsByArticuloIdAndPlantillaId(1, 2L))
                .thenReturn(true);

        assertThatThrownBy(() -> articulosCamposPlantillaServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(articuloRepository, never())
                .findById(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el artículo no existe")
    void crear_articuloNoExiste() {

        ArticuloCamposPlantillaDTO dto =
                new ArticuloCamposPlantillaDTO();

        dto.setIdArticulo(1);
        dto.setIdPlantilla(2L);

        when(articuloCamposPlantillaRepository
                .existsByArticuloIdAndPlantillaId(1, 2L))
                .thenReturn(false);

        when(articuloRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> articulosCamposPlantillaServiceImpl.crear(dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("crear lanza excepción si la plantilla no existe")
    void crear_plantillaNoExiste() {

        ArticuloCamposPlantillaDTO dto =
                new ArticuloCamposPlantillaDTO();

        dto.setIdArticulo(1);
        dto.setIdPlantilla(2L);

        Articulo articulo = articulo(1);

        when(articuloCamposPlantillaRepository
                .existsByArticuloIdAndPlantillaId(1, 2L))
                .thenReturn(false);

        when(articuloRepository.findById(1))
                .thenReturn(Optional.of(articulo));

        when(camposPlantillaRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> articulosCamposPlantillaServiceImpl.crear(dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("listarPorArticulo mapea correctamente la lista")
    void listarPorArticulo_ok() {

        Articulo articulo = articulo(1);
        CamposPlantilla plantilla = plantilla(2L);

        ArticuloCamposPlantilla entidad =
                relacion(articulo, plantilla);

        ArticuloCamposPlantillaDTO dto =
                new ArticuloCamposPlantillaDTO();

        when(articuloCamposPlantillaRepository
                .findByArticuloId(1))
                .thenReturn(List.of(entidad));

        when(mapper.toDTO(entidad))
                .thenReturn(dto);

        List<ArticuloCamposPlantillaDTO> resultado =
                articulosCamposPlantillaServiceImpl.listarPorArticulo(1);

        assertThat(resultado).hasSize(1).containsExactly(dto);
    }

    @Test
    @DisplayName("eliminar elimina correctamente")
    void eliminar_ok() {

        when(articuloCamposPlantillaRepository.existsById(1L))
                .thenReturn(true);

        articulosCamposPlantillaServiceImpl.eliminar(1L);

        verify(articuloCamposPlantillaRepository)
                .deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepción si no existe")
    void eliminar_noExiste() {

        when(articuloCamposPlantillaRepository.existsById(1L))
                .thenReturn(false);

        assertThatThrownBy(() -> articulosCamposPlantillaServiceImpl.eliminar(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(articuloCamposPlantillaRepository, never())
                .deleteById(any());
    }
}
