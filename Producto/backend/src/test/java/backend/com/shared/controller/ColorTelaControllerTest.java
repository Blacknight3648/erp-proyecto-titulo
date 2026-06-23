package backend.com.shared.controller;

import backend.com.shared.application.dto.ColorTelaDTO;
import backend.com.shared.application.service.ColorTelaService;
import backend.com.shared.infrastructure.api.ColorTelaController;
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

@WebMvcTest(controllers = ColorTelaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ColorTelaController (API Web)")
class ColorTelaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ColorTelaService colorTelaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ColorTelaDTO buildDTO(Integer id, String codigo, String descripcion) {
        ColorTelaDTO dto = new ColorTelaDTO();
        dto.setIdColor(id);
        dto.setCodigoColor(codigo);
        dto.setDescripcionColor(descripcion);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(colorTelaService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "COL1", "Rojo")
            ));

            mockMvc.perform(get("/api/v3/maestros/colores-tela"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoColor").value("COL1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(colorTelaService.obtenerPorId(1)).thenReturn(buildDTO(1, "COL1", "Rojo"));

            mockMvc.perform(get("/api/v3/maestros/colores-tela/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idColor").value(1));
        }

        @Test
        void buscarPorDescripcion() throws Exception {
            when(colorTelaService.buscarPorDescripcion("Rojo")).thenReturn(List.of(
                    buildDTO(1, "COL1", "Rojo")
            ));

            mockMvc.perform(get("/api/v3/maestros/colores-tela/buscar").param("descripcion", "Rojo"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void listarPantone() throws Exception {
            when(colorTelaService.listarPantone()).thenReturn(List.of(
                    buildDTO(1, "COL1", "Rojo")
            ));

            mockMvc.perform(get("/api/v3/maestros/colores-tela/pantone"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            ColorTelaDTO input = new ColorTelaDTO();
            input.setCodigoColor("COL-NEW");
            input.setDescripcionColor("Nuevo Color");

            ColorTelaDTO output = buildDTO(1, "COL-NEW", "Nuevo Color");
            when(colorTelaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/colores-tela")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idColor").value(1));
        }

        @Test
        void actualizar() throws Exception {
            ColorTelaDTO input = new ColorTelaDTO();
            input.setCodigoColor("COL-UPD");
            input.setDescripcionColor("Color Actualizado");

            ColorTelaDTO output = buildDTO(1, "COL-UPD", "Color Actualizado");
            when(colorTelaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/colores-tela/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoColor").value("COL-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/colores-tela/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
