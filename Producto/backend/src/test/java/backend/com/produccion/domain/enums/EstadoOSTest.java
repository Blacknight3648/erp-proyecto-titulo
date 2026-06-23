package backend.com.produccion.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static backend.com.produccion.domain.enums.EstadoOS.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EstadoOS")
class EstadoOSTest {

    @Nested
    @DisplayName("Transiciones válidas")
    class TransicionesValidas {

        @Test
        @DisplayName("EMITIDA → EN_PROCESO")
        void emitidaAEnProceso() {
            assertThat(EMITIDA.puedeTransicionarA(EN_PROCESO)).isTrue();
        }

        @Test
        @DisplayName("EN_PROCESO → EN_PROCESO")
        void enProcesoAEnProceso() {
            assertThat(EN_PROCESO.puedeTransicionarA(EN_PROCESO)).isTrue();
        }

        @Test
        @DisplayName("EN_PROCESO → RECEPCIONADA")
        void enProcesoARecepcionada() {
            assertThat(EN_PROCESO.puedeTransicionarA(RECEPCIONADA)).isTrue();
        }

        @Test
        @DisplayName("RECEPCIONADA → RECEPCIONADA")
        void recepcionadaARecepcionada() {
            assertThat(RECEPCIONADA.puedeTransicionarA(RECEPCIONADA)).isTrue();
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
        @EnumSource(EstadoOS.class)
        @DisplayName("CERRADA no puede transicionar a ningún estado")
        void cerradaNoTransiciona(EstadoOS destino) {
            assertThat(CERRADA.puedeTransicionarA(destino)).isFalse();
        }
    }

    @Nested
    @DisplayName("Destino null")
    class DestinoNull {

        @ParameterizedTest(name = "{0} → null es false")
        @EnumSource(EstadoOS.class)
        @DisplayName("ningún estado puede transicionar a null")
        void ningunEstadoTransicionaANull(EstadoOS origen) {
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
                    Arguments.of(EMITIDA, EN_PROCESO, true),
                    Arguments.of(EMITIDA, RECEPCIONADA, false),
                    Arguments.of(EMITIDA, CERRADA, false),

                    // EN_PROCESO →
                    Arguments.of(EN_PROCESO, EMITIDA, false),
                    Arguments.of(EN_PROCESO, EN_PROCESO, true),
                    Arguments.of(EN_PROCESO, RECEPCIONADA, true),
                    Arguments.of(EN_PROCESO, CERRADA, false),

                    // RECEPCIONADA →
                    Arguments.of(RECEPCIONADA, EMITIDA, false),
                    Arguments.of(RECEPCIONADA, EN_PROCESO, false),
                    Arguments.of(RECEPCIONADA, RECEPCIONADA, true),
                    Arguments.of(RECEPCIONADA, CERRADA, true),

                    // CERRADA →
                    Arguments.of(CERRADA, EMITIDA, false),
                    Arguments.of(CERRADA, EN_PROCESO, false),
                    Arguments.of(CERRADA, RECEPCIONADA, false),
                    Arguments.of(CERRADA, CERRADA, false)
            );
        }

        @ParameterizedTest(name = "{0} → {1} = {2}")
        @MethodSource("matrizDeTransiciones")
        @DisplayName("validación exhaustiva de cada par (origen, destino)")
        void verificarTransicion(EstadoOS origen, EstadoOS destino, boolean esperado) {
            assertThat(origen.puedeTransicionarA(destino))
                    .as("%s → %s debería ser %s", origen, destino, esperado)
                    .isEqualTo(esperado);
        }
    }
}
