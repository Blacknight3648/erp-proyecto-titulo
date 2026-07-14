package backend.com.shared.service;

import backend.com.shared.application.dto.FamiliaTelaDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.impl.FamiliaTelaServiceImpl;
import backend.com.shared.domain.model.FamiliaTela;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.FamiliaTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.FamiliaTelaRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("FamiliaTelaServiceImpl")
class FamiliaTelaServiceImplTest {

    @Mock
    private FamiliaTelaRepository familiaTelaRepository;

    @Mock
    private FamiliaTelaMapper mapper;

    @Mock
    private CodigoGeneratorService codigoGeneratorService;

    @InjectMocks
    private FamiliaTelaServiceImpl familiaTelaServiceImpl;

    // ---------------- HELPERS ----------------

    private FamiliaTela familiaTela(Integer id, String codigo, String nombre) {
        return FamiliaTela.builder()
                .idFamiliaTela(id)
                .codigoFamilia(codigo)
                .nombreFamilia(nombre)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        FamiliaTelaDTO dto = FamiliaTelaDTO.builder()
                .codigoFamilia("ALG")
                .nombreFamilia("Algodón")
                .build();

        FamiliaTela guardada = familiaTela(1, "ALG", "Algodón");
        FamiliaTelaDTO esperado = FamiliaTelaDTO.builder()
                .idFamiliaTela(1)
                .codigoFamilia("ALG")
                .nombreFamilia("Algodón")
                .build();

        when(codigoGeneratorService.generarPorAbreviatura(eq("T-"), eq("Algodón"), any())).thenReturn("ALG");
        when(familiaTelaRepository.save(any(FamiliaTela.class))).thenReturn(guardada);
        when(mapper.toDTO(guardada)).thenReturn(esperado);

        FamiliaTelaDTO resultado = familiaTelaServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(familiaTelaRepository).save(argThat(f -> "ALG".equals(f.getCodigoFamilia())));
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica código y nombre cuando la familia existe")
    void actualizar_ok() {
        FamiliaTela existente = familiaTela(1, "ALG", "Algodón");
        FamiliaTelaDTO dto = FamiliaTelaDTO.builder()
                .codigoFamilia("POL")
                .nombreFamilia("Poliéster")
                .build();
        FamiliaTelaDTO esperado = FamiliaTelaDTO.builder()
                .idFamiliaTela(1)
                .codigoFamilia("POL")
                .nombreFamilia("Poliéster")
                .build();

        when(familiaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(familiaTelaRepository.save(existente)).thenReturn(existente);
        when(mapper.toDTO(existente)).thenReturn(esperado);

        FamiliaTelaDTO resultado = familiaTelaServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getCodigoFamilia()).isEqualTo("POL");
        assertThat(existente.getNombreFamilia()).isEqualTo("Poliéster");
    }

    @Test
    @DisplayName("actualizar lanza excepción si la familia no existe")
    void actualizar_noExiste() {
        FamiliaTelaDTO dto = FamiliaTelaDTO.builder()
                .codigoFamilia("POL")
                .nombreFamilia("Poliéster")
                .build();

        when(familiaTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> familiaTelaServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(familiaTelaRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando la familia existe")
    void obtenerPorId_ok() {
        FamiliaTela existente = familiaTela(1, "ALG", "Algodón");
        FamiliaTelaDTO esperado = FamiliaTelaDTO.builder()
                .idFamiliaTela(1)
                .codigoFamilia("ALG")
                .nombreFamilia("Algodón")
                .build();

        when(familiaTelaRepository.findById(1)).thenReturn(Optional.of(existente));
        when(mapper.toDTO(existente)).thenReturn(esperado);

        FamiliaTelaDTO resultado = familiaTelaServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si la familia no existe")
    void obtenerPorId_noExiste() {
        when(familiaTelaRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> familiaTelaServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodas ----------------

    @Test
    @DisplayName("listarTodas mapea correctamente la lista completa")
    void listarTodas_ok() {
        FamiliaTela f1 = familiaTela(1, "ALG", "Algodón");
        FamiliaTela f2 = familiaTela(2, "POL", "Poliéster");
        FamiliaTelaDTO dto1 = FamiliaTelaDTO.builder().idFamiliaTela(1).codigoFamilia("ALG").nombreFamilia("Algodón").build();
        FamiliaTelaDTO dto2 = FamiliaTelaDTO.builder().idFamiliaTela(2).codigoFamilia("POL").nombreFamilia("Poliéster").build();

        when(familiaTelaRepository.findAll()).thenReturn(List.of(f1, f2));
        when(mapper.toDTO(f1)).thenReturn(dto1);
        when(mapper.toDTO(f2)).thenReturn(dto2);

        List<FamiliaTelaDTO> resultado = familiaTelaServiceImpl.listarTodas();

        assertThat(resultado).containsExactly(dto1, dto2);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando la familia existe")
    void eliminar_ok() {
        when(familiaTelaRepository.existsById(1)).thenReturn(true);

        familiaTelaServiceImpl.eliminar(1);

        verify(familiaTelaRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si la familia no existe")
    void eliminar_noExiste() {
        when(familiaTelaRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> familiaTelaServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(familiaTelaRepository, never()).deleteById(any());
    }
}
