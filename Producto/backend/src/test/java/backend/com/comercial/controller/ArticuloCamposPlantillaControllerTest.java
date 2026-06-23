package backend.com.comercial.controller;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.ArticuloCamposPlantillaService;
import backend.com.comercial.infrastructure.api.ArticuloCamposPlantillaController;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticuloCamposPlantillaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ArticuloCamposPlantillaController (API Web)")
class ArticuloCamposPlantillaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ArticuloCamposPlantillaService articuloCamposPlantillaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ArticuloCamposPlantillaDTO dto(Long id, Integer idArticulo, List<String> campos) {
        return ArticuloCamposPlantillaDTO.builder()
                .idModeloPlantilla(id)
                .idArticulo(idArticulo)
                .nombreArticulo("Chaqueta")
                .camposPlantilla(campos)
                .build();
    }

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("GET /articulo/{id} retorna la config del artículo con su lista de campos")
        void obtenerPorArticulo() throws Exception {
            when(articuloCamposPlantillaService.obtenerPorArticulo(10))
                    .thenReturn(Optional.of(dto(1L, 10, List.of("forro", "cuello"))));

            mockMvc.perform(get("/api/v3/comercial/modelos-plantilla/articulo/10"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.idArticulo").value(10))
                    .andExpect(jsonPath("$.camposPlantilla.size()").value(2))
                    .andExpect(jsonPath("$.camposPlantilla[0]").value("forro"));
        }

        @Test
        @DisplayName("GET /articulo/{id} retorna 404 si el artículo no tiene configuración")
        void obtenerPorArticuloNotFound() throws Exception {
            when(articuloCamposPlantillaService.obtenerPorArticulo(99)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v3/comercial/modelos-plantilla/articulo/99"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("Guardado (upsert)")
    class Guardado {

        @Test
        @DisplayName("POST / crea o actualiza la config del artículo")
        void guardar() throws Exception {
            ArticuloCamposPlantillaDTO input = dto(null, 10, List.of("forro", "cuello"));
            when(articuloCamposPlantillaService.guardar(any())).thenReturn(dto(1L, 10, List.of("forro", "cuello")));

            mockMvc.perform(post("/api/v3/comercial/modelos-plantilla")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idArticulo").value(10))
                    .andExpect(jsonPath("$.camposPlantilla[1]").value("cuello"));

            verify(articuloCamposPlantillaService).guardar(any(ArticuloCamposPlantillaDTO.class));
        }

        @Test
        @DisplayName("POST sin idArticulo retorna 400 (validación)")
        void guardarSinArticulo() throws Exception {
            ArticuloCamposPlantillaDTO input = dto(null, null, List.of("forro"));

            mockMvc.perform(post("/api/v3/comercial/modelos-plantilla")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("DELETE /articulo/{id} elimina la config del artículo")
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/comercial/modelos-plantilla/articulo/10"))
                    .andExpect(status().isNoContent());

            verify(articuloCamposPlantillaService).eliminarPorArticulo(10);
        }
    }
}
