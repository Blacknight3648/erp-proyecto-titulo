package backend.com.shared.controller;

import backend.com.shared.application.dto.PaisDTO;
import backend.com.shared.application.service.PaisService;
import backend.com.shared.infrastructure.api.PaisController;
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

@WebMvcTest(controllers = PaisController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PaisController (API Web)")
class PaisControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaisService paisService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private PaisDTO buildDTO(Long id, String nombre) {
        PaisDTO dto = new PaisDTO();
        dto.setIdPais(id);
        dto.setNombrePais(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(paisService.listarTodos()).thenReturn(List.of(
                    buildDTO(1L, "Pais A"), buildDTO(2L, "Pais B")
            ));

            mockMvc.perform(get("/api/v1/paises"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nombrePais").value("Pais A"));
        }

        @Test
        void obtenerPorId_Existe() throws Exception {
            when(paisService.obtenerPorId(1)).thenReturn(Optional.of(buildDTO(1L, "Pais A")));

            mockMvc.perform(get("/api/v1/paises/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPais").value(1));
        }

        @Test
        void obtenerPorId_NoExiste() throws Exception {
            when(paisService.obtenerPorId(99)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/paises/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            PaisDTO input = new PaisDTO();
            input.setNombrePais("Pais Nuevo");

            PaisDTO output = buildDTO(1L, "Pais Nuevo");
            when(paisService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/paises")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idPais").value(1));
        }

        @Test
        void actualizar() throws Exception {
            PaisDTO input = new PaisDTO();
            input.setNombrePais("Pais Actualizado");

            PaisDTO output = buildDTO(1L, "Pais Actualizado");
            when(paisService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/paises/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombrePais").value("Pais Actualizado"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/paises/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
