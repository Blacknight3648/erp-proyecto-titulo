package backend.com.produccion.application.service;

import backend.com.produccion.application.UseCase.CalcularAvanceUseCase;
import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.domain.enums.EstadoOT;
import backend.com.produccion.domain.model.FaseProduccion;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.enums.TipoOT;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests del UseCase con dependencias mockeadas (Mockito).
 * Cubre UT-PRD-005: cálculo del porcentaje global de avance de una OP,
 * considerando mermas reportadas y los distintos umbrales del semáforo.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CalcularAvanceUseCase")
class CalcularAvanceUseCaseTest {

    @Mock
    private OrdenTrabajoRepository otRepository;

    @InjectMocks
    private CalcularAvanceUseCase useCase;

    private OrdenTrabajo ot(long id, String numero, int total, int producida, int merma,
            FaseProduccion fase, EstadoOT estado) {
        return new OrdenTrabajo(id, new DocumentNumber(numero), 1L, 1L, 10L, 1, TipoOT.INTERNA,
                fase, estado, total, producida, merma, null);
    }

    @Test
    @DisplayName("sin OTs asociadas retorna ceros y semáforo ROJO")
    void calcular_sinOTs_retornaCerosYSemaforoRojo() {
        when(otRepository.findByOrdenProduccionId(10L)).thenReturn(List.of());

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getOrdenProduccionId()).isEqualTo(10L);
        assertThat(resp.getTotalUnidades()).isZero();
        assertThat(resp.getTotalProducidas()).isZero();
        assertThat(resp.getTotalMermas()).isZero();
        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resp.getSemaforo()).isEqualTo("ROJO");
        assertThat(resp.getOrdenesTrabajo()).isEmpty();
        verify(otRepository).findByOrdenProduccionId(10L);
    }

    @Test
    @DisplayName("avance completo (100%) retorna semáforo VERDE")
    void calcular_conAvanceCompleto_retornaSemaforoVerde() {
        when(otRepository.findByOrdenProduccionId(10L))
                .thenReturn(List.of(ot(1L, "OT-001", 100, 100, 0, FaseProduccion.CORTE, EstadoOT.FINALIZADA)));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(resp.getSemaforo()).isEqualTo("VERDE");
    }

    @Test
    @DisplayName("avance parcial >= 60% retorna semáforo AMARILLO")
    void calcular_conAvanceParcialAlto_retornaSemaforoAmarillo() {
        when(otRepository.findByOrdenProduccionId(10L))
                .thenReturn(List.of(ot(1L, "OT-001", 100, 70, 0, FaseProduccion.CORTE, EstadoOT.EN_PROCESO)));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(new BigDecimal("70.00"));
        assertThat(resp.getSemaforo()).isEqualTo("AMARILLO");
    }

    @Test
    @DisplayName("avance parcial entre 0% y 60% retorna semáforo NARANJA")
    void calcular_conAvanceParcialBajo_retornaSemaforoNaranja() {
        when(otRepository.findByOrdenProduccionId(10L))
                .thenReturn(List.of(ot(1L, "OT-001", 100, 30, 0, FaseProduccion.CORTE, EstadoOT.EN_PROCESO)));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(new BigDecimal("30.00"));
        assertThat(resp.getSemaforo()).isEqualTo("NARANJA");
    }

    @Test
    @DisplayName("sin unidades producidas ni merma retorna semáforo ROJO")
    void calcular_sinAvance_retornaSemaforoRojo() {
        when(otRepository.findByOrdenProduccionId(10L))
                .thenReturn(List.of(ot(1L, "OT-001", 100, 0, 0, FaseProduccion.CORTE, EstadoOT.PENDIENTE)));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resp.getSemaforo()).isEqualTo("ROJO");
    }

    @Test
    @DisplayName("considera las mermas reportadas dentro del porcentaje global")
    void calcular_consideraMermasEnElPorcentaje() {
        // (50 producidas + 10 merma) / 100 total => 60%
        when(otRepository.findByOrdenProduccionId(10L))
                .thenReturn(List.of(ot(1L, "OT-001", 100, 50, 10, FaseProduccion.CONFECCION, EstadoOT.EN_PROCESO)));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getTotalMermas()).isEqualTo(10);
        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(new BigDecimal("60.00"));
        assertThat(resp.getSemaforo()).isEqualTo("AMARILLO");
    }

    @Test
    @DisplayName("suma los totales de múltiples OTs pertenecientes a la misma OP")
    void calcular_conMultiplesOTs_sumaTotalesCorrectamente() {
        when(otRepository.findByOrdenProduccionId(10L)).thenReturn(List.of(
                ot(1L, "OT-001", 100, 50, 0, FaseProduccion.CORTE, EstadoOT.EN_PROCESO),
                ot(2L, "OT-002", 200, 100, 50, FaseProduccion.CONFECCION, EstadoOT.EN_PROCESO)));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getTotalUnidades()).isEqualTo(300);
        assertThat(resp.getTotalProducidas()).isEqualTo(150);
        assertThat(resp.getTotalMermas()).isEqualTo(50);
        // (150 + 50) / 300 * 100 = 66.67
        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(new BigDecimal("66.67"));
        assertThat(resp.getSemaforo()).isEqualTo("AMARILLO");
        assertThat(resp.getOrdenesTrabajo()).hasSize(2);
    }

    @Test
    @DisplayName("mapea correctamente los campos de cada OT en la respuesta")
    void calcular_mapeaCamposDeCadaOT() {
        when(otRepository.findByOrdenProduccionId(10L))
                .thenReturn(List.of(ot(5L, "OT-005", 80, 40, 0, FaseProduccion.BORDADO, EstadoOT.EN_PROCESO)));

        AvanceOPResponse resp = useCase.calcular(10L);

        AvanceOPResponse.AvanceOT avanceOT = resp.getOrdenesTrabajo().get(0);
        assertThat(avanceOT.getIdOT()).isEqualTo(5L);
        assertThat(avanceOT.getNumeroOT()).isEqualTo("OT-005");
        assertThat(avanceOT.getFase()).isEqualTo("BORDADO");
        assertThat(avanceOT.getEstado()).isEqualTo("EN_PROCESO");
        assertThat(avanceOT.getCantidadTotal()).isEqualTo(80);
        assertThat(avanceOT.getCantidadProducida()).isEqualTo(40);
        assertThat(avanceOT.getCantidadMerma()).isEqualTo(0);
        assertThat(avanceOT.getPorcentajeAvance()).isEqualByComparingTo(new BigDecimal("50.00"));
    }
}
