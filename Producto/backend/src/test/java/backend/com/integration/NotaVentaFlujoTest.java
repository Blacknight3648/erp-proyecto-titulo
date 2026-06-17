package backend.com.integration;

import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.ItemNVDTO;
import backend.com.comercial.domain.enums.TipoItem;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Flujo de extremo a extremo del feature OT-NV que construimos:
 *
 *   POST  crear NV (ítem con requiereOt=true)  → estado BORRADOR, sin OT aún
 *   PATCH aprobar NV                            → dispara generarDesdeNotaVenta
 *   GET   ordenes-trabajo/nota-venta/{id}       → existe la OT de modificación,
 *                                                 con el detalleOt como observaciones
 *                                                 y la FK item_nv_id resuelta.
 *
 * Usa los clientes/vendedores sembrados por data.sql (cliente 1, vendedor 1).
 */
@DisplayName("Flujo end-to-end · NV → aprobar → OT")
class NotaVentaFlujoTest extends AbstractIntegrationTest {

    private static final String DETALLE_OT = "Bordar logo bordado en el pecho";

    @Test
    @DisplayName("aprobar una NV con ítem requiereOt genera la OT de modificación vinculada")
    void aprobarNV_generaOrdenTrabajo() throws Exception {
        // 1) Crear NV con un ítem que requiere OT (prenda lista, tipoItem != OP)
        CrearNVCommand cmd = new CrearNVCommand();
        cmd.setEvaluacionNegocioId(2L);
        cmd.setClienteId(1L);
        cmd.setVendedorId(1L);
        cmd.setFechaEntregaEstimada(LocalDate.now().plusDays(20));
        cmd.setEsKit(false);

        ItemNVDTO item = new ItemNVDTO();
        item.setModelo("Polera Pique");
        item.setItemType(TipoItem.SC); // prenda lista (no genera fases de producción)
        item.setCantidad(10);
        item.setPrecioUnitario(new java.math.BigDecimal("5990"));
        item.setLlevaLogo("N/A");
        item.setRequiereOt(true);
        item.setDetalleOt(DETALLE_OT);
        cmd.setItems(List.of(item));

        MvcResult creada = mockMvc.perform(post("/api/v1/comercial/notas-venta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cmd)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("BORRADOR"))
                .andReturn();

        Long nvId = objectMapper.readTree(creada.getResponse().getContentAsString()).get("idNV").asLong();
        assertThat(nvId).isNotNull();

        // 2) Antes de aprobar no debe existir OT de modificación para esa NV
        mockMvc.perform(get("/api/v1/produccion/ordenes-trabajo/nota-venta/{id}", nvId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        // 3) Aprobar la NV (dispara la generación de la OT)
        FirmaAprobacionRequest firma = new FirmaAprobacionRequest();
        firma.setAprobador("tester");
        mockMvc.perform(patch("/api/v1/comercial/notas-venta/{id}/aprobar", nvId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firma)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("APROBADA"));

        // 4) Ahora sí existe la OT, con el detalle como observaciones, estado PENDIENTE
        //    y la FK item_nv_id resuelta hacia el ítem de la NV.
        MvcResult ots = mockMvc.perform(get("/api/v1/produccion/ordenes-trabajo/nota-venta/{id}", nvId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].observaciones").value(DETALLE_OT))
                .andExpect(jsonPath("$[0].estadoOT").value("PENDIENTE"))
                .andExpect(jsonPath("$[0].notaVentaId").value(nvId))
                .andReturn();

        JsonNode ot = objectMapper.readTree(ots.getResponse().getContentAsString()).get(0);
        assertThat(ot.get("itemNVId").isNull()).isFalse();
    }

    @Test
    @DisplayName("POST con cuerpo inválido (sin cliente/vendedor) → 400")
    void crearNV_sinCamposObligatorios_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/v1/comercial/notas-venta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
