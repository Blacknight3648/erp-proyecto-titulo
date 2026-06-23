package backend.com.produccion.controller;

import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.application.service.OrdenCompraService;
import backend.com.produccion.application.service.OrdenServicioService;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.infrastructure.api.ReportesController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ReportesController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ReportesController (API Web)")
class ReportesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HojaCompraService hojaCompraService;

    @MockitoBean
    private OrdenCompraService ordenCompraService;

    @MockitoBean
    private OrdenServicioService ordenServicioService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    @Nested
    @DisplayName("Consultas de Reportes (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET /hcs-pendientes-aprobacion - Retorna las Hojas de Compra en estado BORRADOR")
        void hcsPendientesAprobacion_retornaListaFiltrada() throws Exception {
            HojaCompraDTO dto = HojaCompraDTO.builder()
                    .idHC(1L)
                    .numeroHC("HC-001")
                    .estado(EstadoHC.BORRADOR)
                    .build();
            when(hojaCompraService.listarPorEstado(EstadoHC.BORRADOR)).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/reportes/hcs-pendientes-aprobacion"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].numeroHC").value("HC-001"))
                    .andExpect(jsonPath("$[0].estado").value("BORRADOR"));

            verify(hojaCompraService).listarPorEstado(EstadoHC.BORRADOR);
        }

        @Test
        @DisplayName("GET /ocs-pendientes-recepcion - Combina OCs emitidas, enviadas y con recepción parcial")
        void ocsPendientesRecepcion_combinaEstadosPendientes() throws Exception {
            OrdenCompraDTO emitida = OrdenCompraDTO.builder().idOC(1L).numeroOC("OC-001").estado(EstadoOC.EMITIDA).build();
            OrdenCompraDTO enviada = OrdenCompraDTO.builder().idOC(2L).numeroOC("OC-002").estado(EstadoOC.ENVIADA).build();
            OrdenCompraDTO parcial = OrdenCompraDTO.builder().idOC(3L).numeroOC("OC-003").estado(EstadoOC.RECEPCIONADA_PARCIAL).build();

            when(ordenCompraService.listarPorEstado(EstadoOC.EMITIDA)).thenReturn(List.of(emitida));
            when(ordenCompraService.listarPorEstado(EstadoOC.ENVIADA)).thenReturn(List.of(enviada));
            when(ordenCompraService.listarPorEstado(EstadoOC.RECEPCIONADA_PARCIAL)).thenReturn(List.of(parcial));

            mockMvc.perform(get("/api/v1/reportes/ocs-pendientes-recepcion"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(3))
                    .andExpect(jsonPath("$[0].numeroOC").value("OC-001"))
                    .andExpect(jsonPath("$[1].numeroOC").value("OC-002"))
                    .andExpect(jsonPath("$[2].numeroOC").value("OC-003"));

            verify(ordenCompraService).listarPorEstado(EstadoOC.EMITIDA);
            verify(ordenCompraService).listarPorEstado(EstadoOC.ENVIADA);
            verify(ordenCompraService).listarPorEstado(EstadoOC.RECEPCIONADA_PARCIAL);
        }

        @Test
        @DisplayName("GET /oss-en-taller - Combina OSs en proceso y recepcionadas")
        void ossEnTaller_combinaEstadosEnTaller() throws Exception {
            OrdenServicioDTO enProceso = OrdenServicioDTO.builder().idOS(1L).numeroOS("OS-001").estado(EstadoOS.EN_PROCESO).build();
            OrdenServicioDTO recepcionada = OrdenServicioDTO.builder().idOS(2L).numeroOS("OS-002").estado(EstadoOS.RECEPCIONADA).build();

            when(ordenServicioService.listarPorEstado(EstadoOS.EN_PROCESO)).thenReturn(List.of(enProceso));
            when(ordenServicioService.listarPorEstado(EstadoOS.RECEPCIONADA)).thenReturn(List.of(recepcionada));

            mockMvc.perform(get("/api/v1/reportes/oss-en-taller"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].numeroOS").value("OS-001"))
                    .andExpect(jsonPath("$[1].numeroOS").value("OS-002"));

            verify(ordenServicioService).listarPorEstado(EstadoOS.EN_PROCESO);
            verify(ordenServicioService).listarPorEstado(EstadoOS.RECEPCIONADA);
        }
    }
}
