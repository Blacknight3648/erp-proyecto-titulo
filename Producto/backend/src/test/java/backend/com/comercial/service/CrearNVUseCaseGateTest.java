package backend.com.comercial.service;

import backend.com.comercial.application.UseCase.CrearNVUseCase;
import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.application.UseCase.CrearOrdenProduccionUseCase;
import backend.com.shared.application.service.NumeroDocumentoService;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica el gate de consistencia: una Nota de Venta generada desde una EVN
 * solo se permite si la EVN está ADJUDICADA. Una EVN CERRADA (u otro estado)
 * bloquea la creación. Una NV manual (sin EVN) se permite.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CrearNVUseCase · gate de EVN ADJUDICADA")
class CrearNVUseCaseGateTest {

    @Mock private NotaVentaRepository nvRepository;
    @Mock private CrearOrdenProduccionUseCase crearOPUseCase;
    @Mock private NumeroDocumentoService numeroDocumentoService;
    @Mock private EvaluacionNegocioRepository evnRepository;

    @InjectMocks private CrearNVUseCase useCase;

    private CrearNVCommand commandConEVN(Long evnId) {
        CrearNVCommand cmd = new CrearNVCommand();
        cmd.setEvaluacionNegocioId(evnId);
        cmd.setClienteId(1L);
        cmd.setVendedorId(1L);
        cmd.setEsKit(false);
        cmd.setFechaEntregaEstimada(LocalDate.now().plusDays(10));
        return cmd; // sin items: no dispara generación de OP
    }

    private EvaluacionNegocio evnEnEstado(Long id, EstadoEVN estado) {
        return new EvaluacionNegocio(
                id, new DocumentNumber("EVN-000002"), 1L, 1L,
                estado, LocalDate.now(),
                null, List.of(), List.of(), BigDecimal.ZERO,
                "Cliente Test", "Ref", "Vendedor Test");
    }

    @Test
    @DisplayName("rechaza crear NV si la EVN está APROBADA (no ADJUDICADA)")
    void rechazaEvnNoAdjudicada() {
        when(evnRepository.findById(2L)).thenReturn(Optional.of(evnEnEstado(2L, EstadoEVN.APROBADA)));

        assertThatThrownBy(() -> useCase.ejecutar(commandConEVN(2L)))
                .isInstanceOf(EVNBusinessException.class);

        verify(nvRepository, never()).save(any());
    }

    @Test
    @DisplayName("rechaza crear NV si la EVN está CERRADA")
    void rechazaEvnCerrada() {
        when(evnRepository.findById(2L)).thenReturn(Optional.of(evnEnEstado(2L, EstadoEVN.CERRADA)));

        assertThatThrownBy(() -> useCase.ejecutar(commandConEVN(2L)))
                .isInstanceOf(EVNBusinessException.class);

        verify(nvRepository, never()).save(any());
    }

    @Test
    @DisplayName("permite crear NV si la EVN está ADJUDICADA")
    void permiteEvnAdjudicada() {
        when(evnRepository.findById(2L)).thenReturn(Optional.of(evnEnEstado(2L, EstadoEVN.ADJUDICADA)));
        when(numeroDocumentoService.siguienteFormateado("NV")).thenReturn(new DocumentNumber("NV-00010"));
        when(nvRepository.save(any(NotaVenta.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.ejecutar(commandConEVN(2L));

        verify(nvRepository).save(any(NotaVenta.class));
    }
}
