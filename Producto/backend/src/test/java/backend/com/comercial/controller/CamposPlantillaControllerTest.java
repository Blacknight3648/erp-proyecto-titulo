package backend.com.comercial.controller;

import backend.com.comercial.application.dto.CamposPlantillaDTO;
import backend.com.comercial.application.service.CamposPlantillaService;
import backend.com.comercial.infrastructure.api.CamposPlantillaController;
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
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CamposPlantillaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CamposPlantillaController (API Web)")
class CamposPlantillaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CamposPlantillaService camposPlantillaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // =====================================================
    // HELPERS
    // =====================================================

    private CamposPlantillaDTO crearDTO(Long id, String nombre) {
        CamposPlantillaDTO dto = new CamposPlantillaDTO();
        dto.setIdPlantilla(id);
        dto.setNombreCampo(nombre);
        return dto;
    }

    // =====================================================
    // CONSULTAS GET
    // =====================================================

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("GET /{id} retorna una plantilla")
        void obtenerPorId() throws Exception {

            CamposPlantillaDTO dto =
                    crearDTO(1L, "Color");

            when(camposPlantillaService.obtenerPorId(1L))
                    .thenReturn(dto);

            mockMvc.perform(
                            get("/api/v3/comercial/plantillas/1")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPlantilla").value(1))
                    .andExpect(jsonPath("$.nombreCampo").value("Color"));

            verify(camposPlantillaService)
                    .obtenerPorId(1L);
        }

        @Test
        @DisplayName("GET / retorna todas las plantillas")
        void listarTodas() throws Exception {

            List<CamposPlantillaDTO> lista = List.of(
                    crearDTO(1L, "Color"),
                    crearDTO(2L, "Talla")
            );

            when(camposPlantillaService.listarTodas())
                    .thenReturn(lista);

            mockMvc.perform(
                            get("/api/v3/comercial/plantillas")
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].nombreCampo").value("Color"))
                    .andExpect(jsonPath("$[1].nombreCampo").value("Talla"));

            verify(camposPlantillaService)
                    .listarTodas();
        }

        @Test
        @DisplayName("GET / retorna lista vacía")
        void listarTodasVacia() throws Exception {

            when(camposPlantillaService.listarTodas())
                    .thenReturn(List.of());

            mockMvc.perform(
                            get("/api/v3/comercial/plantillas")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(0));

            verify(camposPlantillaService)
                    .listarTodas();
        }
    }

    // =====================================================
    // ESCRITURA
    // =====================================================

    @Nested
    @DisplayName("Creación y actualización")
    class Escritura {

        @Test
        @DisplayName("POST / crea una plantilla")
        void crear() throws Exception {

            CamposPlantillaDTO input =
                    crearDTO(null, "Material");

            CamposPlantillaDTO output =
                    crearDTO(1L, "Material");

            when(camposPlantillaService.crear(any()))
                    .thenReturn(output);

            mockMvc.perform(
                            post("/api/v3/comercial/plantillas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(input))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idPlantilla").value(1))
                    .andExpect(jsonPath("$.nombreCampo").value("Material"));

            verify(camposPlantillaService)
                    .crear(any(CamposPlantillaDTO.class));
        }

        @Test
        @DisplayName("PUT /{id} actualiza una plantilla")
        void actualizar() throws Exception {

            CamposPlantillaDTO dto =
                    crearDTO(1L, "Composición");

            when(camposPlantillaService.actualizar(
                    eq(1L),
                    any(CamposPlantillaDTO.class)))
                    .thenReturn(dto);

            mockMvc.perform(
                            put("/api/v3/comercial/plantillas/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPlantilla").value(1))
                    .andExpect(jsonPath("$.nombreCampo").value("Composición"));

            verify(camposPlantillaService)
                    .actualizar(eq(1L), any(CamposPlantillaDTO.class));
        }
    }

    // =====================================================
    // ELIMINACIÓN
    // =====================================================

    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("DELETE elimina correctamente")
        void eliminar() throws Exception {

            mockMvc.perform(
                            delete("/api/v3/comercial/plantillas/1")
                    )
                    .andExpect(status().isNoContent());

            verify(camposPlantillaService)
                    .eliminar(1L);
        }
    }

    // =====================================================
    // EXCEPCIONES
    // =====================================================

    @Nested
    @DisplayName("Manejo de excepciones")
    class Excepciones {

        @Test
        @DisplayName("POST retorna error cuando el servicio falla")
        void crearConError() throws Exception {

            CamposPlantillaDTO dto =
                    crearDTO(null, "Error");

            when(camposPlantillaService.crear(any()))
                    .thenThrow(
                            new IllegalArgumentException(
                                    "Campo inválido"
                            )
                    );

            mockMvc.perform(
                            post("/api/v3/comercial/plantillas")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("PUT retorna error cuando no puede actualizar")
        void actualizarConError() throws Exception {

            CamposPlantillaDTO dto =
                    crearDTO(1L, "Error");

            when(camposPlantillaService.actualizar(
                    eq(1L),
                    any(CamposPlantillaDTO.class)))
                    .thenThrow(
                            new IllegalStateException(
                                    "No se puede actualizar"
                            )
                    );

            mockMvc.perform(
                            put("/api/v3/comercial/plantillas/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("DELETE retorna error cuando el registro no existe")
        void eliminarConError() throws Exception {

            doThrow(
                    new IllegalArgumentException(
                            "No existe"
                    )
            ).when(camposPlantillaService)
                    .eliminar(99L);

            mockMvc.perform(
                            delete("/api/v3/comercial/plantillas/99")
                    )
                    .andExpect(status().isBadRequest());
        }
    }
}
