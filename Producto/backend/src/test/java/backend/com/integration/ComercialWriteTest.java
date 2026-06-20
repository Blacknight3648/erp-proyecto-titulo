package backend.com.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Escritura (POST/PUT/PATCH/DELETE) del módulo comercial. Usa datos sembrados por
 * data.sql (cliente 1, vendedor 1, articulo 1, plantilla 1, SCOS 1). Cubre:
 *   - creación válida (2xx)
 *   - creación inválida en endpoints @Valid (400)
 *   - operaciones por-id sobre un id inexistente (4xx, no 5xx)
 */
@DisplayName("Escritura · comercial")
class ComercialWriteTest extends AbstractIntegrationTest {

    private String json(Map<String, ?> body) throws Exception {
        return objectMapper.writeValueAsString(body);
    }

    // ---------- SolicitudCostos (SCOS) ----------

    @Test
    @DisplayName("SCOS: crear válido → 2xx")
    void scos_crear() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes-costos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "clienteId", 1,
                                "articuloDescripcion", "Polera test",
                                "cantidad", 25,
                                "genero", "UNISEX"))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("SCOS: crear sin campos obligatorios → 400")
    void scos_crearInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes-costos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("SCOS: actualizar inexistente → 4xx")
    void scos_actualizarInexistente() throws Exception {
        mockMvc.perform(put("/api/v1/solicitudes-costos/{id}", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "clienteId", 1,
                                "articuloDescripcion", "X",
                                "cantidad", 1,
                                "genero", "UNISEX"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("SCOS: eliminar inexistente → 204 (delete idempotente)")
    void scos_eliminarInexistente() throws Exception {
        mockMvc.perform(delete("/api/v1/solicitudes-costos/{id}", 999999))
                .andExpect(status().isNoContent());
    }

    // ---------- SolicitudCotizaciones ----------

    @Test
    @DisplayName("Cotización: crear válida → 2xx")
    void cotizacion_crear() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes-cotizaciones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of(
                                "clienteId", 1,
                                "articuloDescripcion", "Polera test",
                                "cantidad", 10))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("Cotización: eliminar inexistente → 204 (delete idempotente)")
    void cotizacion_eliminarInexistente() throws Exception {
        mockMvc.perform(delete("/api/v1/solicitudes-cotizaciones/{id}", 999999))
                .andExpect(status().isNoContent());
    }

    // ---------- EvaluacionNegocio ----------

    @Test
    @DisplayName("EVN: crear válida → 2xx")
    void evn_crear() throws Exception {
        mockMvc.perform(post("/api/v1/comercial/evaluaciones-negocio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("clienteId", 1, "vendedorId", 1))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("EVN: aprobar inexistente → 4xx")
    void evn_aprobarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/{id}/aprobar", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("aprobador", "tester"))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("EVN: rechazar inexistente → 4xx")
    void evn_rechazarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/{id}/rechazar", 999999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("aprobador", "tester", "motivo", "no aplica"))))
                .andExpect(status().is4xxClientError());
    }

    // ---------- Plantillas ----------

    @Test
    @DisplayName("CamposPlantilla: crear válido → 2xx")
    void campos_crear() throws Exception {
        mockMvc.perform(post("/api/v3/comercial/plantillas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("nombreCampo", "Capucha test"))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("CamposPlantilla: crear sin nombre → 400")
    void campos_crearInvalido() throws Exception {
        mockMvc.perform(post("/api/v3/comercial/plantillas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("ArticuloCamposPlantilla: upsert de campos del articulo → 2xx")
    void modeloPlantilla_crear() throws Exception {
        // Upsert: una sola fila por articulo con la lista de campos.
        mockMvc.perform(post("/api/v3/comercial/modelos-plantilla")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("idArticulo", 1, "camposPlantilla", java.util.List.of("forro", "cuello")))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("DescripcionPlantilla: crear sobre SCOS sembrada → 2xx")
    void descripcion_crear() throws Exception {
        mockMvc.perform(post("/api/v3/comercial/scos/{idSCOS}/descripciones", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("idSCOS", 1, "idPlantilla", 1, "valorDescripcion", "polar"))))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("DescripcionPlantilla: listar por SCOS sembrada → 200")
    void descripcion_listar() throws Exception {
        mockMvc.perform(post("/api/v3/comercial/scos/{idSCOS}/descripciones", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(Map.of("idSCOS", 1, "idPlantilla", 1, "valorDescripcion", "polar"))));
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/api/v3/comercial/scos/{idSCOS}/descripciones", 1))
                .andExpect(status().isOk());
    }
}
