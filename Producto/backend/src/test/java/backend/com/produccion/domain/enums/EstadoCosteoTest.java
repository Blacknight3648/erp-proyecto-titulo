package backend.com.produccion.domain.enums;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static backend.com.produccion.domain.enums.EstadoCosteo.APROBADO;
import static backend.com.produccion.domain.enums.EstadoCosteo.BORRADOR;
import static backend.com.produccion.domain.enums.EstadoCosteo.COSTEADO;
import static backend.com.produccion.domain.enums.EstadoCosteo.RECHAZADO;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("EstadoCosteo.puedeTransicionarA")
class EstadoCosteoTest {

    @Test
    @DisplayName("transiciones válidas del modelo lineal con reproceso")
    void transicionesValidas() {
        assertThat(BORRADOR.puedeTransicionarA(COSTEADO)).isTrue();
        assertThat(BORRADOR.puedeTransicionarA(RECHAZADO)).isTrue();

        assertThat(COSTEADO.puedeTransicionarA(APROBADO)).isTrue();
        assertThat(COSTEADO.puedeTransicionarA(RECHAZADO)).isTrue();
        assertThat(COSTEADO.puedeTransicionarA(BORRADOR)).isTrue();

        assertThat(RECHAZADO.puedeTransicionarA(BORRADOR)).isTrue();
    }

    @Test
    @DisplayName("transiciones inválidas y estado terminal APROBADO")
    void transicionesInvalidas() {
        assertThat(BORRADOR.puedeTransicionarA(APROBADO)).isFalse();
        assertThat(BORRADOR.puedeTransicionarA(BORRADOR)).isFalse();

        // APROBADO es terminal
        assertThat(APROBADO.puedeTransicionarA(BORRADOR)).isFalse();
        assertThat(APROBADO.puedeTransicionarA(COSTEADO)).isFalse();
        assertThat(APROBADO.puedeTransicionarA(RECHAZADO)).isFalse();

        // RECHAZADO solo puede reabrirse a BORRADOR
        assertThat(RECHAZADO.puedeTransicionarA(APROBADO)).isFalse();
        assertThat(RECHAZADO.puedeTransicionarA(COSTEADO)).isFalse();

        // destino nulo nunca es válido
        assertThat(BORRADOR.puedeTransicionarA(null)).isFalse();
    }
}
