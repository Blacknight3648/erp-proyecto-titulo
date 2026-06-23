package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.domain.enums.TipoServicioOS;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests unitarios PUROS del modelo de dominio para OrdenServicio.
 */
@DisplayName("OrdenServicio (dominio)")
class OrdenServicioTest {

    private OrdenServicio nuevaOSEmitida(int cantidadPactada) {
        return OrdenServicio.emitir(new DocumentNumber(2001L), 1L, 1L, TipoServicioOS.BORDADO,
                LocalDate.now().plusDays(5), "Bordado de logos", cantidadPactada,
                BigDecimal.valueOf(100), "Obs");
    }

    private DespachoOS nuevoDespacho(int cantidad) {
        return new DespachoOS(null, null, null, cantidad, "Despacho", "Juan", null);
    }

    private RecepcionOS nuevaRecepcion(int cantidad) {
        return new RecepcionOS(null, null, null, cantidad, cantidad, 0, "Pedro", null);
    }

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("emitir() crea una OS en estado EMITIDA, fecha de hoy, sin despachos ni recepciones")
        void emitirCreaOSCorrecta() {
            OrdenServicio os = nuevaOSEmitida(100);

            assertThat(os.getEstado()).isEqualTo(EstadoOS.EMITIDA);
            assertThat(os.getFechaEmision()).isEqualTo(LocalDate.now());
            assertThat(os.getCantidadPactada()).isEqualTo(100);
            assertThat(os.getDespachos()).isEmpty();
            assertThat(os.getRecepciones()).isEmpty();
        }

        @Test
        @DisplayName("el totalNeto se calcula como precioUnitario * cantidadPactada cuando no se indica")
        void calculaTotalNetoPorDefecto() {
            OrdenServicio os = nuevaOSEmitida(10);

            assertThat(os.getTotalNeto()).isEqualByComparingTo(BigDecimal.valueOf(1000)); // 100 * 10
        }

        @Test
        @DisplayName("el constructor asigna valores por defecto si estado, fecha, cantidadPactada y precioUnitario son nulos")
        void constructorAsignaValoresPorDefecto() {
            OrdenServicio os = new OrdenServicio(1L, new DocumentNumber(2001L), 1L, 1L, TipoServicioOS.LAVADO,
                    null, null, null, "Lavado", null, null, null, "Obs", null, null);

            assertThat(os.getEstado()).isEqualTo(EstadoOS.EMITIDA);
            assertThat(os.getFechaEmision()).isEqualTo(LocalDate.now());
            assertThat(os.getCantidadPactada()).isZero();
            assertThat(os.getPrecioUnitario()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(os.getTotalNeto()).isEqualByComparingTo(BigDecimal.ZERO);
            assertThat(os.getDespachos()).isEmpty();
            assertThat(os.getRecepciones()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Registrar Despacho")
    class RegistrarDespacho {

        @Test
        @DisplayName("agrega el despacho y transiciona EMITIDA -> EN_PROCESO en el primer despacho")
        void registraPrimerDespachoYTransiciona() {
            OrdenServicio os = nuevaOSEmitida(100);

            os.registrarDespacho(nuevoDespacho(40));

            assertThat(os.getDespachos()).hasSize(1);
            assertThat(os.getEstado()).isEqualTo(EstadoOS.EN_PROCESO);
            assertThat(os.totalDespachado()).isEqualTo(40);
        }

        @Test
        @DisplayName("permite múltiples despachos acumulando cantidades sin pasar la cantidad pactada")
        void acumulaDespachos() {
            OrdenServicio os = nuevaOSEmitida(100);

            os.registrarDespacho(nuevoDespacho(40));
            os.registrarDespacho(nuevoDespacho(60));

            assertThat(os.getDespachos()).hasSize(2);
            assertThat(os.totalDespachado()).isEqualTo(100);
            assertThat(os.getEstado()).isEqualTo(EstadoOS.EN_PROCESO);
        }

        @Test
        @DisplayName("rechaza un despacho nulo")
        void rechazaDespachoNulo() {
            OrdenServicio os = nuevaOSEmitida(100);

            assertThatThrownBy(() -> os.registrarDespacho(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("El despacho no puede ser nulo");
        }

        @Test
        @DisplayName("rechaza un despacho que excede la cantidad pactada")
        void rechazaDespachoQueExcedeCantidadPactada() {
            OrdenServicio os = nuevaOSEmitida(100);

            assertThatThrownBy(() -> os.registrarDespacho(nuevoDespacho(101)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("excede la cantidad pactada");
        }

        @Test
        @DisplayName("rechaza despachos acumulados que excedan la cantidad pactada")
        void rechazaDespachosAcumuladosQueExcedenCantidadPactada() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(60));

            assertThatThrownBy(() -> os.registrarDespacho(nuevoDespacho(50)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("excede la cantidad pactada");
        }

        @Test
        @DisplayName("rechaza despachar una OS CERRADA")
        void rechazaDespacharOSCerrada() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(100));
            os.registrarRecepcion(nuevaRecepcion(100));
            os.cerrar();

            assertThatThrownBy(() -> os.registrarDespacho(nuevoDespacho(1)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede despachar una OS CERRADA");
        }
    }

    @Nested
    @DisplayName("Registrar Recepción")
    class RegistrarRecepcion {

        @Test
        @DisplayName("agrega la recepción y transiciona EN_PROCESO -> RECEPCIONADA")
        void registraRecepcionYTransiciona() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(100));

            os.registrarRecepcion(nuevaRecepcion(100));

            assertThat(os.getRecepciones()).hasSize(1);
            assertThat(os.getEstado()).isEqualTo(EstadoOS.RECEPCIONADA);
            assertThat(os.totalRecibido()).isEqualTo(100);
        }

        @Test
        @DisplayName("permite múltiples recepciones parciales acumulando cantidades")
        void permiteRecepcionesParciales() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(100));

            os.registrarRecepcion(nuevaRecepcion(40));
            assertThat(os.getEstado()).isEqualTo(EstadoOS.RECEPCIONADA);

            os.registrarRecepcion(nuevaRecepcion(60));
            assertThat(os.totalRecibido()).isEqualTo(100);
            assertThat(os.getEstado()).isEqualTo(EstadoOS.RECEPCIONADA);
        }

        @Test
        @DisplayName("rechaza una recepción nula")
        void rechazaRecepcionNula() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(100));

            assertThatThrownBy(() -> os.registrarRecepcion(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("La recepción no puede ser nula");
        }

        @Test
        @DisplayName("rechaza recepcionar una OS sin despachos previos")
        void rechazaRecepcionSinDespachos() {
            OrdenServicio os = nuevaOSEmitida(100);

            assertThatThrownBy(() -> os.registrarRecepcion(nuevaRecepcion(10)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede recepcionar una OS sin despachos previos");
        }

        @Test
        @DisplayName("rechaza una recepción que excede lo despachado")
        void rechazaRecepcionQueExcedeDespachado() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(50));

            assertThatThrownBy(() -> os.registrarRecepcion(nuevaRecepcion(60)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("excede lo despachado");
        }

        @Test
        @DisplayName("rechaza recepcionar una OS CERRADA")
        void rechazaRecepcionarOSCerrada() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(100));
            os.registrarRecepcion(nuevaRecepcion(100));
            os.cerrar();

            assertThatThrownBy(() -> os.registrarRecepcion(nuevaRecepcion(1)))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("No se puede recepcionar una OS CERRADA");
        }
    }

    @Nested
    @DisplayName("Cierre")
    class Cierre {

        @Test
        @DisplayName("permite cerrar una OS en estado RECEPCIONADA")
        void permiteCerrarOSRecepcionada() {
            OrdenServicio os = nuevaOSEmitida(100);
            os.registrarDespacho(nuevoDespacho(100));
            os.registrarRecepcion(nuevaRecepcion(100));

            os.cerrar();

            assertThat(os.getEstado()).isEqualTo(EstadoOS.CERRADA);
        }

        @Test
        @DisplayName("rechaza cerrar una OS que no está RECEPCIONADA")
        void rechazaCerrarDesdeEstadoInvalido() {
            OrdenServicio os = nuevaOSEmitida(100);

            assertThatThrownBy(os::cerrar)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Transición inválida de OS");
        }
    }

    @Nested
    @DisplayName("Listas inmutables")
    class ListasInmutables {

        @Test
        @DisplayName("getDespachos() devuelve una lista inmutable")
        void getDespachosEsInmutable() {
            OrdenServicio os = nuevaOSEmitida(100);
            List<DespachoOS> despachos = os.getDespachos();

            assertThatThrownBy(() -> despachos.add(nuevoDespacho(1)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("getRecepciones() devuelve una lista inmutable")
        void getRecepcionesEsInmutable() {
            OrdenServicio os = nuevaOSEmitida(100);
            List<RecepcionOS> recepciones = os.getRecepciones();

            assertThatThrownBy(() -> recepciones.add(nuevaRecepcion(1)))
                    .isInstanceOf(UnsupportedOperationException.class);
        }
    }
}
