package backend.com.comercial.service;

import backend.com.comercial.application.dto.CamposPlantillaDTO;
import backend.com.comercial.application.service.impl.CamposPlantillaServiceImpl;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.CamposPlantillaMapper;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
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
@DisplayName("CamposPlantillaServiceImpl")
class CamposPlantillaServiceImplTest {

    @Mock
    private CamposPlantillaRepository plantillaRepository;

    @Mock
    private CamposPlantillaMapper mapper;

    @InjectMocks
    private CamposPlantillaServiceImpl camposPlantillaServiceImpl;

    // ---------------- HELPERS ----------------

    private CamposPlantilla plantilla(Long id, String nombreCampo) {
        return CamposPlantilla.builder()
                .idPlantilla(id)
                .nombreCampo(nombreCampo)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el nombre no está duplicado")
    void crear_ok() {
        CamposPlantillaDTO dto = CamposPlantillaDTO.builder()
                .nombreCampo("Forro")
                .build();

        CamposPlantilla guardada = plantilla(1L, "Forro");
        CamposPlantillaDTO esperado = CamposPlantillaDTO.builder()
                .idPlantilla(1L)
                .nombreCampo("Forro")
                .build();

        when(plantillaRepository.existsByNombreCampo("Forro")).thenReturn(false);
        when(plantillaRepository.save(any(CamposPlantilla.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        CamposPlantillaDTO resultado = camposPlantillaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(plantillaRepository).save(any(CamposPlantilla.class));
    }

    @Test
    @DisplayName("crear lanza excepción si el nombre de campo ya existe")
    void crear_duplicado() {
        CamposPlantillaDTO dto = CamposPlantillaDTO.builder()
                .nombreCampo("Forro")
                .build();

        when(plantillaRepository.existsByNombreCampo("Forro")).thenReturn(true);

        assertThatThrownBy(() -> camposPlantillaServiceImpl.crear(dto))
                .isInstanceOf(DuplicadoException.class);

        verify(plantillaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica el nombre cuando el nuevo nombre no está duplicado")
    void actualizar_ok() {
        CamposPlantilla existente = plantilla(1L, "Forro");
        CamposPlantillaDTO dto = CamposPlantillaDTO.builder()
                .nombreCampo("Capucha")
                .build();
        CamposPlantillaDTO esperado = CamposPlantillaDTO.builder()
                .idPlantilla(1L)
                .nombreCampo("Capucha")
                .build();

        when(plantillaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(plantillaRepository.existsByNombreCampo("Capucha")).thenReturn(false);
        when(plantillaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        CamposPlantillaDTO resultado = camposPlantillaServiceImpl.actualizar(1L, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreCampo()).isEqualTo("Capucha");
    }

    @Test
    @DisplayName("actualizar permite mantener el mismo nombre (ignorando mayúsculas/minúsculas)")
    void actualizar_mismoNombre() {
        CamposPlantilla existente = plantilla(1L, "Forro");
        CamposPlantillaDTO dto = CamposPlantillaDTO.builder()
                .nombreCampo("FORRO")
                .build();
        CamposPlantillaDTO esperado = CamposPlantillaDTO.builder()
                .idPlantilla(1L)
                .nombreCampo("FORRO")
                .build();

        when(plantillaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(plantillaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        CamposPlantillaDTO resultado = camposPlantillaServiceImpl.actualizar(1L, dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(plantillaRepository, never()).existsByNombreCampo(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si el nuevo nombre ya existe en otra plantilla")
    void actualizar_duplicado() {
        CamposPlantilla existente = plantilla(1L, "Forro");
        CamposPlantillaDTO dto = CamposPlantillaDTO.builder()
                .nombreCampo("Capucha")
                .build();

        when(plantillaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(plantillaRepository.existsByNombreCampo("Capucha")).thenReturn(true);

        assertThatThrownBy(() -> camposPlantillaServiceImpl.actualizar(1L, dto))
                .isInstanceOf(DuplicadoException.class);

        verify(plantillaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si la plantilla no existe")
    void actualizar_noExiste() {
        CamposPlantillaDTO dto = CamposPlantillaDTO.builder()
                .nombreCampo("Capucha")
                .build();

        when(plantillaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> camposPlantillaServiceImpl.actualizar(1L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la plantilla existe")
    void obtenerPorId_ok() {
        CamposPlantilla existente = plantilla(1L, "Forro");
        CamposPlantillaDTO esperado = CamposPlantillaDTO.builder()
                .idPlantilla(1L)
                .nombreCampo("Forro")
                .build();

        when(plantillaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        CamposPlantillaDTO resultado = camposPlantillaServiceImpl.obtenerPorId(1L);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la plantilla no existe")
    void obtenerPorId_noExiste() {
        when(plantillaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> camposPlantillaServiceImpl.obtenerPorId(1L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas retorna solo los campos activos")
    void listarTodas_ok() {
        CamposPlantilla p1 = plantilla(1L, "Forro");
        CamposPlantilla p2 = plantilla(2L, "Capucha");
        CamposPlantillaDTO dto1 = CamposPlantillaDTO.builder().idPlantilla(1L).nombreCampo("Forro").build();
        CamposPlantillaDTO dto2 = CamposPlantillaDTO.builder().idPlantilla(2L).nombreCampo("Capucha").build();

        when(plantillaRepository.findAllActivos()).thenReturn(List.of(p1, p2));
        when(mapper.toDTO(p1)).thenReturn(dto1);
        when(mapper.toDTO(p2)).thenReturn(dto2);

        List<CamposPlantillaDTO> resultado = camposPlantillaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar hace soft delete (activo=false) cuando la plantilla existe")
    void eliminar_ok() {
        CamposPlantilla existente = plantilla(1L, "Forro");
        when(plantillaRepository.findById(1L)).thenReturn(Optional.of(existente));

        camposPlantillaServiceImpl.eliminar(1L);

        assertThat(existente.isActivo()).isFalse();
        verify(plantillaRepository).save(existente);
        verify(plantillaRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("eliminar lanza excepción si la plantilla no existe")
    void eliminar_noExiste() {
        when(plantillaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> camposPlantillaServiceImpl.eliminar(1L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(plantillaRepository, never()).deleteById(any());
        verify(plantillaRepository, never()).save(any());
    }
}
