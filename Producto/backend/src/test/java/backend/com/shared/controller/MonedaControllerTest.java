package backend.com.shared.controller;

import backend.com.shared.application.dto.MonedaDTO;
import backend.com.shared.application.service.MonedaService;
import backend.com.shared.infrastructure.api.MonedaController;
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

@WebMvcTest(controllers = MonedaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("MonedaController (API Web)")
class MonedaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MonedaService monedaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private MonedaDTO buildDTO(Integer id, String codigo, String nombre) {
        MonedaDTO dto = new MonedaDTO();
        dto.setIdMoneda(id);
        dto.setCodigoMoneda(codigo);
        dto.setNombreMoneda(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(monedaService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "CLP", "Peso Chileno")
            ));

            mockMvc.perform(get("/api/v3/maestros/monedas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoMoneda").value("CLP"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(monedaService.obtenerPorId(1)).thenReturn(buildDTO(1, "CLP", "Peso Chileno"));

            mockMvc.perform(get("/api/v3/maestros/monedas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idMoneda").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            MonedaDTO input = new MonedaDTO();
            input.setCodigoMoneda("USD");
            input.setNombreMoneda("Dolar");

            MonedaDTO output = buildDTO(2, "USD", "Dolar");
            when(monedaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/monedas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idMoneda").value(2));
        }

        @Test
        void actualizar() throws Exception {
            MonedaDTO input = new MonedaDTO();
            input.setCodigoMoneda("EUR");
            input.setNombreMoneda("Euro");

            MonedaDTO output = buildDTO(1, "EUR", "Euro");
            when(monedaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/monedas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoMoneda").value("EUR"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/monedas/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
