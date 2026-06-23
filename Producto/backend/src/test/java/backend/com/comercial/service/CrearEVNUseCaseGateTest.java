package backend.com.comercial.service;

import backend.com.comercial.application.UseCase.CrearEVNUseCase;
import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.ItemEVNDTO;
import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.service.CosteoService;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Verifica el gate de aprobación: solo un Costeo APROBADO puede vincularse a un
 * ítem de la EVN.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CrearEVNUseCase · gate de costeo APROBADO")
class CrearEVNUseCaseGateTest {

    @Mock private EvaluacionNegocioRepository evnRepository;
    @Mock private NumeroDocumentoService numeroDocumentoService;
    @Mock private CosteoService costeoService;

    @InjectMocks private CrearEVNUseCase useCase;

    private CrearEVNCommand commandConCosteo(Long costeoId) {
        ItemEVNDTO item = new ItemEVNDTO();
        item.setNroItem(1);
        item.setTipoItem(TipoItem.OP);
        item.setCantidad(1);
        item.setCosteoId(costeoId);

        CrearEVNCommand cmd = new CrearEVNCommand();
        cmd.setClienteId(1L);
        cmd.setVendedorId(2L);
        cmd.setItems(List.of(item));
        return cmd;
    }

    @Test
    @DisplayName("rechaza vincular un costeo que no está APROBADO")
    void rechazaCosteoNoAprobado() {
        when(costeoService.findById(50L))
                .thenReturn(Optional.of(CosteoDTO.builder().idCosteo(50L).estado("COSTEADO").build()));

        assertThatThrownBy(() -> useCase.ejecutar(commandConCosteo(50L)))
                .isInstanceOf(BusinessRuleException.class);

        verify(evnRepository, never()).save(any());
    }

    @Test
    @DisplayName("permite vincular un costeo APROBADO")
    void permiteCosteoAprobado() {
        lenient().when(numeroDocumentoService.siguienteFormateado("EVN"))
                .thenReturn(new DocumentNumber("EVN-0000001"));
        when(costeoService.findById(50L))
                .thenReturn(Optional.of(CosteoDTO.builder().idCosteo(50L).estado("APROBADO").build()));
        when(evnRepository.save(any(EvaluacionNegocio.class))).thenAnswer(inv -> inv.getArgument(0));

        useCase.ejecutar(commandConCosteo(50L));

        verify(evnRepository).save(any(EvaluacionNegocio.class));
    }
}
