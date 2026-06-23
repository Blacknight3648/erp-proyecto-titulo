package backend.com.shared.service;

import backend.com.shared.application.dto.RubroDTO;
import backend.com.shared.application.service.impl.RubroServiceImpl;
import backend.com.shared.domain.model.Rubro;
import backend.com.shared.infrastructure.mapper.RubroMapper;
import backend.com.shared.infrastructure.persistence.repository.RubroRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RubroServiceImpl")
class RubroServiceImplTest {

    @Mock
    private RubroRepository rubroRepository;

    @Mock
    private RubroMapper rubroMapper;

    @InjectMocks
    private RubroServiceImpl rubroServiceImpl;

    // ---------------- HELPERS ----------------

    private Rubro rubro(Long id, String nombre, String descripcion) {
        return Rubro.builder()
                .rubroId(id)
                .nombreRubro(nombre)
                .descripcionRubro(descripcion)
                .build();
    }

    // ---------------- getAllRubros ----------------

    @Test
    @DisplayName("getAllRubros mapea correctamente la lista completa")
    void getAllRubros_ok() {
        Rubro r1 = rubro(1L, "Textil", "Rubro textil");
        Rubro r2 = rubro(2L, "Calzado", "Rubro calzado");
        RubroDTO dto1 = RubroDTO.builder().rubroId(1L).nombreRubro("Textil").descripcionRubro("Rubro textil").build();
        RubroDTO dto2 = RubroDTO.builder().rubroId(2L).nombreRubro("Calzado").descripcionRubro("Rubro calzado").build();

        when(rubroRepository.findAll()).thenReturn(List.of(r1, r2));
        when(rubroMapper.toDTO(r1)).thenReturn(dto1);
        when(rubroMapper.toDTO(r2)).thenReturn(dto2);

        List<RubroDTO> resultado = rubroServiceImpl.getAllRubros();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- getRubroById ----------------

    @Test
    @DisplayName("getRubroById retorna el DTO cuando el rubro existe")
    void getRubroById_ok() {
        Rubro existente = rubro(1L, "Textil", "Rubro textil");
        RubroDTO esperado = RubroDTO.builder().rubroId(1L).nombreRubro("Textil").descripcionRubro("Rubro textil").build();

        when(rubroRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rubroMapper.toDTO(existente)).thenReturn(esperado);

        Optional<RubroDTO> resultado = rubroServiceImpl.getRubroById(1L);

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("getRubroById retorna vacío si el rubro no existe")
    void getRubroById_noExiste() {
        when(rubroRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RubroDTO> resultado = rubroServiceImpl.getRubroById(1L);

        assertThat(resultado).isEmpty();
    }

    // ---------------- createRubro ----------------

    @Test
    @DisplayName("createRubro mapea, guarda y retorna el DTO resultante")
    void createRubro_ok() {
        RubroDTO dto = RubroDTO.builder().nombreRubro("Textil").descripcionRubro("Rubro textil").build();
        Rubro nuevo = rubro(null, "Textil", "Rubro textil");
        Rubro guardado = rubro(1L, "Textil", "Rubro textil");
        RubroDTO esperado = RubroDTO.builder().rubroId(1L).nombreRubro("Textil").descripcionRubro("Rubro textil").build();

        when(rubroMapper.toDomain(dto)).thenReturn(nuevo);
        when(rubroRepository.save(nuevo)).thenReturn(guardado);
        when(rubroMapper.toDTO(guardado)).thenReturn(esperado);

        RubroDTO resultado = rubroServiceImpl.createRubro(dto);

        assertThat(resultado).isEqualTo(esperado);
    }

    // ---------------- updateRubro ----------------

    @Test
    @DisplayName("updateRubro modifica nombre y descripción cuando el rubro existe")
    void updateRubro_ok() {
        Rubro existente = rubro(1L, "Textil", "Rubro textil");
        RubroDTO dto = RubroDTO.builder().nombreRubro("Textil Premium").descripcionRubro("Rubro textil premium").build();
        RubroDTO esperado = RubroDTO.builder().rubroId(1L).nombreRubro("Textil Premium").descripcionRubro("Rubro textil premium").build();

        when(rubroRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(rubroRepository.save(existente)).thenReturn(existente);
        when(rubroMapper.toDTO(existente)).thenReturn(esperado);

        Optional<RubroDTO> resultado = rubroServiceImpl.updateRubro(1L, dto);

        assertThat(resultado).contains(esperado);
        assertThat(existente.getNombreRubro()).isEqualTo("Textil Premium");
        assertThat(existente.getDescripcionRubro()).isEqualTo("Rubro textil premium");
    }

    @Test
    @DisplayName("updateRubro retorna vacío si el rubro no existe")
    void updateRubro_noExiste() {
        RubroDTO dto = RubroDTO.builder().nombreRubro("Textil Premium").descripcionRubro("Rubro textil premium").build();

        when(rubroRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RubroDTO> resultado = rubroServiceImpl.updateRubro(1L, dto);

        assertThat(resultado).isEmpty();
        verify(rubroRepository, never()).save(any());
    }

    // ---------------- getRubroByNombreRubro ----------------

    @Test
    @DisplayName("getRubroByNombreRubro retorna el DTO cuando el rubro existe")
    void getRubroByNombreRubro_ok() {
        Rubro existente = rubro(1L, "Textil", "Rubro textil");
        RubroDTO esperado = RubroDTO.builder().rubroId(1L).nombreRubro("Textil").descripcionRubro("Rubro textil").build();

        when(rubroRepository.findByNombreRubro("Textil")).thenReturn(Optional.of(existente));
        when(rubroMapper.toDTO(existente)).thenReturn(esperado);

        Optional<RubroDTO> resultado = rubroServiceImpl.getRubroByNombreRubro("Textil");

        assertThat(resultado).contains(esperado);
    }

    @Test
    @DisplayName("getRubroByNombreRubro retorna vacío si no existe un rubro con ese nombre")
    void getRubroByNombreRubro_noExiste() {
        when(rubroRepository.findByNombreRubro("Inexistente")).thenReturn(Optional.empty());

        Optional<RubroDTO> resultado = rubroServiceImpl.getRubroByNombreRubro("Inexistente");

        assertThat(resultado).isEmpty();
    }

    // ---------------- deleteRubro ----------------

    @Test
    @DisplayName("deleteRubro invoca la eliminación por id")
    void deleteRubro_ok() {
        rubroServiceImpl.deleteRubro(1L);

        verify(rubroRepository).deleteById(1L);
    }

    // ---------------- searchRubros ----------------

    @Test
    @DisplayName("searchRubros retorna los rubros ordenados por nombre")
    void searchRubros_ok() {
        Rubro r1 = rubro(1L, "Calzado", "Rubro calzado");
        Rubro r2 = rubro(2L, "Textil", "Rubro textil");
        RubroDTO dto1 = RubroDTO.builder().rubroId(1L).nombreRubro("Calzado").descripcionRubro("Rubro calzado").build();
        RubroDTO dto2 = RubroDTO.builder().rubroId(2L).nombreRubro("Textil").descripcionRubro("Rubro textil").build();

        when(rubroRepository.findAllByOrderByNombreRubroAsc()).thenReturn(List.of(r1, r2));
        when(rubroMapper.toDTO(r1)).thenReturn(dto1);
        when(rubroMapper.toDTO(r2)).thenReturn(dto2);

        List<RubroDTO> resultado = rubroServiceImpl.searchRubros("tex", 10);

        assertThat(resultado).containsExactly(dto1, dto2);
    }
}
