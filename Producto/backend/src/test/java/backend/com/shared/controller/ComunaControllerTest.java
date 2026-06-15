package backend.com.shared.controller;

import backend.com.shared.application.dto.ComunaDTO;
import backend.com.shared.application.service.ComunaService;
import backend.com.shared.infrastructure.api.ComunaController;
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

@WebMvcTest(controllers = ComunaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ComunaController (API Web)")
class ComunaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ComunaService comunaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ComunaDTO buildDTO(Long id, String nombre) {
        ComunaDTO dto = new ComunaDTO();
        dto.setComunaId(id);
        dto.setNombreComuna(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(comunaService.listarTodos()).thenReturn(List.of(
                    buildDTO(1L, "Comuna A"), buildDTO(2L, "Comuna B")
            ));

            mockMvc.perform(get("/api/v1/comunas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nombreComuna").value("Comuna A"));
        }

        @Test
        void obtenerPorId_Existe() throws Exception {
            when(comunaService.obtenerPorId(1L)).thenReturn(Optional.of(buildDTO(1L, "Comuna A")));

            mockMvc.perform(get("/api/v1/comunas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.comunaId").value(1));
        }

        @Test
        void obtenerPorId_NoExiste() throws Exception {
            when(comunaService.obtenerPorId(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/comunas/99"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void listarPorRegion() throws Exception {
            when(comunaService.listarPorRegion(1L)).thenReturn(List.of(
                    buildDTO(1L, "Comuna Region 1")
            ));

            mockMvc.perform(get("/api/v1/comunas/region/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            ComunaDTO input = new ComunaDTO();
            input.setNombreComuna("Comuna Nueva");

            ComunaDTO output = buildDTO(1L, "Comuna Nueva");
            when(comunaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/comunas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.comunaId").value(1));
        }

        @Test
        void actualizar() throws Exception {
            ComunaDTO input = new ComunaDTO();
            input.setNombreComuna("Comuna Actualizada");

            ComunaDTO output = buildDTO(1L, "Comuna Actualizada");
            when(comunaService.actualizar(eq(1L), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/comunas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreComuna").value("Comuna Actualizada"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/comunas/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
