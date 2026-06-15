package backend.com.shared.controller;

import backend.com.shared.application.dto.BancoDTO;
import backend.com.shared.application.service.BancoService;
import backend.com.shared.infrastructure.api.BancoController;
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

@WebMvcTest(controllers = BancoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("BancoController (API Web)")
class BancoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BancoService bancoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private BancoDTO buildDTO(Integer id, String nombre, String codigo) {
        BancoDTO dto = new BancoDTO();
        dto.setBancoId(id);
        dto.setNombreBanco(nombre);
        dto.setCodigoBanco(codigo);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(bancoService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "Banco A", "BA"), buildDTO(2, "Banco B", "BB")
            ));

            mockMvc.perform(get("/api/v1/bancos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nombreBanco").value("Banco A"));
        }

        @Test
        void obtenerPorId_Existe() throws Exception {
            when(bancoService.obtenerPorId(1)).thenReturn(Optional.of(buildDTO(1, "Banco A", "BA")));

            mockMvc.perform(get("/api/v1/bancos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bancoId").value(1));
        }

        @Test
        void obtenerPorId_NoExiste() throws Exception {
            when(bancoService.obtenerPorId(99)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/bancos/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            BancoDTO input = new BancoDTO();
            input.setNombreBanco("Banco Nuevo");
            input.setCodigoBanco("BNUEVO");

            BancoDTO output = buildDTO(1, "Banco Nuevo", "BNUEVO");
            when(bancoService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/bancos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.bancoId").value(1));
        }

        @Test
        void actualizar() throws Exception {
            BancoDTO input = new BancoDTO();
            input.setNombreBanco("Banco Actualizado");
            input.setCodigoBanco("BACT");

            BancoDTO output = buildDTO(1, "Banco Actualizado", "BACT");
            when(bancoService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/bancos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreBanco").value("Banco Actualizado"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/bancos/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
