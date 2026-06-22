package backend.com.comercial.domain.model;

import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EvaluacionNegocio · cierre")
class EvaluacionNegocioTest {

    private EvaluacionNegocio nuevaEVN() {
        return EvaluacionNegocio.crear(
                new DocumentNumber("EVN-000001"),
                1L, 1L, BigDecimal.ZERO,
                "Cliente Test", "Ref Test", "Vendedor Test");
    }

    @Test
    @DisplayName("cerrar() desde ADJUDICADA cambia el estado a CERRADA")
    void cerrarDesdeAdjudicada() {
        EvaluacionNegocio evn = nuevaEVN();
        evn.adjudicar(); // BORRADOR → ADJUDICADA

        evn.cerrar();

        assertThat(evn.getEstado()).isEqualTo(EstadoEVN.CERRADA);
    }

    @Test
    @DisplayName("cerrar() desde BORRADOR lanza EVNBusinessException")
    void cerrarDesdeBorradorFalla() {
        EvaluacionNegocio evn = nuevaEVN(); // estado BORRADOR

        assertThatThrownBy(evn::cerrar)
                .isInstanceOf(EVNBusinessException.class)
                .hasMessageContaining("ADJUDICADA");

        assertThat(evn.getEstado()).isEqualTo(EstadoEVN.BORRADOR);
    }

    @Test
    @DisplayName("cerrar() una EVN ya CERRADA vuelve a fallar (no es reversible)")
    void cerrarDosVecesFalla() {
        EvaluacionNegocio evn = nuevaEVN();
        evn.adjudicar();
        evn.cerrar();

        assertThatThrownBy(evn::cerrar)
                .isInstanceOf(EVNBusinessException.class);
    }
}
