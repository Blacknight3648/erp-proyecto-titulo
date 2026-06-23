package backend.com.shared.controller;

import backend.com.shared.application.dto.SubCategoriaTelaDTO;
import backend.com.shared.application.service.SubCategoriaTelaService;
import backend.com.shared.infrastructure.api.SubCategoriaTelaController;
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

@WebMvcTest(controllers = SubCategoriaTelaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("SubCategoriaTelaController (API Web)")
class SubCategoriaTelaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SubCategoriaTelaService subCategoriaTelaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private SubCategoriaTelaDTO buildDTO(Integer id, String codigo, String nombre) {
        SubCategoriaTelaDTO dto = new SubCategoriaTelaDTO();
        dto.setIdSubCategoriaTela(id);
        dto.setCodigoSubCategoriaTela(codigo);
        dto.setNombreSubCategoriaTela(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(subCategoriaTelaService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "SUB1", "Subcategoria 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/subcategorias-tela"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoSubCategoriaTela").value("SUB1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(subCategoriaTelaService.obtenerPorId(1)).thenReturn(buildDTO(1, "SUB1", "Subcategoria 1"));

            mockMvc.perform(get("/api/v3/maestros/subcategorias-tela/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idSubCategoriaTela").value(1));
        }

        @Test
        void listarPorCategoriaTela() throws Exception {
            when(subCategoriaTelaService.listarPorCategoriaTela(10)).thenReturn(List.of(
                    buildDTO(1, "SUB1", "Subcategoria 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/subcategorias-tela/por-categoria-tela/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            SubCategoriaTelaDTO input = new SubCategoriaTelaDTO();
            input.setCodigoSubCategoriaTela("SUB-NEW");
            input.setNombreSubCategoriaTela("Nueva Subcategoria");
            input.setIdCategoriaTela(10);

            SubCategoriaTelaDTO output = buildDTO(1, "SUB-NEW", "Nueva Subcategoria");
            when(subCategoriaTelaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/subcategorias-tela")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idSubCategoriaTela").value(1));
        }

        @Test
        void actualizar() throws Exception {
            SubCategoriaTelaDTO input = new SubCategoriaTelaDTO();
            input.setCodigoSubCategoriaTela("SUB-UPD");
            input.setNombreSubCategoriaTela("Subcategoria Actualizada");
            input.setIdCategoriaTela(10);

            SubCategoriaTelaDTO output = buildDTO(1, "SUB-UPD", "Subcategoria Actualizada");
            when(subCategoriaTelaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/subcategorias-tela/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoSubCategoriaTela").value("SUB-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/subcategorias-tela/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
