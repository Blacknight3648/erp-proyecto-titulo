package backend.com.produccion.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitarios PUROS del modelo de dominio.
 * No requieren Spring ni base de datos: ejecutan en milisegundos.
 *
 * Sirven como "documentación viva" de las reglas de negocio de OrdenTrabajo:
 * cualquier nuevo desarrollador puede leer los nombres de los tests y entender
 * las invariantes sin abrir el código de la entidad.
 */
@DisplayName("OrdenTrabajo (dominio)")
class OrdenTrabajoTest {

    private OrdenTrabajo nuevaOT(int cantidadTotal) {
        return OrdenTrabajo.crearParaFase(
                new DocumentNumber(1L),
                1L,                 // notaVentaId
                1L,                 // ordenProduccionId
                1,                  // nroItem
                FaseProduccion.CORTE,
                cantidadTotal,
                null);
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("una OT recién creada está PENDIENTE y con 0 unidades procesadas")
        void otRecienCreadaEstaPendiente() {
            OrdenTrabajo ot = nuevaOT(100);

            assertThat(ot.getEstadoOT()).isEqualTo(EstadoOT.PENDIENTE);
            assertThat(ot.getCantidadProducida()).isZero();
            assertThat(ot.getCantidadMerma()).isZero();
            assertThat(ot.getPorcentajeAvance()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }

    @Nested
    @DisplayName("Registrar avance")
    class RegistrarAvance {

        @Test
        @DisplayName("acumula producción y merma sobre los totales existentes")
        void acumulaCantidades() {
            OrdenTrabajo ot = nuevaOT(100);

            ot.registrarAvance(30, 2);
            ot.registrarAvance(20, 1);

            assertThat(ot.getCantidadProducida()).isEqualTo(50);
            assertThat(ot.getCantidadMerma()).isEqualTo(3);
        }

        @Test
        @DisplayName("transiciona automáticamente PENDIENTE → EN_PROCESO al primer avance")
        void transicionaAEnProceso() {
            OrdenTrabajo ot = nuevaOT(100);

            ot.registrarAvance(10, 0);

            assertThat(ot.getEstadoOT()).isEqualTo(EstadoOT.EN_PROCESO);
        }

        @Test
        @DisplayName("transiciona a FINALIZADA cuando producida+merma alcanza el total")
        void transicionaAFinalizada() {
            OrdenTrabajo ot = nuevaOT(100);

            ot.registrarAvance(98, 2);

            assertThat(ot.getEstadoOT()).isEqualTo(EstadoOT.FINALIZADA);
        }

        @Test
        @DisplayName("rechaza cantidades negativas")
        void rechazaCantidadesNegativas() {
            OrdenTrabajo ot = nuevaOT(100);

            assertThatThrownBy(() -> ot.registrarAvance(-5, 0))
                    .isInstanceOf(IllegalArgumentException.class);
            assertThatThrownBy(() -> ot.registrarAvance(0, -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("no permite registrar avance en una OT ya finalizada")
        void rechazaAvanceEnOtFinalizada() {
            OrdenTrabajo ot = nuevaOT(100);
            ot.registrarAvance(100, 0);
            assertThat(ot.getEstadoOT()).isEqualTo(EstadoOT.FINALIZADA);

            assertThatThrownBy(() -> ot.registrarAvance(1, 0))
                    .isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested
    @DisplayName("Porcentaje de avance")
    class PorcentajeAvance {

        @Test
        @DisplayName("calcula (producida + merma) / total * 100")
        void calculaPorcentaje() {
            OrdenTrabajo ot = nuevaOT(200);
            ot.registrarAvance(48, 2); // 50 / 200 = 25%

            assertThat(ot.getPorcentajeAvance()).isEqualByComparingTo(new BigDecimal("25.00"));
        }

        @Test
        @DisplayName("retorna 0 si la cantidad total es 0")
        void retornaCeroSiTotalEsCero() {
            OrdenTrabajo ot = nuevaOT(0);

            assertThat(ot.getPorcentajeAvance()).isEqualByComparingTo(BigDecimal.ZERO);
        }
    }
}
