package backend.com.produccion.controller;

import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.dto.RecepcionOCItemDTO;
import backend.com.produccion.application.service.RecepcionOCService;
import backend.com.produccion.infrastructure.api.RecepcionOCController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RecepcionOCController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RecepcionOCController (API Web)")
class RecepcionOCControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecepcionOCService recepcionOCService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // --- HELPER PARA CONSTRUIR DTOs DE PRUEBA ---
    private RecepcionOCDTO crearRecepcionOCDTOTest(Long id, Long ocId) {
        return RecepcionOCDTO.builder()
                .idRecepcion(id)
                .ocId(ocId)
                .fechaRecepcion(LocalDate.now())
                .numeroGuia("GUIA-001")
                .responsable("Bodeguero")
                .items(List.of(RecepcionOCItemDTO.builder()
                        .ocItemId(100L)
                        .cantidadRecibida(new BigDecimal("10"))
                        .cantidadConforme(new BigDecimal("10"))
                        .cantidadRechazada(BigDecimal.ZERO)
                        .build()))
                .build();
    }

    // =========================================================================
    // REGISTRO DE RECEPCIONES (POST)
    // =========================================================================
    @Nested
    @DisplayName("Registro de Recepciones (POST)")
    class RegistroPost {

        @Test
        @DisplayName("POST /oc/{ocId} - Caso correcto: registra la recepción y retorna 201 CREATED")
        void registrar_payloadValido_retorna201() throws Exception {
            RecepcionOCDTO request = crearRecepcionOCDTOTest(null, 10L);
            RecepcionOCDTO response = crearRecepcionOCDTOTest(1L, 10L);

            when(recepcionOCService.registrar(eq(10L), any(RecepcionOCDTO.class))).thenReturn(response);

            mockMvc.perform(post("/api/v1/recepciones-oc/oc/10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idRecepcion").value(1L))
                    .andExpect(jsonPath("$.ocId").value(10L));

            verify(recepcionOCService).registrar(eq(10L), any(RecepcionOCDTO.class));
        }

        @Test
        @DisplayName("POST /oc/{ocId} - Error/400: payload sin items lanza error de validación")
        void registrar_sinItems_retorna400() throws Exception {
            RecepcionOCDTO request = crearRecepcionOCDTOTest(null, 10L);
            request.setItems(List.of());

            mockMvc.perform(post("/api/v1/recepciones-oc/oc/10")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());

            verify(recepcionOCService, never()).registrar(any(), any());
        }
    }

    // =========================================================================
    // CONSULTAS DE RECEPCIONES (GET)
    // =========================================================================
    @Nested
    @DisplayName("Consultas de Recepciones (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET /{idRecepcion} - Caso correcto: encuentra y retorna la recepción por ID")
        void obtenerPorId_existente_retornaRecepcion() throws Exception {
            RecepcionOCDTO dto = crearRecepcionOCDTOTest(1L, 10L);
            when(recepcionOCService.obtenerPorId(1L)).thenReturn(Optional.of(dto));

            mockMvc.perform(get("/api/v1/recepciones-oc/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idRecepcion").value(1L))
                    .andExpect(jsonPath("$.ocId").value(10L));

            verify(recepcionOCService).obtenerPorId(1L);
        }

        @Test
        @DisplayName("GET /{idRecepcion} - Error/404: no encuentra la recepción por el ID especificado")
        void obtenerPorId_noExistente_retorna404() throws Exception {
            when(recepcionOCService.obtenerPorId(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/recepciones-oc/99"))
                    .andExpect(status().isNotFound());

            verify(recepcionOCService).obtenerPorId(99L);
        }

        @Test
        @DisplayName("GET /oc/{ocId} - Retorna la lista de recepciones asociadas a la OC")
        void listarPorOC_retornaListaDeRecepciones() throws Exception {
            List<RecepcionOCDTO> lista = List.of(
                    crearRecepcionOCDTOTest(1L, 10L),
                    crearRecepcionOCDTOTest(2L, 10L)
            );
            when(recepcionOCService.listarPorOC(10L)).thenReturn(lista);

            mockMvc.perform(get("/api/v1/recepciones-oc/oc/10"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].idRecepcion").value(1L))
                    .andExpect(jsonPath("$[1].idRecepcion").value(2L));

            verify(recepcionOCService).listarPorOC(10L);
        }
    }
}
