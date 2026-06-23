package backend.com.shared.controller;

import backend.com.shared.application.dto.GramajeTelaDTO;
import backend.com.shared.application.service.GramajeTelaService;
import backend.com.shared.infrastructure.api.GramajeTelaController;
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

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GramajeTelaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("GramajeTelaController (API Web)")
class GramajeTelaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GramajeTelaService gramajeTelaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private GramajeTelaDTO buildDTO(Integer id, String codigo, BigDecimal valor, String categoria) {
        GramajeTelaDTO dto = new GramajeTelaDTO();
        dto.setIdGramaje(id);
        dto.setCodigoGramaje(codigo);
        dto.setValorGramosM2(valor);
        dto.setCategoriaVestuario(categoria);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(gramajeTelaService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "GRM1", new BigDecimal("150.00"), "Camisería")
            ));

            mockMvc.perform(get("/api/v3/maestros/gramajes-tela"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoGramaje").value("GRM1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(gramajeTelaService.obtenerPorId(1)).thenReturn(buildDTO(1, "GRM1", new BigDecimal("150.00"), "Camisería"));

            mockMvc.perform(get("/api/v3/maestros/gramajes-tela/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idGramaje").value(1));
        }

        @Test
        void listarPorCategoria() throws Exception {
            when(gramajeTelaService.listarPorCategoriaVestuario("Camisería")).thenReturn(List.of(
                    buildDTO(1, "GRM1", new BigDecimal("150.00"), "Camisería")
            ));

            mockMvc.perform(get("/api/v3/maestros/gramajes-tela/categoria/Camisería"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            GramajeTelaDTO input = new GramajeTelaDTO();
            input.setCodigoGramaje("GRM-NEW");
            input.setValorGramosM2(new BigDecimal("200.00"));

            GramajeTelaDTO output = buildDTO(1, "GRM-NEW", new BigDecimal("200.00"), "Abrigo");
            when(gramajeTelaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/gramajes-tela")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idGramaje").value(1));
        }

        @Test
        void actualizar() throws Exception {
            GramajeTelaDTO input = new GramajeTelaDTO();
            input.setCodigoGramaje("GRM-UPD");
            input.setValorGramosM2(new BigDecimal("250.00"));

            GramajeTelaDTO output = buildDTO(1, "GRM-UPD", new BigDecimal("250.00"), "Deportivo");
            when(gramajeTelaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/gramajes-tela/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoGramaje").value("GRM-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/gramajes-tela/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
