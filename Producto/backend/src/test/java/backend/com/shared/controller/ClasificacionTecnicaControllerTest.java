package backend.com.shared.controller;

import backend.com.shared.application.dto.ClasificacionTecnicaDTO;
import backend.com.shared.application.service.ClasificacionTecnicaService;
import backend.com.shared.infrastructure.api.ClasificacionTecnicaController;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClasificacionTecnicaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ClasificacionTecnicaController (API Web)")
class ClasificacionTecnicaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClasificacionTecnicaService clasificacionService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ClasificacionTecnicaDTO buildDTO(Integer id, String nombre) {
        ClasificacionTecnicaDTO dto = new ClasificacionTecnicaDTO();
        dto.setIdClasificacionTecnica(id);
        dto.setNombreClasificacion(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(clasificacionService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "Clasificacion 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/clasificaciones-tecnicas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].nombreClasificacion").value("Clasificacion 1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(clasificacionService.obtenerPorId(1)).thenReturn(buildDTO(1, "Clasificacion 1"));

            mockMvc.perform(get("/api/v3/maestros/clasificaciones-tecnicas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idClasificacionTecnica").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            ClasificacionTecnicaDTO input = new ClasificacionTecnicaDTO();
            input.setNombreClasificacion("Nueva Clasificacion");

            ClasificacionTecnicaDTO output = buildDTO(1, "Nueva Clasificacion");
            when(clasificacionService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/clasificaciones-tecnicas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idClasificacionTecnica").value(1));
        }

        @Test
        void actualizar() throws Exception {
            ClasificacionTecnicaDTO input = new ClasificacionTecnicaDTO();
            input.setNombreClasificacion("Clasificacion Actualizada");

            ClasificacionTecnicaDTO output = buildDTO(1, "Clasificacion Actualizada");
            when(clasificacionService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/clasificaciones-tecnicas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreClasificacion").value("Clasificacion Actualizada"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/clasificaciones-tecnicas/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
