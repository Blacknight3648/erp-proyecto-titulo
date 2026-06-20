package backend.com.comercial.service;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;
import backend.com.comercial.application.dto.SCOSPlantillaMaterialVinculoDTO;
import backend.com.comercial.application.service.impl.DescripcionPlantillaServiceImpl;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.model.DescripcionPlantilla;
import backend.com.comercial.domain.model.SCOSPlantillaMaterialVinculo;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.domain.repository.DescripcionPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.DescripcionPlantillaMapper;
import backend.com.comercial.infrastructure.mapper.SCOSPlantillaMaterialVinculoMapper;
import backend.com.shared.exception.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DescripcionPlantillaServiceImpl")
class DescripcionPlantillaServiceImplTest {

    @Mock
    private DescripcionPlantillaRepository descripcionRepository;

    @Mock
    private CamposPlantillaRepository plantillaRepository;

    @Mock
    private DescripcionPlantillaMapper mapper;

    @Mock
    private SCOSPlantillaMaterialVinculoMapper vinculoMapper;

    @InjectMocks
    private DescripcionPlantillaServiceImpl descripcionPlantillaServiceImpl;

    // ---------------- HELPERS ----------------

    private CamposPlantilla plantilla(Long id) {
        return CamposPlantilla.builder().idPlantilla(id).nombreCampo("Forro").build();
    }

    private SCOSPlantillaMaterialVinculo vinculo(Long id) {
        return SCOSPlantillaMaterialVinculo.builder()
                .id(id)
                .materialType("TELA")
                .materialId(100L)
                .cantidad(1)
                .build();
    }

    private SCOSPlantillaMaterialVinculoDTO vinculoDTO(Long id) {
        return SCOSPlantillaMaterialVinculoDTO.builder()
                .id(id)
                .materialType("TELA")
                .materialId(100L)
                .cantidad(1)
                .build();
    }

    private DescripcionPlantilla descripcion(Long id, Long idSCOS, CamposPlantilla plantilla,
            String valor, Boolean activo) {
        return DescripcionPlantilla.builder()
                .idDescripcionPlantilla(id)
                .idSCOS(idSCOS)
                .plantilla(plantilla)
                .valorDescripcion(valor)
                .activo(activo)
                .vinculos(new ArrayList<>())
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando la plantilla existe")
    void crear_ok() {
        CamposPlantilla plantilla = plantilla(2L);

        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idSCOS(1L)
                .idPlantilla(2L)
                .valorDescripcion("polar")
                .vinculos(null)
                .build();

        DescripcionPlantilla guardada = descripcion(10L, 1L, plantilla, "polar", true);
        DescripcionPlantillaDTO esperado = DescripcionPlantillaDTO.builder()
                .idDescripcionPlantilla(10L)
                .idSCOS(1L)
                .idPlantilla(2L)
                .valorDescripcion("polar")
                .activo(true)
                .build();

        when(plantillaRepository.findById(2L)).thenReturn(Optional.of(plantilla));
        when(descripcionRepository.save(any(DescripcionPlantilla.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        DescripcionPlantillaDTO resultado = descripcionPlantillaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(descripcionRepository).save(argThat(d ->
                d.getIdSCOS().equals(1L)
                        && d.getPlantilla() == plantilla
                        && d.getValorDescripcion().equals("polar")
                        && d.getActivo()
                        && d.getVinculos().isEmpty()));
    }

    @Test
    @DisplayName("crear respeta el valor de 'activo' y mapea los vínculos recibidos")
    void crear_conVinculosYActivoFalso() {
        CamposPlantilla plantilla = plantilla(2L);
        SCOSPlantillaMaterialVinculoDTO vDto = vinculoDTO(null);
        SCOSPlantillaMaterialVinculo vDomain = vinculo(null);

        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idSCOS(1L)
                .idPlantilla(2L)
                .valorDescripcion("polar")
                .activo(false)
                .vinculos(List.of(vDto))
                .build();

        DescripcionPlantilla guardada = descripcion(10L, 1L, plantilla, "polar", false);
        DescripcionPlantillaDTO esperado = DescripcionPlantillaDTO.builder()
                .idDescripcionPlantilla(10L)
                .idSCOS(1L)
                .idPlantilla(2L)
                .valorDescripcion("polar")
                .activo(false)
                .build();

        when(plantillaRepository.findById(2L)).thenReturn(Optional.of(plantilla));
        when(vinculoMapper.toDomain(vDto)).thenReturn(vDomain);
        when(descripcionRepository.save(any(DescripcionPlantilla.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        DescripcionPlantillaDTO resultado = descripcionPlantillaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(descripcionRepository).save(argThat(d ->
                !d.getActivo() && d.getVinculos().equals(List.of(vDomain))));
    }

    @Test
    @DisplayName("crear lanza excepción si la plantilla no existe")
    void crear_plantillaNoExiste() {
        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idSCOS(1L)
                .idPlantilla(2L)
                .valorDescripcion("polar")
                .build();

        when(plantillaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> descripcionPlantillaServiceImpl.crear(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(descripcionRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar cambia la plantilla cuando el idPlantilla recibido es distinto")
    void actualizar_cambiaPlantilla() {
        CamposPlantilla plantillaActual = plantilla(2L);
        CamposPlantilla plantillaNueva = plantilla(3L);
        DescripcionPlantilla existente = descripcion(10L, 1L, plantillaActual, "polar", true);

        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idPlantilla(3L)
                .valorDescripcion("algodon")
                .activo(false)
                .vinculos(null)
                .build();

        DescripcionPlantillaDTO esperado = DescripcionPlantillaDTO.builder()
                .idDescripcionPlantilla(10L)
                .idPlantilla(3L)
                .valorDescripcion("algodon")
                .activo(false)
                .build();

        when(descripcionRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(plantillaRepository.findById(3L)).thenReturn(Optional.of(plantillaNueva));
        when(descripcionRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        DescripcionPlantillaDTO resultado = descripcionPlantillaServiceImpl.actualizar(10L, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getPlantilla()).isEqualTo(plantillaNueva);
        assertThat(existente.getValorDescripcion()).isEqualTo("algodon");
        assertThat(existente.getActivo()).isFalse();
    }

    @Test
    @DisplayName("actualizar no busca la plantilla si el idPlantilla recibido es el mismo")
    void actualizar_mismaPlantilla() {
        CamposPlantilla plantillaActual = plantilla(2L);
        DescripcionPlantilla existente = descripcion(10L, 1L, plantillaActual, "polar", true);

        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idPlantilla(2L)
                .valorDescripcion("algodon")
                .vinculos(null)
                .build();

        DescripcionPlantillaDTO esperado = DescripcionPlantillaDTO.builder()
                .idDescripcionPlantilla(10L)
                .idPlantilla(2L)
                .valorDescripcion("algodon")
                .build();

        when(descripcionRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(descripcionRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        DescripcionPlantillaDTO resultado = descripcionPlantillaServiceImpl.actualizar(10L, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getPlantilla()).isEqualTo(plantillaActual);
        verify(plantillaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("actualizar no modifica 'activo' cuando viene null en el DTO")
    void actualizar_activoNull() {
        CamposPlantilla plantillaActual = plantilla(2L);
        DescripcionPlantilla existente = descripcion(10L, 1L, plantillaActual, "polar", true);

        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idPlantilla(2L)
                .valorDescripcion("algodon")
                .activo(null)
                .vinculos(null)
                .build();

        when(descripcionRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(descripcionRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(DescripcionPlantillaDTO.builder().build());

        descripcionPlantillaServiceImpl.actualizar(10L, dto);

        assertThat(existente.getActivo()).isTrue();
    }

    @Test
    @DisplayName("actualizar lanza excepción si la nueva plantilla no existe")
    void actualizar_plantillaNoExiste() {
        CamposPlantilla plantillaActual = plantilla(2L);
        DescripcionPlantilla existente = descripcion(10L, 1L, plantillaActual, "polar", true);

        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idPlantilla(3L)
                .valorDescripcion("algodon")
                .build();

        when(descripcionRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(plantillaRepository.findById(3L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> descripcionPlantillaServiceImpl.actualizar(10L, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(descripcionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si la descripción no existe")
    void actualizar_noExiste() {
        DescripcionPlantillaDTO dto = DescripcionPlantillaDTO.builder()
                .idPlantilla(2L)
                .valorDescripcion("algodon")
                .build();

        when(descripcionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> descripcionPlantillaServiceImpl.actualizar(10L, dto))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la descripción existe")
    void obtenerPorId_ok() {
        CamposPlantilla plantilla = plantilla(2L);
        DescripcionPlantilla existente = descripcion(10L, 1L, plantilla, "polar", true);
        DescripcionPlantillaDTO esperado = DescripcionPlantillaDTO.builder()
                .idDescripcionPlantilla(10L)
                .build();

        when(descripcionRepository.findById(10L)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        DescripcionPlantillaDTO resultado = descripcionPlantillaServiceImpl.obtenerPorId(10L);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la descripción no existe")
    void obtenerPorId_noExiste() {
        when(descripcionRepository.findById(10L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> descripcionPlantillaServiceImpl.obtenerPorId(10L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarPorSCOS ----------------

    @Test
    @DisplayName("listarPorSCOS mapea correctamente la lista")
    void listarPorSCOS_ok() {
        CamposPlantilla plantilla = plantilla(2L);
        DescripcionPlantilla d1 = descripcion(10L, 1L, plantilla, "polar", true);
        DescripcionPlantillaDTO dto1 = DescripcionPlantillaDTO.builder().idDescripcionPlantilla(10L).build();

        when(descripcionRepository.findByIdSCOS(1L)).thenReturn(List.of(d1));
        when(mapper.toDTO(d1)).thenReturn(dto1);

        List<DescripcionPlantillaDTO> resultado = descripcionPlantillaServiceImpl.listarPorSCOS(1L);

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la descripción existe")
    void eliminar_ok() {
        when(descripcionRepository.existsById(10L)).thenReturn(true);

        descripcionPlantillaServiceImpl.eliminar(10L);

        verify(descripcionRepository).deleteById(10L);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la descripción no existe")
    void eliminar_noExiste() {
        when(descripcionRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> descripcionPlantillaServiceImpl.eliminar(10L))
                .isInstanceOf(EntityNotFoundException.class);

        verify(descripcionRepository, never()).deleteById(any());
    }

    // ---------------- guardarMultiples (bulk) ----------------

    @Test
    @DisplayName("guardarMultiples persiste todas las descripciones del lote")
    void guardarMultiples_ok() {
        CamposPlantilla p2 = plantilla(2L);
        CamposPlantilla p3 = plantilla(3L);
        DescripcionPlantillaDTO d1 = DescripcionPlantillaDTO.builder().idSCOS(1L).idPlantilla(2L).valorDescripcion("polar").build();
        DescripcionPlantillaDTO d2 = DescripcionPlantillaDTO.builder().idSCOS(1L).idPlantilla(3L).valorDescripcion("redondo").build();

        when(plantillaRepository.findById(2L)).thenReturn(Optional.of(p2));
        when(plantillaRepository.findById(3L)).thenReturn(Optional.of(p3));
        when(descripcionRepository.save(any(DescripcionPlantilla.class))).thenAnswer(inv -> inv.getArgument(0));
        when(mapper.toDTO(any(DescripcionPlantilla.class))).thenReturn(DescripcionPlantillaDTO.builder().build());

        List<DescripcionPlantillaDTO> resultado = descripcionPlantillaServiceImpl.guardarMultiples(List.of(d1, d2));

        assertThat(resultado).hasSize(2);
        verify(descripcionRepository, times(2)).save(any(DescripcionPlantilla.class));
    }

    @Test
    @DisplayName("guardarMultiples revierte (lanza) si un idPlantilla del lote no existe")
    void guardarMultiples_plantillaInexistente() {
        DescripcionPlantillaDTO d1 = DescripcionPlantillaDTO.builder().idSCOS(1L).idPlantilla(2L).valorDescripcion("polar").build();
        DescripcionPlantillaDTO d2 = DescripcionPlantillaDTO.builder().idSCOS(1L).idPlantilla(99L).valorDescripcion("x").build();

        when(plantillaRepository.findById(2L)).thenReturn(Optional.of(plantilla(2L)));
        when(plantillaRepository.findById(99L)).thenReturn(Optional.empty());
        when(descripcionRepository.save(any(DescripcionPlantilla.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThatThrownBy(() -> descripcionPlantillaServiceImpl.guardarMultiples(List.of(d1, d2)))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("guardarMultiples con lista vacía o null no persiste nada")
    void guardarMultiples_vacio() {
        assertThat(descripcionPlantillaServiceImpl.guardarMultiples(List.of())).isEmpty();
        assertThat(descripcionPlantillaServiceImpl.guardarMultiples(null)).isEmpty();
        verify(descripcionRepository, never()).save(any());
    }
}
