package backend.com.produccion.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static backend.com.produccion.domain.enums.EstadoCosteo.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests exhaustivos del enum EstadoCosteo y su lógica de transiciones.
 *
 * <pre>
 * BORRADOR  → COSTEADO, RECHAZADO
 * COSTEADO  → APROBADO, RECHAZADO, BORRADOR (reproceso)
 * APROBADO  → (terminal)
 * RECHAZADO → BORRADOR (reproceso)
 * </pre>
 */
@DisplayName("EstadoCosteo")
class EstadoCosteoTest {

    // ------------------------------------------------------------------ //
    //  Transiciones válidas (aristas del grafo)
    // ------------------------------------------------------------------ //

    @Nested
    @DisplayName("Transiciones válidas")
    class TransicionesValidas {

        @Test
        @DisplayName("BORRADOR → COSTEADO")
        void borradorACosteado() {
            assertThat(BORRADOR.puedeTransicionarA(COSTEADO)).isTrue();
        }

        @Test
        @DisplayName("BORRADOR → RECHAZADO")
        void borradorARechazado() {
            assertThat(BORRADOR.puedeTransicionarA(RECHAZADO)).isTrue();
        }

        @Test
        @DisplayName("COSTEADO → APROBADO")
        void costeadoAAprobado() {
            assertThat(COSTEADO.puedeTransicionarA(APROBADO)).isTrue();
        }

        @Test
        @DisplayName("COSTEADO → RECHAZADO")
        void costeadoARechazado() {
            assertThat(COSTEADO.puedeTransicionarA(RECHAZADO)).isTrue();
        }

        @Test
        @DisplayName("COSTEADO → BORRADOR (reproceso)")
        void costeadoABorrador() {
            assertThat(COSTEADO.puedeTransicionarA(BORRADOR)).isTrue();
        }

        @Test
        @DisplayName("RECHAZADO → BORRADOR (retomar)")
        void rechazadoABorrador() {
            assertThat(RECHAZADO.puedeTransicionarA(BORRADOR)).isTrue();
        }
    }

    // ------------------------------------------------------------------ //
    //  Transiciones inválidas (aristas que NO existen)
    // ------------------------------------------------------------------ //

    @Nested
    @DisplayName("Transiciones inválidas")
    class TransicionesInvalidas {

        @Test
        @DisplayName("BORRADOR no puede ir directamente a APROBADO")
        void borradorNoAprueba() {
            assertThat(BORRADOR.puedeTransicionarA(APROBADO)).isFalse();
        }

        @Test
        @DisplayName("BORRADOR no puede transicionar a sí mismo")
        void borradorNoASiMismo() {
            assertThat(BORRADOR.puedeTransicionarA(BORRADOR)).isFalse();
        }

        @Test
        @DisplayName("RECHAZADO no puede ir a APROBADO")
        void rechazadoNoAprueba() {
            assertThat(RECHAZADO.puedeTransicionarA(APROBADO)).isFalse();
        }

        @Test
        @DisplayName("RECHAZADO no puede ir a COSTEADO directamente")
        void rechazadoNoCosteado() {
            assertThat(RECHAZADO.puedeTransicionarA(COSTEADO)).isFalse();
        }

        @Test
        @DisplayName("RECHAZADO no puede transicionar a sí mismo")
        void rechazadoNoASiMismo() {
            assertThat(RECHAZADO.puedeTransicionarA(RECHAZADO)).isFalse();
        }

        @Test
        @DisplayName("COSTEADO no puede transicionar a sí mismo")
        void costeadoNoASiMismo() {
            assertThat(COSTEADO.puedeTransicionarA(COSTEADO)).isFalse();
        }
    }

    // ------------------------------------------------------------------ //
    //  Estado terminal: APROBADO
    // ------------------------------------------------------------------ //

    @Nested
    @DisplayName("APROBADO es estado terminal")
    class AprobadoTerminal {

        @ParameterizedTest(name = "APROBADO → {0} es inválida")
        @EnumSource(EstadoCosteo.class)
        @DisplayName("APROBADO no puede transicionar a ningún estado")
        void aprobadoNoTransiciona(EstadoCosteo destino) {
            assertThat(APROBADO.puedeTransicionarA(destino)).isFalse();
        }
    }

    // ------------------------------------------------------------------ //
    //  Destino null
    // ------------------------------------------------------------------ //

    @Nested
    @DisplayName("Destino null")
    class DestinoNull {

        @ParameterizedTest(name = "{0} → null es false")
        @EnumSource(EstadoCosteo.class)
        @DisplayName("ningún estado puede transicionar a null")
        void ningunEstadoTransicionaANull(EstadoCosteo origen) {
            assertThat(origen.puedeTransicionarA(null)).isFalse();
        }
    }

    // ------------------------------------------------------------------ //
    //  Matriz exhaustiva de transiciones (parametrizada)
    // ------------------------------------------------------------------ //

    @Nested
    @DisplayName("Matriz completa de transiciones")
    class MatrizTransiciones {

        /**
         * Genera las 16 combinaciones posibles (4 orígenes × 4 destinos) con
         * el resultado esperado según el grafo de estados documentado.
         */
        static Stream<Arguments> matrizDeTransiciones() {
            return Stream.of(
                    // BORRADOR →
                    Arguments.of(BORRADOR, BORRADOR,  false),
                    Arguments.of(BORRADOR, COSTEADO,  true),
                    Arguments.of(BORRADOR, APROBADO,  false),
                    Arguments.of(BORRADOR, RECHAZADO, true),

                    // COSTEADO →
                    Arguments.of(COSTEADO, BORRADOR,  true),
                    Arguments.of(COSTEADO, COSTEADO,  false),
                    Arguments.of(COSTEADO, APROBADO,  true),
                    Arguments.of(COSTEADO, RECHAZADO, true),

                    // APROBADO → (terminal)
                    Arguments.of(APROBADO, BORRADOR,  false),
                    Arguments.of(APROBADO, COSTEADO,  false),
                    Arguments.of(APROBADO, APROBADO,  false),
                    Arguments.of(APROBADO, RECHAZADO, false),

                    // RECHAZADO →
                    Arguments.of(RECHAZADO, BORRADOR,  true),
                    Arguments.of(RECHAZADO, COSTEADO,  false),
                    Arguments.of(RECHAZADO, APROBADO,  false),
                    Arguments.of(RECHAZADO, RECHAZADO, false)
            );
        }

        @ParameterizedTest(name = "{0} → {1} = {2}")
        @MethodSource("matrizDeTransiciones")
        @DisplayName("validación exhaustiva de cada par (origen, destino)")
        void verificarTransicion(EstadoCosteo origen, EstadoCosteo destino, boolean esperado) {
            assertThat(origen.puedeTransicionarA(destino))
                    .as("%s → %s debería ser %s", origen, destino, esperado)
                    .isEqualTo(esperado);
        }
    }

    // ------------------------------------------------------------------ //
    //  Descripción (getDescripcion)
    // ------------------------------------------------------------------ //

    @Nested
    @DisplayName("Etiquetas de descripción")
    class Descripciones {

        @Test
        @DisplayName("cada estado tiene la descripción legible correcta")
        void descripcionesCorrectas() {
            assertThat(BORRADOR.getDescripcion()).isEqualTo("Borrador");
            assertThat(COSTEADO.getDescripcion()).isEqualTo("Costeado");
            assertThat(APROBADO.getDescripcion()).isEqualTo("Aprobado");
            assertThat(RECHAZADO.getDescripcion()).isEqualTo("Rechazado");
        }

        @ParameterizedTest(name = "{0} tiene descripción no vacía")
        @EnumSource(EstadoCosteo.class)
        @DisplayName("ninguna descripción es nula ni vacía")
        void descripcionNuncaVacia(EstadoCosteo estado) {
            assertThat(estado.getDescripcion()).isNotNull().isNotBlank();
        }
    }

    // ------------------------------------------------------------------ //
    //  Cobertura de valores del enum
    // ------------------------------------------------------------------ //

    @Test
    @DisplayName("el enum tiene exactamente 4 valores")
    void cantidadDeValores() {
        assertThat(EstadoCosteo.values()).hasSize(4);
    }

    @Test
    @DisplayName("valueOf funciona para los 4 estados")
    void valueOfFunciona() {
        assertThat(EstadoCosteo.valueOf("BORRADOR")).isEqualTo(BORRADOR);
        assertThat(EstadoCosteo.valueOf("COSTEADO")).isEqualTo(COSTEADO);
        assertThat(EstadoCosteo.valueOf("APROBADO")).isEqualTo(APROBADO);
        assertThat(EstadoCosteo.valueOf("RECHAZADO")).isEqualTo(RECHAZADO);
    }
}
