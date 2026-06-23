package backend.com.shared.controller;

import backend.com.shared.application.dto.RegionDTO;
import backend.com.shared.application.service.RegionService;
import backend.com.shared.infrastructure.api.RegionController;
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

@WebMvcTest(controllers = RegionController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("RegionController (API Web)")
class RegionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegionService regionService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private RegionDTO buildDTO(Long id, String nombre) {
        RegionDTO dto = new RegionDTO();
        dto.setRegionId(id);
        dto.setNombreRegion(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(regionService.listarTodos()).thenReturn(List.of(
                    buildDTO(1L, "Region A"), buildDTO(2L, "Region B")
            ));

            mockMvc.perform(get("/api/v1/regiones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nombreRegion").value("Region A"));
        }

        @Test
        void obtenerPorId_Existe() throws Exception {
            when(regionService.obtenerPorId(1L)).thenReturn(Optional.of(buildDTO(1L, "Region A")));

            mockMvc.perform(get("/api/v1/regiones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.regionId").value(1));
        }

        @Test
        void obtenerPorId_NoExiste() throws Exception {
            when(regionService.obtenerPorId(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/regiones/99"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void listarPorPais() throws Exception {
            when(regionService.listarPorPais(1)).thenReturn(List.of(
                    buildDTO(1L, "Region Pais 1")
            ));

            mockMvc.perform(get("/api/v1/regiones/pais/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            RegionDTO input = new RegionDTO();
            input.setNombreRegion("Region Nueva");

            RegionDTO output = buildDTO(1L, "Region Nueva");
            when(regionService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/regiones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.regionId").value(1));
        }

        @Test
        void actualizar() throws Exception {
            RegionDTO input = new RegionDTO();
            input.setNombreRegion("Region Actualizada");

            RegionDTO output = buildDTO(1L, "Region Actualizada");
            when(regionService.actualizar(eq(1L), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/regiones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreRegion").value("Region Actualizada"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/regiones/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
