package backend.com.produccion.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static backend.com.produccion.domain.enums.EstadoOC.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EstadoOC")
class EstadoOCTest {

    @Nested
    @DisplayName("Transiciones válidas")
    class TransicionesValidas {

        @Test
        @DisplayName("EMITIDA → ENVIADA")
        void emitidaAEnviada() {
            assertThat(EMITIDA.puedeTransicionarA(ENVIADA)).isTrue();
        }

        @Test
        @DisplayName("ENVIADA → RECEPCIONADA_PARCIAL")
        void enviadaARecepcionadaParcial() {
            assertThat(ENVIADA.puedeTransicionarA(RECEPCIONADA_PARCIAL)).isTrue();
        }

        @Test
        @DisplayName("ENVIADA → RECEPCIONADA")
        void enviadaARecepcionada() {
            assertThat(ENVIADA.puedeTransicionarA(RECEPCIONADA)).isTrue();
        }

        @Test
        @DisplayName("RECEPCIONADA_PARCIAL → RECEPCIONADA_PARCIAL")
        void recepcionadaParcialARecepcionadaParcial() {
            assertThat(RECEPCIONADA_PARCIAL.puedeTransicionarA(RECEPCIONADA_PARCIAL)).isTrue();
        }

        @Test
        @DisplayName("RECEPCIONADA_PARCIAL → RECEPCIONADA")
        void recepcionadaParcialARecepcionada() {
            assertThat(RECEPCIONADA_PARCIAL.puedeTransicionarA(RECEPCIONADA)).isTrue();
        }

        @Test
        @DisplayName("RECEPCIONADA → CERRADA")
        void recepcionadaACerrada() {
            assertThat(RECEPCIONADA.puedeTransicionarA(CERRADA)).isTrue();
        }
    }

    @Nested
    @DisplayName("Estado terminal: CERRADA")
    class CerradaTerminal {

        @ParameterizedTest(name = "CERRADA → {0} es inválida")
        @EnumSource(EstadoOC.class)
        @DisplayName("CERRADA no puede transicionar a ningún estado")
        void cerradaNoTransiciona(EstadoOC destino) {
            assertThat(CERRADA.puedeTransicionarA(destino)).isFalse();
        }
    }

    @Nested
    @DisplayName("Destino null")
    class DestinoNull {

        @ParameterizedTest(name = "{0} → null es false")
        @EnumSource(EstadoOC.class)
        @DisplayName("ningún estado puede transicionar a null")
        void ningunEstadoTransicionaANull(EstadoOC origen) {
            assertThat(origen.puedeTransicionarA(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("Matriz completa de transiciones")
    class MatrizTransiciones {

        static Stream<Arguments> matrizDeTransiciones() {
            return Stream.of(
                    // EMITIDA →
                    Arguments.of(EMITIDA, EMITIDA, false),
                    Arguments.of(EMITIDA, ENVIADA, true),
                    Arguments.of(EMITIDA, RECEPCIONADA_PARCIAL, false),
                    Arguments.of(EMITIDA, RECEPCIONADA, false),
                    Arguments.of(EMITIDA, CERRADA, false),

                    // ENVIADA →
                    Arguments.of(ENVIADA, EMITIDA, false),
                    Arguments.of(ENVIADA, ENVIADA, false),
                    Arguments.of(ENVIADA, RECEPCIONADA_PARCIAL, true),
                    Arguments.of(ENVIADA, RECEPCIONADA, true),
                    Arguments.of(ENVIADA, CERRADA, false),

                    // RECEPCIONADA_PARCIAL →
                    Arguments.of(RECEPCIONADA_PARCIAL, EMITIDA, false),
                    Arguments.of(RECEPCIONADA_PARCIAL, ENVIADA, false),
                    Arguments.of(RECEPCIONADA_PARCIAL, RECEPCIONADA_PARCIAL, true),
                    Arguments.of(RECEPCIONADA_PARCIAL, RECEPCIONADA, true),
                    Arguments.of(RECEPCIONADA_PARCIAL, CERRADA, false),

                    // RECEPCIONADA →
                    Arguments.of(RECEPCIONADA, EMITIDA, false),
                    Arguments.of(RECEPCIONADA, ENVIADA, false),
                    Arguments.of(RECEPCIONADA, RECEPCIONADA_PARCIAL, false),
                    Arguments.of(RECEPCIONADA, RECEPCIONADA, false),
                    Arguments.of(RECEPCIONADA, CERRADA, true),

                    // CERRADA →
                    Arguments.of(CERRADA, EMITIDA, false),
                    Arguments.of(CERRADA, ENVIADA, false),
                    Arguments.of(CERRADA, RECEPCIONADA_PARCIAL, false),
                    Arguments.of(CERRADA, RECEPCIONADA, false),
                    Arguments.of(CERRADA, CERRADA, false)
            );
        }

        @ParameterizedTest(name = "{0} → {1} = {2}")
        @MethodSource("matrizDeTransiciones")
        @DisplayName("validación exhaustiva de cada par (origen, destino)")
        void verificarTransicion(EstadoOC origen, EstadoOC destino, boolean esperado) {
            assertThat(origen.puedeTransicionarA(destino))
                    .as("%s → %s debería ser %s", origen, destino, esperado)
                    .isEqualTo(esperado);
        }
    }
}
