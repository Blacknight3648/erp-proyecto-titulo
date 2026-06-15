package backend.com.shared.controller;

import backend.com.shared.application.dto.DireccionDTO;
import backend.com.shared.application.service.DireccionService;
import backend.com.shared.infrastructure.api.DireccionController;
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

@WebMvcTest(controllers = DireccionController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DireccionController (API Web)")
class DireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DireccionService direccionService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private DireccionDTO buildDTO(Long id, String calle, String numero) {
        DireccionDTO dto = new DireccionDTO();
        dto.setDireccionId(id);
        dto.setCalle(calle);
        dto.setNumero(numero);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(direccionService.listarTodos()).thenReturn(List.of(
                    buildDTO(1L, "Calle 1", "123")
            ));

            mockMvc.perform(get("/api/v1/direcciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].calle").value("Calle 1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(direccionService.obtenerPorId(1L)).thenReturn(Optional.of(buildDTO(1L, "Calle 1", "123")));

            mockMvc.perform(get("/api/v1/direcciones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.direccionId").value(1));
        }

        @Test
        void listarPorComuna() throws Exception {
            when(direccionService.listarPorComuna(10L)).thenReturn(List.of(buildDTO(1L, "Calle 1", "123")));

            mockMvc.perform(get("/api/v1/direcciones/comuna/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            DireccionDTO input = new DireccionDTO();
            input.setCalle("Calle Nueva");
            input.setNumero("456");

            DireccionDTO output = buildDTO(1L, "Calle Nueva", "456");
            when(direccionService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/direcciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.direccionId").value(1));
        }

        @Test
        void actualizar() throws Exception {
            DireccionDTO input = new DireccionDTO();
            input.setCalle("Calle Actualizada");
            input.setNumero("789");

            DireccionDTO output = buildDTO(1L, "Calle Actualizada", "789");
            when(direccionService.actualizar(eq(1L), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/direcciones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.calle").value("Calle Actualizada"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/direcciones/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
