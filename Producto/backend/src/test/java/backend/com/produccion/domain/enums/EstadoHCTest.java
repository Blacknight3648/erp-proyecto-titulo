package backend.com.produccion.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static backend.com.produccion.domain.enums.EstadoHC.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EstadoHC")
class EstadoHCTest {

    @Nested
    @DisplayName("Transiciones válidas")
    class TransicionesValidas {

        @Test
        @DisplayName("BORRADOR → APROBADA")
        void borradorAAprobada() {
            assertThat(BORRADOR.puedeTransicionarA(APROBADA)).isTrue();
        }

        @Test
        @DisplayName("APROBADA → CERRADA")
        void aprobadaACerrada() {
            assertThat(APROBADA.puedeTransicionarA(CERRADA)).isTrue();
        }
    }

    @Nested
    @DisplayName("Estado terminal: CERRADA")
    class CerradaTerminal {

        @ParameterizedTest(name = "CERRADA → {0} es inválida")
        @EnumSource(EstadoHC.class)
        @DisplayName("CERRADA no puede transicionar a ningún estado")
        void cerradaNoTransiciona(EstadoHC destino) {
            assertThat(CERRADA.puedeTransicionarA(destino)).isFalse();
        }
    }

    @Nested
    @DisplayName("Destino null")
    class DestinoNull {

        @ParameterizedTest(name = "{0} → null es false")
        @EnumSource(EstadoHC.class)
        @DisplayName("ningún estado puede transicionar a null")
        void ningunEstadoTransicionaANull(EstadoHC origen) {
            assertThat(origen.puedeTransicionarA(null)).isFalse();
        }
    }

    @Nested
    @DisplayName("Matriz completa de transiciones")
    class MatrizTransiciones {

        static Stream<Arguments> matrizDeTransiciones() {
            return Stream.of(
                    // BORRADOR →
                    Arguments.of(BORRADOR, BORRADOR, false),
                    Arguments.of(BORRADOR, APROBADA, true),
                    Arguments.of(BORRADOR, CERRADA, false),

                    // APROBADA →
                    Arguments.of(APROBADA, BORRADOR, false),
                    Arguments.of(APROBADA, APROBADA, false),
                    Arguments.of(APROBADA, CERRADA, true),

                    // CERRADA →
                    Arguments.of(CERRADA, BORRADOR, false),
                    Arguments.of(CERRADA, APROBADA, false),
                    Arguments.of(CERRADA, CERRADA, false)
            );
        }

        @ParameterizedTest(name = "{0} → {1} = {2}")
        @MethodSource("matrizDeTransiciones")
        @DisplayName("validación exhaustiva de cada par (origen, destino)")
        void verificarTransicion(EstadoHC origen, EstadoHC destino, boolean esperado) {
            assertThat(origen.puedeTransicionarA(destino))
                    .as("%s → %s debería ser %s", origen, destino, esperado)
                    .isEqualTo(esperado);
        }
    }
}
