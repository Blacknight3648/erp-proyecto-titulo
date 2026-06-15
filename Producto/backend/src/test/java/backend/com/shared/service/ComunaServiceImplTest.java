package backend.com.shared.service;

import backend.com.shared.application.dto.ComunaDTO;
import backend.com.shared.application.dto.RegionDTO;
import backend.com.shared.application.service.impl.ComunaServiceImpl;
import backend.com.shared.domain.model.Comuna;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.domain.model.Region;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ComunaNotFoundException;
import backend.com.shared.exception.RegionNotFoundException;
import backend.com.shared.infrastructure.mapper.ComunaMapper;
import backend.com.shared.infrastructure.persistence.repository.ComunaRepository;
import backend.com.shared.infrastructure.persistence.repository.RegionRepository;
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
@DisplayName("ComunaServiceImpl")
class ComunaServiceImplTest {

    @Mock
    private ComunaRepository comunaRepository;

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private ComunaMapper comunaMapper;

    @InjectMocks
    private ComunaServiceImpl comunaServiceImpl;

    // ---------------- HELPERS ----------------

    private Region region(Long id, String nombre) {
        return Region.builder()
                .regionId(id)
                .nombreRegion(nombre)
                .pais(Pais.builder().idPais(1L).nombrePais("Chile").build())
                .build();
    }

    private Comuna comuna(Long id, String nombre, Region region) {
        return Comuna.builder()
                .comunaId(id)
                .nombreComuna(nombre)
                .region(region)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        Region valparaiso = region(1L, "Valparaíso");
        Comuna c1 = comuna(1L, "Viña del Mar", valparaiso);
        Comuna c2 = comuna(2L, "Valparaíso", valparaiso);
        ComunaDTO dto1 = ComunaDTO.builder().comunaId(1L).nombreComuna("Viña del Mar").build();
        ComunaDTO dto2 = ComunaDTO.builder().comunaId(2L).nombreComuna("Valparaíso").build();

        when(comunaRepository.findAll()).thenReturn(List.of(c1, c2));
        when(comunaMapper.toDTO(c1)).thenReturn(dto1);
        when(comunaMapper.toDTO(c2)).thenReturn(dto2);

        List<ComunaDTO> resultado = comunaServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la comuna existe")
    void obtenerPorId_ok() {
        Region valparaiso = region(1L, "Valparaíso");
        Comuna existente = comuna(1L, "Viña del Mar", valparaiso);
        ComunaDTO esperado = ComunaDTO.builder().comunaId(1L).nombreComuna("Viña del Mar").build();

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(comunaMapper.toDTO(existente)).thenReturn(esperado);

        Optional<ComunaDTO> resultado = comunaServiceImpl.obtenerPorId(1L);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si la comuna no existe")
    void obtenerPorId_noExiste() {
        when(comunaRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<ComunaDTO> resultado = comunaServiceImpl.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- listarPorRegion ----------------

    @Test
    @DisplayName("listarPorRegion mapea correctamente las comunas de la región")
    void listarPorRegion_ok() {
        Region valparaiso = region(1L, "Valparaíso");
        Comuna c1 = comuna(1L, "Viña del Mar", valparaiso);
        ComunaDTO dto1 = ComunaDTO.builder().comunaId(1L).nombreComuna("Viña del Mar").build();

        when(regionRepository.existsById(1L)).thenReturn(true);
        when(comunaRepository.findByRegionId(1L)).thenReturn(List.of(c1));
        when(comunaMapper.toDTO(c1)).thenReturn(dto1);

        List<ComunaDTO> resultado = comunaServiceImpl.listarPorRegion(1L);

        assertThat(resultado).containsExactly(dto1);
    }

    @Test
    @DisplayName("listarPorRegion lanza excepción si la región no existe")
    void listarPorRegion_regionNoExiste() {
        when(regionRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> comunaServiceImpl.listarPorRegion(1L))
                .isInstanceOf(RegionNotFoundException.class);

        verify(comunaRepository, never()).findByRegionId(any());
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando la región existe y el nombre no está duplicado")
    void crear_ok() {
        Region valparaiso = region(1L, "Valparaíso");
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna(" Viña del Mar ")
                .region(RegionDTO.builder().regionId(1L).build())
                .build();

        Comuna guardado = comuna(1L, "Viña del Mar", valparaiso);
        ComunaDTO esperado = ComunaDTO.builder().comunaId(1L).nombreComuna("Viña del Mar").build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(valparaiso));
        when(comunaRepository.findByNombreComuna(" Viña del Mar ")).thenReturn(Optional.empty());
        when(comunaRepository.save(any(Comuna.class))).thenReturn(guardado);
        when(comunaMapper.toDTO(guardado)).thenReturn(esperado);

        ComunaDTO resultado = comunaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(comunaRepository).save(argThat(c ->
                c.getNombreComuna().equals("Viña del Mar") && c.getRegion() == valparaiso));
    }

    @Test
    @DisplayName("crear lanza excepción si no se especifica la región")
    void crear_sinRegion() {
        ComunaDTO dto = ComunaDTO.builder().nombreComuna("Viña del Mar").build();

        assertThatThrownBy(() -> comunaServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verifyNoInteractions(regionRepository, comunaRepository);
    }

    @Test
    @DisplayName("crear lanza excepción si la región no existe")
    void crear_regionNoExiste() {
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna("Viña del Mar")
                .region(RegionDTO.builder().regionId(1L).build())
                .build();

        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> comunaServiceImpl.crear(dto))
                .isInstanceOf(RegionNotFoundException.class);

        verify(comunaRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el nombre de la comuna ya existe")
    void crear_duplicado() {
        Region valparaiso = region(1L, "Valparaíso");
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna("Viña del Mar")
                .region(RegionDTO.builder().regionId(1L).build())
                .build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(valparaiso));
        when(comunaRepository.findByNombreComuna("Viña del Mar")).thenReturn(Optional.of(comuna(1L, "Viña del Mar", valparaiso)));

        assertThatThrownBy(() -> comunaServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(comunaRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica nombre y región cuando la comuna existe")
    void actualizar_ok() {
        Region valparaiso = region(1L, "Valparaíso");
        Region metropolitana = region(2L, "Metropolitana");
        Comuna existente = comuna(1L, "Viña del Mar", valparaiso);
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna(" Santiago ")
                .region(RegionDTO.builder().regionId(2L).build())
                .build();
        ComunaDTO esperado = ComunaDTO.builder().comunaId(1L).nombreComuna("Santiago").build();

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(2L)).thenReturn(Optional.of(metropolitana));
        when(comunaRepository.findByNombreComuna(" Santiago ")).thenReturn(Optional.empty());
        when(comunaRepository.save(existente)).thenReturn(existente);
        when(comunaMapper.toDTO(existente)).thenReturn(esperado);

        ComunaDTO resultado = comunaServiceImpl.actualizar(1L, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreComuna()).isEqualTo("Santiago");
        assertThat(existente.getRegion()).isEqualTo(metropolitana);
    }

    @Test
    @DisplayName("actualizar lanza excepción si el nombre ya existe en otra comuna")
    void actualizar_duplicado() {
        Region valparaiso = region(1L, "Valparaíso");
        Comuna existente = comuna(1L, "Viña del Mar", valparaiso);
        Comuna otra = comuna(2L, "Santiago", valparaiso);
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna("Santiago")
                .region(RegionDTO.builder().regionId(1L).build())
                .build();

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(1L)).thenReturn(Optional.of(valparaiso));
        when(comunaRepository.findByNombreComuna("Santiago")).thenReturn(Optional.of(otra));

        assertThatThrownBy(() -> comunaServiceImpl.actualizar(1L, dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(comunaRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si la comuna no existe")
    void actualizar_noExiste() {
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna("Santiago")
                .region(RegionDTO.builder().regionId(1L).build())
                .build();

        when(comunaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> comunaServiceImpl.actualizar(1L, dto))
                .isInstanceOf(ComunaNotFoundException.class);

        verify(comunaRepository, never()).save(any());
        verifyNoInteractions(regionRepository);
    }

    @Test
    @DisplayName("actualizar lanza excepción si la región no existe")
    void actualizar_regionNoExiste() {
        Region valparaiso = region(1L, "Valparaíso");
        Comuna existente = comuna(1L, "Viña del Mar", valparaiso);
        ComunaDTO dto = ComunaDTO.builder()
                .nombreComuna("Santiago")
                .region(RegionDTO.builder().regionId(99L).build())
                .build();

        when(comunaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> comunaServiceImpl.actualizar(1L, dto))
                .isInstanceOf(RegionNotFoundException.class);

        verify(comunaRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la comuna existe")
    void eliminar_ok() {
        when(comunaRepository.existsById(1L)).thenReturn(true);

        comunaServiceImpl.eliminar(1L);

        verify(comunaRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la comuna no existe")
    void eliminar_noExiste() {
        when(comunaRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> comunaServiceImpl.eliminar(1L))
                .isInstanceOf(ComunaNotFoundException.class);

        verify(comunaRepository, never()).deleteById(any());
    }
}
