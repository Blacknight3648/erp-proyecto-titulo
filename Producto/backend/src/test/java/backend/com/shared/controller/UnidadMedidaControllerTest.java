package backend.com.shared.controller;

import backend.com.shared.application.dto.UnidadMedidaDTO;
import backend.com.shared.application.service.UnidadMedidaService;
import backend.com.shared.infrastructure.api.UnidadMedidaController;
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

@WebMvcTest(controllers = UnidadMedidaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("UnidadMedidaController (API Web)")
class UnidadMedidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UnidadMedidaService unidadMedidaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private UnidadMedidaDTO buildDTO(Integer id, String nombre, String abreviatura) {
        UnidadMedidaDTO dto = new UnidadMedidaDTO();
        dto.setIdUnidadMedida(id);
        dto.setNombreUnidad(nombre);
        dto.setAbreviatura(abreviatura);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(unidadMedidaService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "Metros", "m")
            ));

            mockMvc.perform(get("/api/v3/maestros/unidades-medida"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].abreviatura").value("m"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(unidadMedidaService.obtenerPorId(1)).thenReturn(buildDTO(1, "Metros", "m"));

            mockMvc.perform(get("/api/v3/maestros/unidades-medida/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idUnidadMedida").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            UnidadMedidaDTO input = new UnidadMedidaDTO();
            input.setNombreUnidad("Kilogramos");
            input.setAbreviatura("kg");

            UnidadMedidaDTO output = buildDTO(2, "Kilogramos", "kg");
            when(unidadMedidaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/unidades-medida")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idUnidadMedida").value(2));
        }

        @Test
        void actualizar() throws Exception {
            UnidadMedidaDTO input = new UnidadMedidaDTO();
            input.setNombreUnidad("Litros");
            input.setAbreviatura("L");

            UnidadMedidaDTO output = buildDTO(1, "Litros", "L");
            when(unidadMedidaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/unidades-medida/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.abreviatura").value("L"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/unidades-medida/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
