package backend.com.produccion.controller;

import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.infrastructure.api.HojaCompraController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HojaCompraController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("HojaCompraController (API Web)")
class HojaCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HojaCompraService hojaCompraService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // --- HELPER PARA CONSTRUIR DTOs DE PRUEBA ---
    private HojaCompraDTO crearHojaCompraDTOTest(Long id, String numero, Long opId, EstadoHC estado) {
        return HojaCompraDTO.builder()
                .idHC(id)
                .numeroHC(numero)
                .opId(opId)
                .costeoVersionId(200L)
                .estado(estado)
                .fechaGeneracion(LocalDate.now())
                .observaciones("Observación de prueba")
                .items(List.of())
                .build();
    }

    // =========================================================================
    // CONSULTAS / OPERACIONES DE LECTURA (GET)
    // =========================================================================
    @Nested
    @DisplayName("Consultas de Hojas de Compra (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET / - Sin parámetros retorna la lista completa de todas las hojas de compra")
        void listarTodasLasHojas() throws Exception {
            List<HojaCompraDTO> lista = List.of(
                    crearHojaCompraDTOTest(1L, "HC-001", 10L, EstadoHC.BORRADOR),
                    crearHojaCompraDTOTest(2L, "HC-002", 11L, EstadoHC.APROBADA)
            );
            when(hojaCompraService.listarTodas()).thenReturn(lista);

            mockMvc.perform(get("/api/v1/hojas-compra"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].numeroHC").value("HC-001"))
                    .andExpect(jsonPath("$[1].numeroHC").value("HC-002"));

            verify(hojaCompraService).listarTodas();
        }

        @Test
        @DisplayName("GET /?estado=BORRADOR - Retorna la lista filtrada por el estado indicado")
        void listarPorEstado() throws Exception {
            List<HojaCompraDTO> listaFiltrada = List.of(
                    crearHojaCompraDTOTest(1L, "HC-001", 10L, EstadoHC.BORRADOR)
            );
            when(hojaCompraService.listarPorEstado(EstadoHC.BORRADOR)).thenReturn(listaFiltrada);

            mockMvc.perform(get("/api/v1/hojas-compra")
                            .param("estado", "BORRADOR"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].estado").value("BORRADOR"));

            verify(hojaCompraService).listarPorEstado(EstadoHC.BORRADOR);
        }

        @Test
        @DisplayName("GET /{idHC} - Caso correcto: encuentra y retorna la hoja de compra por ID")
        void obtenerPorIdEncontrado() throws Exception {
            HojaCompraDTO dto = crearHojaCompraDTOTest(1L, "HC-100", 10L, EstadoHC.BORRADOR);
            when(hojaCompraService.obtenerPorId(1L)).thenReturn(Optional.of(dto));

            mockMvc.perform(get("/api/v1/hojas-compra/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idHC").value(1L))
                    .andExpect(jsonPath("$.numeroHC").value("HC-100"));
        }

        @Test
        @DisplayName("GET /{idHC} - Error/404: no encuentra la hoja de compra por el ID especificado")
        void obtenerPorIdNoEncontrado() throws Exception {
            when(hojaCompraService.obtenerPorId(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/hojas-compra/99"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("GET /op/{opId} - Caso correcto: encuentra y retorna la hoja de compra asociada a la OP")
        void obtenerPorOpIdEncontrado() throws Exception {
            HojaCompraDTO dto = crearHojaCompraDTOTest(5L, "HC-500", 10L, EstadoHC.BORRADOR);
            when(hojaCompraService.obtenerPorOpId(10L)).thenReturn(Optional.of(dto));

            mockMvc.perform(get("/api/v1/hojas-compra/op/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.opId").value(10L))
                    .andExpect(jsonPath("$.numeroHC").value("HC-500"));
        }
    }

    // =========================================================================
    // ESCRITURA Y MUTACIÓN DE RECURSOS (POST/PATCH)
    // =========================================================================
    @Nested
    @DisplayName("Escritura y Mutación de Hojas de Compra (POST/PATCH)")
    class EscrituraYMutacion {

        @Test
        @DisplayName("POST /generar/{opId} - Genera una hoja de compra desde una OP y retorna 201 CREATED")
        void generarDesdeOP() throws Exception {
            HojaCompraDTO resultadoDto = crearHojaCompraDTOTest(1L, "HC-NUEVA", 10L, EstadoHC.BORRADOR);
            when(hojaCompraService.generarDesdeOP(10L)).thenReturn(resultadoDto);

            mockMvc.perform(post("/api/v1/hojas-compra/generar/10"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idHC").value(1L))
                    .andExpect(jsonPath("$.numeroHC").value("HC-NUEVA"))
                    .andExpect(jsonPath("$.opId").value(10L));
        }

        @Test
        @DisplayName("PATCH /{idHC}/aprobar - Transiciona el estado de la hoja de compra a APROBADA")
        void aprobarHojaCompra() throws Exception {
            HojaCompraDTO aprobadoDto = crearHojaCompraDTOTest(1L, "HC-001", 10L, EstadoHC.APROBADA);
            when(hojaCompraService.aprobar(1L)).thenReturn(aprobadoDto);

            mockMvc.perform(patch("/api/v1/hojas-compra/1/aprobar"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("APROBADA"));
        }

        @Test
        @DisplayName("PATCH /{idHC}/cerrar - Transiciona el estado de la hoja de compra a CERRADA")
        void cerrarHojaCompra() throws Exception {
            HojaCompraDTO cerradoDto = crearHojaCompraDTOTest(1L, "HC-001", 10L, EstadoHC.CERRADA);
            when(hojaCompraService.cerrar(1L)).thenReturn(cerradoDto);

            mockMvc.perform(patch("/api/v1/hojas-compra/1/cerrar"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.estado").value("CERRADA"));
        }
    }
}
