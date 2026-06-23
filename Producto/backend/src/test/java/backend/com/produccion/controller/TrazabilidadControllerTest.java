package backend.com.produccion.controller;

import backend.com.produccion.application.UseCase.TrazabilidadOPUseCase;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.TrazabilidadOPDTO;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.enums.EstadoOP;
import backend.com.produccion.infrastructure.api.TrazabilidadController;
import backend.com.shared.exception.EntityNotFoundException;
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

@WebMvcTest(controllers = TrazabilidadController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TrazabilidadController (API Web)")
class TrazabilidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrazabilidadOPUseCase trazabilidadOPUseCase;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    @Nested
    @DisplayName("Consultas de Trazabilidad (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET /op/{opId} - Caso correcto: retorna la trazabilidad consolidada de la OP")
        void obtenerPorOP_existente_retornaTrazabilidad() throws Exception {
            HojaCompraDTO hc = HojaCompraDTO.builder()
                    .idHC(1L)
                    .numeroHC("HC-001")
                    .opId(10L)
                    .estado(EstadoHC.APROBADA)
                    .build();

            TrazabilidadOPDTO dto = TrazabilidadOPDTO.builder()
                    .opId(10L)
                    .numeroOP("OP-010")
                    .notaVentaId(5L)
                    .estadoOP(EstadoOP.EN_PROCESO)
                    .hojaCompra(hc)
                    .ordenesCompra(List.of())
                    .recepcionesOC(List.of())
                    .ordenesServicio(List.of())
                    .build();

            when(trazabilidadOPUseCase.ejecutar(10L)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/trazabilidad/op/10"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.opId").value(10L))
                    .andExpect(jsonPath("$.numeroOP").value("OP-010"))
                    .andExpect(jsonPath("$.estadoOP").value("EN_PROCESO"))
                    .andExpect(jsonPath("$.hojaCompra.numeroHC").value("HC-001"));

            verify(trazabilidadOPUseCase).ejecutar(10L);
        }

        @Test
        @DisplayName("GET /op/{opId} - Error/404: lanza EntityNotFoundException si la OP no existe")
        void obtenerPorOP_noExistente_retorna404() throws Exception {
            when(trazabilidadOPUseCase.ejecutar(99L))
                    .thenThrow(new EntityNotFoundException("Orden de Producción no encontrada: 99"));

            mockMvc.perform(get("/api/v1/trazabilidad/op/99"))
                    .andExpect(status().isNotFound());

            verify(trazabilidadOPUseCase).ejecutar(99L);
        }
    }
}
