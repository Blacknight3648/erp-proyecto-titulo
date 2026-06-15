package backend.com.comercial.controller;

import backend.com.comercial.application.dto.SolicitudCostosCreateDTO;
import backend.com.comercial.application.dto.SolicitudCostosDTO;
import backend.com.comercial.application.service.SolicitudCostosService;

import backend.com.comercial.infrastructure.api.SolicitudCostosController;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SolicitudCostosController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SolicitudCostosController (API Web)")
class SolicitudCostosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SolicitudCostosService solicitudCostosService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // =========================
    // HELPERS
    // =========================

    private SolicitudCostosDTO dto(Long id) {
        SolicitudCostosDTO d = new SolicitudCostosDTO();
        d.setId(id);
        d.setNumero("SCOS-001");
        return d;
    }

    // =========================
    // GET
    // =========================

    @Nested
    class Consultas {

        @Test
        void listarTodasLasSCOS() throws Exception {

            when(solicitudCostosService.findAll())
                    .thenReturn(List.of(dto(1L), dto(2L)));

            mockMvc.perform(get("/api/v1/solicitudes-costos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2));
        }

        @Test
        void buscarPorId() throws Exception {

            when(solicitudCostosService.findById(1L))
                    .thenReturn(Optional.of(dto(1L)));

            mockMvc.perform(get("/api/v1/solicitudes-costos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void busquedaFallida() throws Exception {

            when(solicitudCostosService.findById(99L))
                    .thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/solicitudes-costos/99"))
                    .andExpect(status().isNotFound());
        }
    }

    // =========================
    // POST / PUT
    // =========================

    @Nested
    class Escritura {

        @Test
        void crearSCOS() throws Exception {

            SolicitudCostosCreateDTO input = new SolicitudCostosCreateDTO();
            input.setClienteId(1L);
            input.setArticuloDescripcion("Articulo test");
            input.setCantidad(10);
            input.setGenero("UNISEX");
            SolicitudCostosDTO output = dto(1L);

            when(solicitudCostosService.create(any()))
                    .thenReturn(output);

            mockMvc.perform(post("/api/v1/solicitudes-costos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        void atualizarSCOS() throws Exception {

            SolicitudCostosCreateDTO input = new SolicitudCostosCreateDTO();
            input.setClienteId(1L);
            input.setArticuloDescripcion("Articulo test");
            input.setCantidad(10);
            input.setGenero("UNISEX");
            SolicitudCostosDTO output = dto(1L);

            when(solicitudCostosService.update(eq(1L), any()))
                    .thenReturn(output);

            mockMvc.perform(put("/api/v1/solicitudes-costos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk());
        }
    }

    // =========================
    // DELETE
    // =========================

    @Nested
    class Eliminacion {

        @Test
        void eliminarSCOS() throws Exception {

            mockMvc.perform(delete("/api/v1/solicitudes-costos/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
