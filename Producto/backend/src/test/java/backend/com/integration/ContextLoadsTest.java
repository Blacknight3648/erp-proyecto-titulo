package backend.com.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Sanidad: si el contexto completo levanta (Hibernate + las 12 migraciones Flyway
 * sobre H2 + todos los beans de comercial/produccion), MockMvc queda inyectado.
 */
@DisplayName("Contexto de aplicación")
class ContextLoadsTest extends AbstractIntegrationTest {

    @Test
    @DisplayName("levanta el contexto y cablea MockMvc")
    void contextLoads() {
        assertThat(mockMvc).isNotNull();
    }
}
