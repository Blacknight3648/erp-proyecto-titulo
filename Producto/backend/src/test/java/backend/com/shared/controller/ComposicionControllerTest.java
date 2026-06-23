package backend.com.shared.controller;

import backend.com.shared.application.dto.ComposicionDTO;
import backend.com.shared.application.service.ComposicionService;
import backend.com.shared.infrastructure.api.ComposicionController;
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

@WebMvcTest(controllers = ComposicionController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ComposicionController (API Web)")
class ComposicionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ComposicionService composicionService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ComposicionDTO buildDTO(Integer id, String codigo, String descripcion) {
        ComposicionDTO dto = new ComposicionDTO();
        dto.setIdComposicion(id);
        dto.setCodigoComposicion(codigo);
        dto.setDescripcionComposicion(descripcion);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(composicionService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "COMP1", "Composicion 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/composiciones"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoComposicion").value("COMP1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(composicionService.obtenerPorId(1)).thenReturn(buildDTO(1, "COMP1", "Composicion 1"));

            mockMvc.perform(get("/api/v3/maestros/composiciones/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idComposicion").value(1));
        }

        @Test
        void listarPorClasificacion() throws Exception {
            when(composicionService.listarPorClasificacion("NATURAL")).thenReturn(List.of(
                    buildDTO(1, "COMP1", "Composicion 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/composiciones/clasificacion/NATURAL"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            ComposicionDTO input = new ComposicionDTO();
            input.setCodigoComposicion("COMP-NEW");
            input.setDescripcionComposicion("Nueva Composicion");

            ComposicionDTO output = buildDTO(1, "COMP-NEW", "Nueva Composicion");
            when(composicionService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/composiciones")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idComposicion").value(1));
        }

        @Test
        void actualizar() throws Exception {
            ComposicionDTO input = new ComposicionDTO();
            input.setCodigoComposicion("COMP-UPD");
            input.setDescripcionComposicion("Composicion Actualizada");

            ComposicionDTO output = buildDTO(1, "COMP-UPD", "Composicion Actualizada");
            when(composicionService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/composiciones/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoComposicion").value("COMP-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/composiciones/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
