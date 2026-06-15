package backend.com.shared.service;

import backend.com.shared.application.dto.PaisDTO;
import backend.com.shared.application.dto.RegionDTO;
import backend.com.shared.application.service.impl.RegionServiceImpl;
import backend.com.shared.domain.model.Pais;
import backend.com.shared.domain.model.Region;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.PaisNotFoundException;
import backend.com.shared.exception.RegionNotFoundException;
import backend.com.shared.infrastructure.mapper.RegionMapper;
import backend.com.shared.infrastructure.persistence.repository.PaisRepository;
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
@DisplayName("RegionServiceImpl")
class RegionServiceImplTest {

    @Mock
    private RegionRepository regionRepository;

    @Mock
    private PaisRepository paisRepository;

    @Mock
    private RegionMapper regionMapper;

    @InjectMocks
    private RegionServiceImpl regionServiceImpl;

    // ---------------- HELPERS ----------------

    private Pais pais(Long id, String nombre) {
        return Pais.builder()
                .idPais(id)
                .nombrePais(nombre)
                .build();
    }

    private Region region(Long id, String nombre, Pais pais) {
        return Region.builder()
                .regionId(id)
                .nombreRegion(nombre)
                .pais(pais)
                .build();
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        Pais chile = pais(1L, "Chile");
        Region r1 = region(1L, "Valparaíso", chile);
        Region r2 = region(2L, "Metropolitana", chile);
        RegionDTO dto1 = RegionDTO.builder().regionId(1L).nombreRegion("Valparaíso").build();
        RegionDTO dto2 = RegionDTO.builder().regionId(2L).nombreRegion("Metropolitana").build();

        when(regionRepository.findAll()).thenReturn(List.of(r1, r2));
        when(regionMapper.toDTO(r1)).thenReturn(dto1);
        when(regionMapper.toDTO(r2)).thenReturn(dto2);

        List<RegionDTO> resultado = regionServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la región existe")
    void obtenerPorId_ok() {
        Pais chile = pais(1L, "Chile");
        Region existente = region(1L, "Valparaíso", chile);
        RegionDTO esperado = RegionDTO.builder().regionId(1L).nombreRegion("Valparaíso").build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(regionMapper.toDTO(existente)).thenReturn(esperado);

        Optional<RegionDTO> resultado = regionServiceImpl.obtenerPorId(1L);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("obtenerPorId retorna vacío si la región no existe")
    void obtenerPorId_noExiste() {
        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RegionDTO> resultado = regionServiceImpl.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- listarPorPais ----------------

    @Test
    @DisplayName("listarPorPais mapea correctamente las regiones del país")
    void listarPorPais_ok() {
        Pais chile = pais(1L, "Chile");
        Region r1 = region(1L, "Valparaíso", chile);
        RegionDTO dto1 = RegionDTO.builder().regionId(1L).nombreRegion("Valparaíso").build();

        when(paisRepository.existsById(1)).thenReturn(true);
        when(regionRepository.findByPaisId(1)).thenReturn(List.of(r1));
        when(regionMapper.toDTO(r1)).thenReturn(dto1);

        List<RegionDTO> resultado = regionServiceImpl.listarPorPais(1);

        assertThat(resultado).containsExactly(dto1);
    }

    @Test
    @DisplayName("listarPorPais lanza excepción si el país no existe")
    void listarPorPais_paisNoExiste() {
        when(paisRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> regionServiceImpl.listarPorPais(1))
                .isInstanceOf(PaisNotFoundException.class);

        verify(regionRepository, never()).findByPaisId(any());
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el país existe y el nombre no está duplicado")
    void crear_ok() {
        Pais chile = pais(1L, "Chile");
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion(" Valparaíso ")
                .pais(PaisDTO.builder().idPais(1L).build())
                .build();

        Region guardado = region(1L, "Valparaíso", chile);
        RegionDTO esperado = RegionDTO.builder().regionId(1L).nombreRegion("Valparaíso").build();

        when(paisRepository.findById(1)).thenReturn(Optional.of(chile));
        when(regionRepository.findByNombreRegion(" Valparaíso ")).thenReturn(Optional.empty());
        when(regionRepository.save(any(Region.class))).thenReturn(guardado);
        when(regionMapper.toDTO(guardado)).thenReturn(esperado);

        RegionDTO resultado = regionServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(regionRepository).save(argThat(r ->
                r.getNombreRegion().equals("Valparaíso") && r.getPais() == chile));
    }

    @Test
    @DisplayName("crear lanza excepción si no se especifica el país")
    void crear_sinPais() {
        RegionDTO dto = RegionDTO.builder().nombreRegion("Valparaíso").build();

        assertThatThrownBy(() -> regionServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verifyNoInteractions(paisRepository, regionRepository);
    }

    @Test
    @DisplayName("crear lanza excepción si el país no existe")
    void crear_paisNoExiste() {
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion("Valparaíso")
                .pais(PaisDTO.builder().idPais(1L).build())
                .build();

        when(paisRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> regionServiceImpl.crear(dto))
                .isInstanceOf(PaisNotFoundException.class);

        verify(regionRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si el nombre de la región ya existe")
    void crear_duplicado() {
        Pais chile = pais(1L, "Chile");
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion("Valparaíso")
                .pais(PaisDTO.builder().idPais(1L).build())
                .build();

        when(paisRepository.findById(1)).thenReturn(Optional.of(chile));
        when(regionRepository.findByNombreRegion("Valparaíso")).thenReturn(Optional.of(region(1L, "Valparaíso", chile)));

        assertThatThrownBy(() -> regionServiceImpl.crear(dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(regionRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica nombre y país cuando la región existe")
    void actualizar_ok() {
        Pais chile = pais(1L, "Chile");
        Pais argentina = pais(2L, "Argentina");
        Region existente = region(1L, "Valparaíso", chile);
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion(" Mendoza ")
                .pais(PaisDTO.builder().idPais(2L).build())
                .build();
        RegionDTO esperado = RegionDTO.builder().regionId(1L).nombreRegion("Mendoza").build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(paisRepository.findById(2)).thenReturn(Optional.of(argentina));
        when(regionRepository.findByNombreRegion(" Mendoza ")).thenReturn(Optional.empty());
        when(regionRepository.save(existente)).thenReturn(existente);
        when(regionMapper.toDTO(existente)).thenReturn(esperado);

        RegionDTO resultado = regionServiceImpl.actualizar(1L, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreRegion()).isEqualTo("Mendoza");
        assertThat(existente.getPais()).isEqualTo(argentina);
    }

    @Test
    @DisplayName("actualizar lanza excepción si el nombre ya existe en otra región")
    void actualizar_duplicado() {
        Pais chile = pais(1L, "Chile");
        Region existente = region(1L, "Valparaíso", chile);
        Region otra = region(2L, "Mendoza", chile);
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion("Mendoza")
                .pais(PaisDTO.builder().idPais(1L).build())
                .build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(paisRepository.findById(1)).thenReturn(Optional.of(chile));
        when(regionRepository.findByNombreRegion("Mendoza")).thenReturn(Optional.of(otra));

        assertThatThrownBy(() -> regionServiceImpl.actualizar(1L, dto))
                .isInstanceOf(BusinessRuleException.class);

        verify(regionRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si la región no existe")
    void actualizar_noExiste() {
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion("Mendoza")
                .pais(PaisDTO.builder().idPais(1L).build())
                .build();

        when(regionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> regionServiceImpl.actualizar(1L, dto))
                .isInstanceOf(RegionNotFoundException.class);

        verify(regionRepository, never()).save(any());
        verifyNoInteractions(paisRepository);
    }

    @Test
    @DisplayName("actualizar lanza excepción si el país no existe")
    void actualizar_paisNoExiste() {
        Pais chile = pais(1L, "Chile");
        Region existente = region(1L, "Valparaíso", chile);
        RegionDTO dto = RegionDTO.builder()
                .nombreRegion("Mendoza")
                .pais(PaisDTO.builder().idPais(99L).build())
                .build();

        when(regionRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(paisRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> regionServiceImpl.actualizar(1L, dto))
                .isInstanceOf(PaisNotFoundException.class);

        verify(regionRepository, never()).save(any());
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la región existe")
    void eliminar_ok() {
        when(regionRepository.existsById(1L)).thenReturn(true);

        regionServiceImpl.eliminar(1L);

        verify(regionRepository).deleteById(1L);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la región no existe")
    void eliminar_noExiste() {
        when(regionRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> regionServiceImpl.eliminar(1L))
                .isInstanceOf(RegionNotFoundException.class);

        verify(regionRepository, never()).deleteById(any());
    }
}
