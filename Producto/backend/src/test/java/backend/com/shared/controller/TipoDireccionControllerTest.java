package backend.com.shared.controller;

import backend.com.shared.application.dto.TipoDireccionDTO;
import backend.com.shared.application.service.TipoDireccionService;
import backend.com.shared.infrastructure.api.TipoDireccionController;
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

@WebMvcTest(controllers = TipoDireccionController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TipoDireccionController (API Web)")
class TipoDireccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoDireccionService tipoDireccionService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private TipoDireccionDTO buildDTO(Integer id, String descripcion) {
        TipoDireccionDTO dto = new TipoDireccionDTO();
        dto.setTipoDireccionId(id);
        dto.setDescripcion(descripcion);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(tipoDireccionService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "Comercial")
            ));

            mockMvc.perform(get("/api/v1/tipos-direccion"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].descripcion").value("Comercial"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(tipoDireccionService.obtenerPorId(1)).thenReturn(Optional.of(buildDTO(1, "Comercial")));

            mockMvc.perform(get("/api/v1/tipos-direccion/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoDireccionId").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            TipoDireccionDTO input = new TipoDireccionDTO();
            input.setDescripcion("Bodega");

            TipoDireccionDTO output = buildDTO(2, "Bodega");
            when(tipoDireccionService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/tipos-direccion")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoDireccionId").value(2));
        }

        @Test
        void actualizar() throws Exception {
            TipoDireccionDTO input = new TipoDireccionDTO();
            input.setDescripcion("Sucursal");

            TipoDireccionDTO output = buildDTO(1, "Sucursal");
            when(tipoDireccionService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/tipos-direccion/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.descripcion").value("Sucursal"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/tipos-direccion/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
