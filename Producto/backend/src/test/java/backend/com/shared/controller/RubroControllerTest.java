package backend.com.shared.controller;

import backend.com.shared.application.dto.RubroDTO;
import backend.com.shared.application.service.RubroService;
import backend.com.shared.infrastructure.api.RubroController;
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

@WebMvcTest(controllers = RubroController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RubroController (API Web)")
class RubroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RubroService rubroService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private RubroDTO buildDTO(Long id, String nombre) {
        RubroDTO dto = new RubroDTO();
        dto.setRubroId(id);
        dto.setNombreRubro(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void getAllRubros() throws Exception {
            when(rubroService.getAllRubros()).thenReturn(List.of(
                    buildDTO(1L, "Rubro 1")
            ));

            mockMvc.perform(get("/api/v1/rubros"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].nombreRubro").value("Rubro 1"));
        }

        @Test
        void getRubroById() throws Exception {
            when(rubroService.getRubroById(1L)).thenReturn(Optional.of(buildDTO(1L, "Rubro 1")));

            mockMvc.perform(get("/api/v1/rubros/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rubroId").value(1));
        }

        @Test
        void getRubroByNombreRubro() throws Exception {
            when(rubroService.getRubroByNombreRubro("Rubro 1")).thenReturn(Optional.of(buildDTO(1L, "Rubro 1")));

            mockMvc.perform(get("/api/v1/rubros/nombre/Rubro 1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rubroId").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void createRubro() throws Exception {
            RubroDTO input = new RubroDTO();
            input.setNombreRubro("Nuevo Rubro");

            RubroDTO output = buildDTO(1L, "Nuevo Rubro");
            when(rubroService.createRubro(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/rubros")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.rubroId").value(1));
        }

        @Test
        void updateRubro() throws Exception {
            RubroDTO input = new RubroDTO();
            input.setNombreRubro("Rubro Actualizado");

            RubroDTO output = buildDTO(1L, "Rubro Actualizado");
            when(rubroService.updateRubro(eq(1L), any())).thenReturn(Optional.of(output));

            mockMvc.perform(put("/api/v1/rubros/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreRubro").value("Rubro Actualizado"));
        }

        @Test
        void deleteRubro() throws Exception {
            mockMvc.perform(delete("/api/v1/rubros/1"))
                    .andExpect(status().isOk());
        }
    }
}
