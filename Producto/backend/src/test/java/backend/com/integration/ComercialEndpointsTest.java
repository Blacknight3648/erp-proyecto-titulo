package backend.com.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Endpoints del módulo comercial: smoke de lectura (las colecciones responden 200
 * aunque estén vacías → prueba ruteo + serialización + stack completo) y rutas
 * de "no encontrado" (by-id inexistente → 404 vía EntityNotFoundException).
 */
@DisplayName("Endpoints · comercial")
class ComercialEndpointsTest extends AbstractIntegrationTest {

    @ParameterizedTest(name = "GET {0} → 200")
    @ValueSource(strings = {
            "/api/v1/comercial/notas-venta",
            "/api/v1/comercial/notas-venta/next-number",
            "/api/v1/comercial/evaluaciones-negocio",
            "/api/v1/comercial/evaluaciones-negocio/next-number",
            "/api/v1/solicitudes-costos",
            "/api/v1/solicitudes-cotizaciones",
            "/api/v3/comercial/plantillas"
    })
    @DisplayName("colecciones de lectura responden 200")
    void getCollections_returnOk(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET nota de venta inexistente → 404")
    void getNotaVenta_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/comercial/notas-venta/{id}", 999999))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("GET evaluación de negocio inexistente → 404")
    void getEvn_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/comercial/evaluaciones-negocio/{id}", 999999))
                .andExpect(status().isNotFound());
    }
}
