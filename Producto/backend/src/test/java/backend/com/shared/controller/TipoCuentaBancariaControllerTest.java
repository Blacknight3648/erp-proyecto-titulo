package backend.com.shared.controller;

import backend.com.shared.application.dto.TipoCuentaBancariaDTO;
import backend.com.shared.application.service.TipoCuentaBancariaService;
import backend.com.shared.infrastructure.api.TipoCuentaBancariaController;
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

@WebMvcTest(controllers = TipoCuentaBancariaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TipoCuentaBancariaController (API Web)")
class TipoCuentaBancariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoCuentaBancariaService tipoCuentaBancariaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private TipoCuentaBancariaDTO buildDTO(Integer id, String denominacion) {
        TipoCuentaBancariaDTO dto = new TipoCuentaBancariaDTO();
        dto.setTipoCuentaId(id);
        dto.setDenominacionCuenta(denominacion);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(tipoCuentaBancariaService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "Cuenta Corriente")
            ));

            mockMvc.perform(get("/api/v1/tipos-cuenta-bancaria"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].denominacionCuenta").value("Cuenta Corriente"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(tipoCuentaBancariaService.obtenerPorId(1)).thenReturn(Optional.of(buildDTO(1, "Cuenta Corriente")));

            mockMvc.perform(get("/api/v1/tipos-cuenta-bancaria/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoCuentaId").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            TipoCuentaBancariaDTO input = new TipoCuentaBancariaDTO();
            input.setDenominacionCuenta("Cuenta Vista");

            TipoCuentaBancariaDTO output = buildDTO(2, "Cuenta Vista");
            when(tipoCuentaBancariaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/tipos-cuenta-bancaria")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoCuentaId").value(2));
        }

        @Test
        void actualizar() throws Exception {
            TipoCuentaBancariaDTO input = new TipoCuentaBancariaDTO();
            input.setDenominacionCuenta("Cuenta Ahorro");

            TipoCuentaBancariaDTO output = buildDTO(1, "Cuenta Ahorro");
            when(tipoCuentaBancariaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/tipos-cuenta-bancaria/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.denominacionCuenta").value("Cuenta Ahorro"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/tipos-cuenta-bancaria/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
