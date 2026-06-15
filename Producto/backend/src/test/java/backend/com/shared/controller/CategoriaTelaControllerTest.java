package backend.com.shared.controller;

import backend.com.shared.application.dto.CategoriaTelaDTO;
import backend.com.shared.application.service.CategoriaTelaService;
import backend.com.shared.infrastructure.api.CategoriaTelaController;
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

@WebMvcTest(controllers = CategoriaTelaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CategoriaTelaController (API Web)")
class CategoriaTelaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CategoriaTelaService categoriaTelaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private CategoriaTelaDTO buildDTO(Integer id, String codigo, String nombre) {
        CategoriaTelaDTO dto = new CategoriaTelaDTO();
        dto.setIdCategoriaTela(id);
        dto.setCodigoCategoriaTela(codigo);
        dto.setNombreCategoriaTela(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodas() throws Exception {
            when(categoriaTelaService.listarTodas()).thenReturn(List.of(
                    buildDTO(1, "CAT1", "Categoria 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/categorias-tela"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoCategoriaTela").value("CAT1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(categoriaTelaService.obtenerPorId(1)).thenReturn(buildDTO(1, "CAT1", "Categoria 1"));

            mockMvc.perform(get("/api/v3/maestros/categorias-tela/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idCategoriaTela").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            CategoriaTelaDTO input = new CategoriaTelaDTO();
            input.setCodigoCategoriaTela("CAT-NEW");
            input.setNombreCategoriaTela("Nueva Categoria");

            CategoriaTelaDTO output = buildDTO(1, "CAT-NEW", "Nueva Categoria");
            when(categoriaTelaService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/categorias-tela")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idCategoriaTela").value(1));
        }

        @Test
        void actualizar() throws Exception {
            CategoriaTelaDTO input = new CategoriaTelaDTO();
            input.setCodigoCategoriaTela("CAT-UPD");
            input.setNombreCategoriaTela("Categoria Actualizada");

            CategoriaTelaDTO output = buildDTO(1, "CAT-UPD", "Categoria Actualizada");
            when(categoriaTelaService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/categorias-tela/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoCategoriaTela").value("CAT-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/categorias-tela/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
