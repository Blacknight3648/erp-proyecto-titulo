package backend.com.comercial.service;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.impl.ArticulosCamposPlantillaServiceImpl;
import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.domain.repository.ArticuloCamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.ArticuloCamposPlantillaMapper;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    private ArticuloCamposPlantillaRepository modeloPlantillaRepository;

    @Mock
    private ArticuloRepository articuloRepository;

    @Mock
    private ArticuloCamposPlantillaMapper mapper;

    @InjectMocks
    private ArticulosCamposPlantillaServiceImpl service;

    private Articulo articulo(Integer id) {
        Articulo a = new Articulo();
        a.setIdArticulo(id);
        return a;
    }

    @Test
    @DisplayName("guardar crea una fila nueva cuando el artículo no tiene configuración")
    void guardar_creaNueva() {
        ArticuloCamposPlantillaDTO dto = ArticuloCamposPlantillaDTO.builder()
                .idArticulo(1)
                .camposPlantilla(List.of("forro", "cuello"))
                .build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(articulo(1)));
        when(modeloPlantillaRepository.findByArticuloId(1)).thenReturn(Optional.empty());
        when(modeloPlantillaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDTO(any())).thenReturn(dto);

        service.guardar(dto);

        ArgumentCaptor<ArticuloCamposPlantilla> captor = ArgumentCaptor.forClass(ArticuloCamposPlantilla.class);
        verify(modeloPlantillaRepository).save(captor.capture());
        assertThat(captor.getValue().getCampos()).containsExactly("forro", "cuello");
        assertThat(captor.getValue().getArticulo().getIdArticulo()).isEqualTo(1);
    }

    @Test
    @DisplayName("guardar actualiza (upsert) la fila existente del artículo, sin duplicar")
    void guardar_actualizaExistente() {
        ArticuloCamposPlantillaDTO dto = ArticuloCamposPlantillaDTO.builder()
                .idArticulo(1)
                .camposPlantilla(List.of("mangas", "  ", "mangas", "gorro")) // se normaliza
                .build();

        ArticuloCamposPlantilla existente = ArticuloCamposPlantilla.builder()
                .idModeloPlantilla(10L)
                .articulo(articulo(1))
                .campos(List.of("forro"))
                .build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(articulo(1)));
        when(modeloPlantillaRepository.findByArticuloId(1)).thenReturn(Optional.of(existente));
        when(modeloPlantillaRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDTO(any())).thenReturn(dto);

        service.guardar(dto);

        ArgumentCaptor<ArticuloCamposPlantilla> captor = ArgumentCaptor.forClass(ArticuloCamposPlantilla.class);
        verify(modeloPlantillaRepository).save(captor.capture());
        assertThat(captor.getValue().getIdModeloPlantilla()).isEqualTo(10L); // misma fila
        assertThat(captor.getValue().getCampos()).containsExactly("mangas", "gorro"); // trim + distinct
    }

    @Test
    @DisplayName("guardar lanza excepción si el artículo no existe")
    void guardar_articuloNoExiste() {
        ArticuloCamposPlantillaDTO dto = ArticuloCamposPlantillaDTO.builder()
                .idArticulo(99).camposPlantilla(List.of("forro")).build();
        when(articuloRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.guardar(dto)).isInstanceOf(EntityNotFoundException.class);
        verify(modeloPlantillaRepository, never()).save(any());
    }

    @Test
    @DisplayName("guardar lanza excepción si la lista de campos queda vacía")
    void guardar_sinCampos() {
        ArticuloCamposPlantillaDTO dto = ArticuloCamposPlantillaDTO.builder()
                .idArticulo(1).camposPlantilla(List.of("  ", "")).build();
        when(articuloRepository.findById(1)).thenReturn(Optional.of(articulo(1)));

        assertThatThrownBy(() -> service.guardar(dto)).isInstanceOf(BusinessRuleException.class);
        verify(modeloPlantillaRepository, never()).save(any());
    }

    @Test
    @DisplayName("obtenerPorArticulo devuelve la fila mapeada si existe")
    void obtenerPorArticulo_ok() {
        ArticuloCamposPlantilla entidad = ArticuloCamposPlantilla.builder()
                .articulo(articulo(1)).campos(List.of("forro")).build();
        ArticuloCamposPlantillaDTO dto = ArticuloCamposPlantillaDTO.builder().idArticulo(1).build();

        when(modeloPlantillaRepository.findByArticuloId(1)).thenReturn(Optional.of(entidad));
        when(mapper.toDTO(entidad)).thenReturn(dto);

        assertThat(service.obtenerPorArticulo(1)).contains(dto);
    }

    @Test
    @DisplayName("eliminarPorArticulo borra cuando existe configuración")
    void eliminarPorArticulo_ok() {
        when(modeloPlantillaRepository.findByArticuloId(1))
                .thenReturn(Optional.of(ArticuloCamposPlantilla.builder().articulo(articulo(1)).build()));

        service.eliminarPorArticulo(1);

        verify(modeloPlantillaRepository).deleteByArticuloId(1);
    }

    @Test
    @DisplayName("eliminarPorArticulo lanza excepción si no hay configuración")
    void eliminarPorArticulo_noExiste() {
        when(modeloPlantillaRepository.findByArticuloId(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.eliminarPorArticulo(1)).isInstanceOf(EntityNotFoundException.class);
        verify(modeloPlantillaRepository, never()).deleteByArticuloId(any());
    }
}
