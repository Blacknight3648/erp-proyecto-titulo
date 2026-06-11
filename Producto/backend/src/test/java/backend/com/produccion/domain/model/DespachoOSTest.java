package backend.com.produccion.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitarios PUROS del modelo de dominio para DespachoOS.
 */
@DisplayName("DespachoOS (dominio)")
class DespachoOSTest {

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("asigna todos los valores recibidos correctamente")
        void asignaValoresCorrectamente() {
            LocalDate fecha = LocalDate.of(2025, 1, 15);

            DespachoOS despacho = new DespachoOS(1L, 10L, fecha, 50,
                    "Despacho parcial de bordado", "Juan Perez", "Sin novedades");

            assertThat(despacho.getIdDespacho()).isEqualTo(1L);
            assertThat(despacho.getOsId()).isEqualTo(10L);
            assertThat(despacho.getFechaDespacho()).isEqualTo(fecha);
            assertThat(despacho.getCantidadDespachada()).isEqualTo(50);
            assertThat(despacho.getDescripcion()).isEqualTo("Despacho parcial de bordado");
            assertThat(despacho.getResponsable()).isEqualTo("Juan Perez");
            assertThat(despacho.getObservaciones()).isEqualTo("Sin novedades");
        }

        @Test
        @DisplayName("asigna la fecha de hoy si fechaDespacho es nula")
        void asignaFechaActualSiEsNula() {
            DespachoOS despacho = new DespachoOS(1L, 10L, null, 50, "Despacho", "Juan", null);

            assertThat(despacho.getFechaDespacho()).isEqualTo(LocalDate.now());
        }

        @Test
        @DisplayName("asigna 0 a cantidadDespachada si es nula")
        void asignaCeroSiCantidadDespachadaEsNula() {
            DespachoOS despacho = new DespachoOS(1L, 10L, LocalDate.now(), null, "Despacho", "Juan", null);

            assertThat(despacho.getCantidadDespachada()).isZero();
        }
    }
}
