package backend.com.produccion.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

/**
 * Tests unitarios PUROS del modelo de dominio para RegistroAvance.
 */
@DisplayName("RegistroAvance (dominio)")
class RegistroAvanceTest {

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("asigna todos los valores recibidos correctamente")
        void asignaValoresCorrectamente() {
            LocalDateTime fecha = LocalDateTime.of(2025, 1, 15, 10, 30);

            RegistroAvance registro = new RegistroAvance(1L, 10L, 50, 5,
                    "Material defectuoso", "Juan Perez", "Sin novedades", fecha);

            assertThat(registro.getId()).isEqualTo(1L);
            assertThat(registro.getOrdenTrabajoId()).isEqualTo(10L);
            assertThat(registro.getCantidadProducida()).isEqualTo(50);
            assertThat(registro.getCantidadMerma()).isEqualTo(5);
            assertThat(registro.getMotivoMerma()).isEqualTo("Material defectuoso");
            assertThat(registro.getUsuario()).isEqualTo("Juan Perez");
            assertThat(registro.getObservacion()).isEqualTo("Sin novedades");
            assertThat(registro.getFecha()).isEqualTo(fecha);
        }

        @Test
        @DisplayName("asigna 0 a cantidadProducida y cantidadMerma si son nulas")
        void asignaCerosSiCantidadesSonNulas() {
            RegistroAvance registro = new RegistroAvance(1L, 10L, null, null, null, "Juan", null, null);

            assertThat(registro.getCantidadProducida()).isZero();
            assertThat(registro.getCantidadMerma()).isZero();
        }

        @Test
        @DisplayName("asigna la fecha/hora actual si fecha es nula")
        void asignaFechaActualSiEsNula() {
            RegistroAvance registro = new RegistroAvance(1L, 10L, 10, 0, null, "Juan", null, null);

            assertThat(registro.getFecha()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
        }
    }

    @Nested
    @DisplayName("Factory: crear")
    class Crear {

        @Test
        @DisplayName("crea un registro sin id, con la fecha/hora actual y los valores indicados")
        void creaRegistroCorrectamente() {
            RegistroAvance registro = RegistroAvance.crear(10L, 30, 2, "Falla de maquina", "Pedro", "Sin novedad");

            assertThat(registro.getId()).isNull();
            assertThat(registro.getOrdenTrabajoId()).isEqualTo(10L);
            assertThat(registro.getCantidadProducida()).isEqualTo(30);
            assertThat(registro.getCantidadMerma()).isEqualTo(2);
            assertThat(registro.getMotivoMerma()).isEqualTo("Falla de maquina");
            assertThat(registro.getUsuario()).isEqualTo("Pedro");
            assertThat(registro.getObservacion()).isEqualTo("Sin novedad");
            assertThat(registro.getFecha()).isCloseTo(LocalDateTime.now(), within(5, ChronoUnit.SECONDS));
        }
    }
}
