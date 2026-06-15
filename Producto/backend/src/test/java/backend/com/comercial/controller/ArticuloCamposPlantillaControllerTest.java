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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
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

    // ============================================================
    // HELPERS
    // ============================================================

    private ArticuloCamposPlantillaDTO crearDTO(Long id,
                                                Integer idArticulo,
                                                Long idPlantilla) {

        ArticuloCamposPlantillaDTO dto = new ArticuloCamposPlantillaDTO();
        dto.setIdModeloPlantilla(id);
        dto.setIdArticulo(idArticulo);
        dto.setIdPlantilla(idPlantilla);

        return dto;
    }

    // ============================================================
    // CONSULTAS GET
    // ============================================================

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("GET /articulo/{idArticulo} retorna las relaciones del artículo")
        void listarPorArticulo() throws Exception {

            List<ArticuloCamposPlantillaDTO> lista = List.of(
                    crearDTO(1L, 10, 100L),
                    crearDTO(2L, 10, 200L)
            );

            when(articuloCamposPlantillaService.listarPorArticulo(10))
                    .thenReturn(lista);

            mockMvc.perform(
                            get("/api/v3/comercial/modelos-plantilla/articulo/10")
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].idModeloPlantilla").value(1))
                    .andExpect(jsonPath("$[0].idArticulo").value(10))
                    .andExpect(jsonPath("$[0].idPlantilla").value(100));

            verify(articuloCamposPlantillaService)
                    .listarPorArticulo(10);
        }

        @Test
        @DisplayName("GET /articulo/{idArticulo} retorna lista vacía")
        void listarPorArticuloVacio() throws Exception {

            when(articuloCamposPlantillaService.listarPorArticulo(99))
                    .thenReturn(List.of());

            mockMvc.perform(
                            get("/api/v3/comercial/modelos-plantilla/articulo/99")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));

            verify(articuloCamposPlantillaService)
                    .listarPorArticulo(99);
        }
    }

    // ============================================================
    // ESCRITURA
    // ============================================================

    @Nested
    @DisplayName("Creación")
    class Creacion {

        @Test
        @DisplayName("POST / crea una nueva relación artículo-plantilla")
        void crear() throws Exception {

            ArticuloCamposPlantillaDTO input =
                    crearDTO(null, 10, 100L);

            ArticuloCamposPlantillaDTO output =
                    crearDTO(1L, 10, 100L);

            when(articuloCamposPlantillaService.crear(any()))
                    .thenReturn(output);

            mockMvc.perform(
                            post("/api/v3/comercial/modelos-plantilla")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(input))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idModeloPlantilla").value(1))
                    .andExpect(jsonPath("$.idArticulo").value(10))
                    .andExpect(jsonPath("$.idPlantilla").value(100));

            verify(articuloCamposPlantillaService)
                    .crear(any(ArticuloCamposPlantillaDTO.class));
        }
    }

    // ============================================================
    // ELIMINACIÓN
    // ============================================================

    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("DELETE /{id} elimina correctamente")
        void eliminar() throws Exception {

            mockMvc.perform(
                            delete("/api/v3/comercial/modelos-plantilla/1")
                    )
                    .andExpect(status().isNoContent());

            verify(articuloCamposPlantillaService)
                    .eliminar(1L);
        }
    }

    // ============================================================
    // EXCEPCIONES
    // ============================================================

    @Nested
    @DisplayName("Control de excepciones")
    class Excepciones {

        @Test
        @DisplayName("POST retorna 400 cuando el servicio lanza IllegalArgumentException")
        void crearConError() throws Exception {

            ArticuloCamposPlantillaDTO dto =
                    crearDTO(null, 10, 100L);

            when(articuloCamposPlantillaService.crear(any()))
                    .thenThrow(
                            new IllegalArgumentException(
                                    "Relación inválida"
                            )
                    );

            mockMvc.perform(
                            post("/api/v3/comercial/modelos-plantilla")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("DELETE retorna error cuando el elemento no existe")
        void eliminarNoExistente() throws Exception {

            doThrow(
                    new IllegalArgumentException(
                            "No existe"
                    )
            ).when(articuloCamposPlantillaService)
                    .eliminar(99L);

            mockMvc.perform(
                            delete("/api/v3/comercial/modelos-plantilla/99")
                    )
                    .andExpect(status().isBadRequest());
        }
    }
}
