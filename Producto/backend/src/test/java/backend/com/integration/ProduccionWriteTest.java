package backend.com.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Escritura (POST/PUT/PATCH/DELETE) del módulo produccion. La mayoría son
 * transiciones de estado sobre entidades existentes; aquí se verifica que cada
 * endpoint rutea y maneja correctamente la entrada inválida / id inexistente
 * (responde 4xx, no 5xx) y que la validación de los @Valid funciona.
 */
@DisplayName("Escritura · produccion")
class ProduccionWriteTest extends AbstractIntegrationTest {

    private static final long NOPE = 999999L;

    // NOTA: CosteoController.actualizar (PUT /costeos/{id}) NO se prueba aquí porque
    // es un "upsert" sin validación de entrada ni chequeo de existencia: con un body
    // inválido responde 500 (falta @Valid en CosteoDTO). Queda reportado como hallazgo
    // para endurecer la validación, no se cubre con un test que normalice ese 500.

    // ---------- HojaCompra ----------

    @Test
    @DisplayName("HojaCompra: generar desde OP inexistente → 4xx")
    void hc_generarOpInexistente() throws Exception {
        mockMvc.perform(post("/api/v1/hojas-compra/generar/{opId}", NOPE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("HojaCompra: aprobar inexistente → 4xx")
    void hc_aprobarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/hojas-compra/{id}/aprobar", NOPE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("HojaCompra: cerrar inexistente → 4xx")
    void hc_cerrarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/hojas-compra/{id}/cerrar", NOPE))
                .andExpect(status().is4xxClientError());
    }

    // ---------- OrdenCompra ----------

    @Test
    @DisplayName("OrdenCompra: enviar inexistente → 4xx")
    void oc_enviarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/ordenes-compra/{id}/enviar", NOPE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("OrdenCompra: recepcionar inexistente → 4xx")
    void oc_recepcionarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/ordenes-compra/{id}/recepcionar", NOPE))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("OrdenCompra: cerrar inexistente → 4xx")
    void oc_cerrarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/ordenes-compra/{id}/cerrar", NOPE))
                .andExpect(status().is4xxClientError());
    }

    // ---------- OrdenProduccion ----------

    @Test
    @DisplayName("OrdenProduccion: recepcionar inexistente → 4xx")
    void op_recepcionarInexistente() throws Exception {
        mockMvc.perform(post("/api/v1/produccion/ordenes-produccion/recepcionar/{id}", NOPE))
                .andExpect(status().is4xxClientError());
    }

    // ---------- OrdenServicio ----------

    @Test
    @DisplayName("OrdenServicio: crear con cuerpo inválido → 400")
    void os_crearInvalido() throws Exception {
        mockMvc.perform(post("/api/v1/ordenes-servicio")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("OrdenServicio: cerrar inexistente → 4xx")
    void os_cerrarInexistente() throws Exception {
        mockMvc.perform(patch("/api/v1/ordenes-servicio/{id}/cerrar", NOPE))
                .andExpect(status().is4xxClientError());
    }

    // ---------- OrdenTrabajo (state machine) ----------

    @Test
    @DisplayName("OrdenTrabajo: iniciar inexistente → no falla (no-op idempotente)")
    void ot_iniciarInexistente() throws Exception {
        mockMvc.perform(post("/api/v1/produccion/ordenes-trabajo/{id}/iniciar", NOPE))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName("OrdenTrabajo: finalizar inexistente → no falla (no-op idempotente)")
    void ot_finalizarInexistente() throws Exception {
        mockMvc.perform(post("/api/v1/produccion/ordenes-trabajo/{id}/finalizar", NOPE))
                .andExpect(status().is2xxSuccessful());
    }

    // ---------- RecepcionOC ----------

    @Test
    @DisplayName("RecepcionOC: recepcionar sobre OC inexistente → 4xx")
    void recepcion_ocInexistente() throws Exception {
        mockMvc.perform(post("/api/v1/recepciones-oc/oc/{ocId}", NOPE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().is4xxClientError());
    }
}
