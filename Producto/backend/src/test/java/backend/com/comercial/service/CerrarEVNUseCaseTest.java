package backend.com.comercial.service;

import backend.com.comercial.application.UseCase.CerrarEVNUseCase;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.DocumentNumber;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CerrarEVNUseCase")
class CerrarEVNUseCaseTest {

    @Mock private EvaluacionNegocioRepository evnRepository;
    @Mock private HistorialEstadoService historialService;

    @InjectMocks private CerrarEVNUseCase useCase;

    private EvaluacionNegocio evnEnEstado(Long id, EstadoEVN estado) {
        return new EvaluacionNegocio(
                id, new DocumentNumber("EVN-000002"), 1L, 1L,
                estado, LocalDate.now(),
                null, List.of(), List.of(), BigDecimal.ZERO,
                "Cliente Test", "Ref", "Vendedor Test");
    }

    @Test
    @DisplayName("cierra una EVN ADJUDICADA, persiste y registra historial")
    void cierraAdjudicada() {
        EvaluacionNegocio evn = evnEnEstado(2L, EstadoEVN.ADJUDICADA);
        when(evnRepository.findById(2L)).thenReturn(Optional.of(evn));
        when(evnRepository.save(any(EvaluacionNegocio.class))).thenAnswer(inv -> inv.getArgument(0));

        EVNResponse response = useCase.ejecutar(2L, "tester", "cierre de prueba");

        assertThat(response.getEstado()).isEqualTo("CERRADA");
        verify(evnRepository).save(any(EvaluacionNegocio.class));
        verify(historialService).registrar(eq("EVN"), eq(2L), eq("ADJUDICADA"), eq("CERRADA"),
                eq("tester"), any());
    }

    @Test
    @DisplayName("rechaza cerrar sin aprobador y no persiste")
    void rechazaSinAprobador() {
        assertThatThrownBy(() -> useCase.ejecutar(2L, "  ", "x"))
                .isInstanceOf(EVNBusinessException.class);

        verify(evnRepository, never()).save(any());
    }

    @Test
    @DisplayName("rechaza cerrar una EVN que no está ADJUDICADA")
    void rechazaEstadoInvalido() {
        when(evnRepository.findById(2L)).thenReturn(Optional.of(evnEnEstado(2L, EstadoEVN.APROBADA)));

        assertThatThrownBy(() -> useCase.ejecutar(2L, "tester", "x"))
                .isInstanceOf(EVNBusinessException.class);

        verify(evnRepository, never()).save(any());
    }
}
