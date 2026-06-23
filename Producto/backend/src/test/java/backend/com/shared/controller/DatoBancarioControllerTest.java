package backend.com.shared.controller;

import backend.com.shared.application.dto.DatoBancarioDTO;
import backend.com.shared.application.service.DatoBancarioService;
import backend.com.shared.infrastructure.api.DatoBancarioController;
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

@WebMvcTest(controllers = DatoBancarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DatoBancarioController (API Web)")
class DatoBancarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DatoBancarioService datoBancarioService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private DatoBancarioDTO buildDTO(Integer id, String cuenta) {
        DatoBancarioDTO dto = new DatoBancarioDTO();
        dto.setDatoBancarioId(id);
        dto.setNumeroCuenta(cuenta);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(datoBancarioService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "123456789")
            ));

            mockMvc.perform(get("/api/v1/datos-bancarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].numeroCuenta").value("123456789"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(datoBancarioService.obtenerPorId(1)).thenReturn(Optional.of(buildDTO(1, "123456789")));

            mockMvc.perform(get("/api/v1/datos-bancarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.datoBancarioId").value(1));
        }

        @Test
        void listarPorBanco() throws Exception {
            when(datoBancarioService.listarPorBanco(10)).thenReturn(List.of(buildDTO(1, "123456789")));

            mockMvc.perform(get("/api/v1/datos-bancarios/banco/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            DatoBancarioDTO input = new DatoBancarioDTO();
            input.setNumeroCuenta("NEW-ACC");

            DatoBancarioDTO output = buildDTO(1, "NEW-ACC");
            when(datoBancarioService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/datos-bancarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.datoBancarioId").value(1));
        }

        @Test
        void actualizar() throws Exception {
            DatoBancarioDTO input = new DatoBancarioDTO();
            input.setNumeroCuenta("UPD-ACC");

            DatoBancarioDTO output = buildDTO(1, "UPD-ACC");
            when(datoBancarioService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/datos-bancarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroCuenta").value("UPD-ACC"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/datos-bancarios/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
