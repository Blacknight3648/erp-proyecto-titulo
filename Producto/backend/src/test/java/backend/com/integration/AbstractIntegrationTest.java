package backend.com.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Base para los tests de integración de endpoints (comercial + produccion).
 *
 * Levanta el contexto completo de Spring sobre H2 (perfil dev), ejecuta Hibernate
 * (ddl-auto=update) + las migraciones Flyway, y golpea los endpoints reales vía
 * MockMvc. Como todas las subclases comparten esta misma configuración, Spring
 * cachea y reutiliza un único contexto entre clases de test (un solo arranque).
 *
 * Cada método de test corre en una transacción que se revierte al terminar
 * (@Transactional), de modo que los tests quedan aislados entre sí.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
@Transactional
public abstract class AbstractIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;
}
