package backend.com.produccion.service.Impl;

import backend.com.produccion.application.UseCase.RegistrarRecepcionOCUseCase;
import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.dto.RecepcionOCItemDTO;
import backend.com.produccion.application.service.impl.RecepcionOCServiceImpl;
import backend.com.produccion.domain.model.RecepcionOC;
import backend.com.produccion.domain.model.RecepcionOCItem;
import backend.com.produccion.domain.repository.RecepcionOCRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RecepcionOCServiceImpl")
class RecepcionOCServiceImplTest {

    @Mock
    private RecepcionOCRepository recepcionOCRepository;

    @Mock
    private RegistrarRecepcionOCUseCase registrarRecepcionOCUseCase;

    @InjectMocks
    private RecepcionOCServiceImpl recepcionOCService;

    // --- Métodos Helper ---

    private RecepcionOC recepcion(Long id, Long ocId, String numeroGuia, List<RecepcionOCItem> items) {
        return new RecepcionOC(id, ocId, LocalDate.now(), numeroGuia, "Responsable Test", "obs", items);
    }

    private RecepcionOCItem item(Long idItem, Long recepcionId, Long ocItemId, BigDecimal cantidadRecibida,
            BigDecimal cantidadConforme, BigDecimal cantidadRechazada) {
        return new RecepcionOCItem(idItem, recepcionId, ocItemId, cantidadRecibida, cantidadConforme,
                cantidadRechazada, null);
    }

    // --- registrar ---

    @Test
    @DisplayName("registrar delega en RegistrarRecepcionOCUseCase y mapea el DTO resultante")
    void registrar_delegaEnUseCaseYMapeaDTO() {
        RecepcionOCItemDTO itemDTO = RecepcionOCItemDTO.builder()
                .ocItemId(100L)
                .cantidadRecibida(new BigDecimal("10"))
                .cantidadConforme(new BigDecimal("9"))
                .cantidadRechazada(new BigDecimal("1"))
                .build();
        RecepcionOCDTO request = RecepcionOCDTO.builder()
                .ocId(10L)
                .numeroGuia("GUIA-001")
                .responsable("Pedro")
                .items(List.of(itemDTO))
                .build();

        RecepcionOC recepcionMock = recepcion(1L, 10L, "GUIA-001",
                List.of(item(50L, 1L, 100L, new BigDecimal("10"), new BigDecimal("9"), new BigDecimal("1"))));

        when(registrarRecepcionOCUseCase.ejecutar(10L, request)).thenReturn(recepcionMock);

        RecepcionOCDTO resultado = recepcionOCService.registrar(10L, request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdRecepcion()).isEqualTo(1L);
        assertThat(resultado.getOcId()).isEqualTo(10L);
        assertThat(resultado.getNumeroGuia()).isEqualTo("GUIA-001");
        assertThat(resultado.getItems()).hasSize(1);
        assertThat(resultado.getItems().get(0).getOcItemId()).isEqualTo(100L);
        assertThat(resultado.getItems().get(0).getCantidadRecibida()).isEqualByComparingTo("10");
        verify(registrarRecepcionOCUseCase).ejecutar(10L, request);
    }

    // --- obtenerPorId ---

    @Test
    @DisplayName("obtenerPorId retorna DTO si existe")
    void obtenerPorId_existente_retornaDTOOptional() {
        RecepcionOC recepcionMock = recepcion(1L, 10L, "GUIA-001", List.of());
        when(recepcionOCRepository.findById(1L)).thenReturn(Optional.of(recepcionMock));

        Optional<RecepcionOCDTO> resultado = recepcionOCService.obtenerPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getIdRecepcion()).isEqualTo(1L);
    }

    @Test
    @DisplayName("obtenerPorId retorna Optional vacio si no existe")
    void obtenerPorId_noExistente_retornaOptionalEmpty() {
        when(recepcionOCRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<RecepcionOCDTO> resultado = recepcionOCService.obtenerPorId(1L);

        assertThat(resultado).isEmpty();
    }

    // --- listarPorOC ---

    @Test
    @DisplayName("listarPorOC retorna el mapeo de las recepciones de una OC")
    void listarPorOC_retornaListaDeDTOs() {
        when(recepcionOCRepository.findAllByOcId(10L)).thenReturn(List.of(
                recepcion(1L, 10L, "GUIA-001", List.of()),
                recepcion(2L, 10L, "GUIA-002", List.of())));

        List<RecepcionOCDTO> resultado = recepcionOCService.listarPorOC(10L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNumeroGuia()).isEqualTo("GUIA-001");
        assertThat(resultado.get(1).getNumeroGuia()).isEqualTo("GUIA-002");
        verify(recepcionOCRepository).findAllByOcId(10L);
    }
}
