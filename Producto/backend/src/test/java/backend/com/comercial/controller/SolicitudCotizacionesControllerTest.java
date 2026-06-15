package backend.com.comercial.controller;

import backend.com.comercial.application.dto.SolicitudCotizacionesCreateDTO;
import backend.com.comercial.application.dto.SolicitudCotizacionesDTO;
import backend.com.comercial.application.service.SolicitudCotizacionesService;
import backend.com.comercial.infrastructure.api.SolicitudCotizacionesController;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SolicitudCotizacionesController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SolicitudCotizacionesController (API Web)")
class SolicitudCotizacionesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudCotizacionesService service;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // =========================================================
    // HELPERS
    // =========================================================
    private SolicitudCotizacionesDTO buildDTO(Long id, String numero) {
        SolicitudCotizacionesDTO dto = new SolicitudCotizacionesDTO();
        dto.setId(id);
        dto.setNumero(numero);
        return dto;
    }

    // =========================================================
    // CONSULTAS GET
    // =========================================================
    @Nested
    @DisplayName("Consultas de Solicitudes de Cotización (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET /api/v1/solicitudes-cotizaciones - Debe retornar listado completo con 200 OK")
        void listarTodasLasSolicitudesDeCotizacion() throws Exception {

            List<SolicitudCotizacionesDTO> lista = List.of(
                    buildDTO(1L, "SCOT-001"),
                    buildDTO(2L, "SCOT-002")
            );

            when(service.findAll()).thenReturn(lista);

            mockMvc.perform(get("/api/v1/solicitudes-cotizaciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].numero").value("SCOT-001"));
        }

        @Test
        @DisplayName("GET /{id} - Debe retornar cotización cuando existe (200 OK)")
        void obtenerSolicitudCotizacionPorIdExistente() throws Exception {

            when(service.findById(1L))
                    .thenReturn(Optional.of(buildDTO(1L, "SCOT-100")));

            mockMvc.perform(get("/api/v1/solicitudes-cotizaciones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numero").value("SCOT-100"));
        }

        @Test
        @DisplayName("GET /{id} - Debe retornar 404 cuando la cotización no existe")
        void obtenerSolicitudCotizacionPorIdNoExistente() throws Exception {

            when(service.findById(999L))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/solicitudes-cotizaciones/999"))
                    .andExpect(status().isNotFound());
        }
    }

    // =========================================================
    // MUTACIONES POST / PUT / DELETE
    // =========================================================
    @Nested
    @DisplayName("Mutaciones de Solicitudes de Cotización (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        @DisplayName("POST /api/v1/solicitudes-cotizaciones - Debe crear cotización (201 CREATED)")
        void crearSolicitudCotizacionExitosamente() throws Exception {

            SolicitudCotizacionesCreateDTO input = new SolicitudCotizacionesCreateDTO();
            input.setClienteId(1L);
            input.setCantidad(5);
            SolicitudCotizacionesDTO output = buildDTO(10L, "SCOT-NEW");

            when(service.create(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/solicitudes-cotizaciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(10L))
                    .andExpect(jsonPath("$.numero").value("SCOT-NEW"));
        }

        @Test
        @DisplayName("PUT /{id} - Debe actualizar cotización correctamente (200 OK)")
        void actualizarSolicitudCotizacionExitosamente() throws Exception {

            SolicitudCotizacionesCreateDTO input = new SolicitudCotizacionesCreateDTO();
            input.setClienteId(1L);
            input.setCantidad(5);
            SolicitudCotizacionesDTO output = buildDTO(1L, "SCOT-UPD");

            when(service.update(any(), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/solicitudes-cotizaciones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numero").value("SCOT-UPD"));
        }

        @Test
        @DisplayName("DELETE /{id} - Debe eliminar cotización (204 NO CONTENT)")
        void eliminarSolicitudCotizacionExitosamente() throws Exception {

            mockMvc.perform(delete("/api/v1/solicitudes-cotizaciones/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
