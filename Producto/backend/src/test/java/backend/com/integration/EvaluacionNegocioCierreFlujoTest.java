package backend.com.integration;

import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.ItemNVDTO;
import backend.com.comercial.domain.enums.TipoItem;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Flujo end-to-end del cierre de EVN: adjudicar → generar NV (ok) → cerrar EVN →
 * intentar generar otra NV (bloqueada). Usa la EVN 2 sembrada por data.sql
 * (estado inicial APROBADA).
 */
@DisplayName("Flujo end-to-end · cierre de EVN bloquea nuevas NV")
class EvaluacionNegocioCierreFlujoTest extends AbstractIntegrationTest {

    private CrearNVCommand nvDesdeEvn2() {
        CrearNVCommand cmd = new CrearNVCommand();
        cmd.setEvaluacionNegocioId(2L);
        cmd.setClienteId(1L);
        cmd.setVendedorId(1L);
        cmd.setEsKit(false);
        cmd.setFechaEntregaEstimada(LocalDate.now().plusDays(20));

        ItemNVDTO item = new ItemNVDTO();
        item.setModelo("Polera Pique");
        item.setItemType(TipoItem.SC);
        item.setCantidad(5);
        item.setPrecioUnitario(new java.math.BigDecimal("5990"));
        item.setLlevaLogo("N/A");
        cmd.setItems(List.of(item));
        return cmd;
    }

    @Test
    @DisplayName("una EVN cerrada ya no permite generar nuevas Notas de Venta (422)")
    void cierreBloqueaNuevaNV() throws Exception {
        FirmaAprobacionRequest firma = new FirmaAprobacionRequest();
        firma.setAprobador("tester");

        // 1) Adjudicar la EVN 2 (APROBADA → ADJUDICADA)
        mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/{id}/adjudicar", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firma)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("ADJUDICADA"));

        // 2) Con la EVN ADJUDICADA, generar una NV está permitido
        mockMvc.perform(post("/api/v1/comercial/notas-venta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nvDesdeEvn2())))
                .andExpect(status().isOk());

        // 3) Cerrar la EVN (ADJUDICADA → CERRADA)
        mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/{id}/cerrar", 2L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firma)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CERRADA"));

        // 4) Tras el cierre, generar otra NV desde la misma EVN queda bloqueado (422)
        mockMvc.perform(post("/api/v1/comercial/notas-venta")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(nvDesdeEvn2())))
                .andExpect(status().isUnprocessableEntity());
    }
}
