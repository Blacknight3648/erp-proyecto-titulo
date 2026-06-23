package backend.com.shared.controller;

import backend.com.shared.application.dto.FamiliaTelaDTO;
import backend.com.shared.application.service.FamiliaTelaService;
import backend.com.shared.infrastructure.api.FamiliaTelaController;
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

@WebMvcTest(controllers = FamiliaTelaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("FamiliaTelaController (API Web)")
class FamiliaTelaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FamiliaTelaService familiaTelaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private FamiliaTelaDTO buildDTO(Integer id, String codigo, String nombre) {
        FamiliaTelaDTO dto = new FamiliaTelaDTO();
        dto.setIdFamiliaTela(id);
        dto.setCodigoFamilia(codigo);
        dto.setNombreFamilia(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(familiaTelaService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "FAM1", "Familia 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/familias-tela"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoFamilia").value("FAM1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(familiaTelaService.obtenerPorId(1)).thenReturn(buildDTO(1, "FAM1", "Familia 1"));

            mockMvc.perform(get("/api/v3/maestros/familias-tela/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idFamiliaTela").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            FamiliaTelaDTO input = new FamiliaTelaDTO();
            input.setCodigoFamilia("FAM-NEW");
            input.setNombreFamilia("Nueva Familia");

            FamiliaTelaDTO output = buildDTO(1, "FAM-NEW", "Nueva Familia");
            when(familiaTelaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/familias-tela")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idFamiliaTela").value(1));
        }

        @Test
        void actualizar() throws Exception {
            FamiliaTelaDTO input = new FamiliaTelaDTO();
            input.setCodigoFamilia("FAM-UPD");
            input.setNombreFamilia("Familia Actualizada");

            FamiliaTelaDTO output = buildDTO(1, "FAM-UPD", "Familia Actualizada");
            when(familiaTelaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/familias-tela/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoFamilia").value("FAM-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/familias-tela/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
