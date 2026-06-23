package backend.com.comercial.controller;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;
import backend.com.comercial.application.service.DescripcionPlantillaService;
import backend.com.comercial.infrastructure.api.DescripcionPlantillaController;
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

@WebMvcTest(controllers = DescripcionPlantillaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("DescripcionPlantillaController (API Web)")
class DescripcionPlantillaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DescripcionPlantillaService descripcionPlantillaService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // ==================================================
    // HELPERS
    // ==================================================

    private DescripcionPlantillaDTO crearDTO(
            Long id,
            Long idSCOS,
            Long idPlantilla,
            String descripcion) {

        DescripcionPlantillaDTO dto = new DescripcionPlantillaDTO();
        dto.setIdDescripcionPlantilla(id);
        dto.setIdSCOS(idSCOS);
        dto.setIdPlantilla(idPlantilla);
        dto.setValorDescripcion(descripcion);

        return dto;
    }

    // ==================================================
    // CONSULTAS
    // ==================================================

    @Nested
    @DisplayName("Consultas")
    class Consultas {

        @Test
        @DisplayName("GET listar por SCOS")
        void listarPorSCOS() throws Exception {

            List<DescripcionPlantillaDTO> lista = List.of(
                    crearDTO(1L, 100L, 10L, "Rojo"),
                    crearDTO(2L, 100L, 20L, "XL")
            );

            when(descripcionPlantillaService.listarPorSCOS(100L))
                    .thenReturn(lista);

            mockMvc.perform(
                            get("/api/v3/comercial/scos/100/descripciones")
                    )
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].valorDescripcion").value("Rojo"));

            verify(descripcionPlantillaService)
                    .listarPorSCOS(100L);
        }

        @Test
        @DisplayName("GET obtener por id")
        void obtenerPorId() throws Exception {

            DescripcionPlantillaDTO dto =
                    crearDTO(1L, 100L, 10L, "Azul");

            when(descripcionPlantillaService.obtenerPorId(1L))
                    .thenReturn(dto);

            mockMvc.perform(
                            get("/api/v3/comercial/descripciones-plantilla/1")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idDescripcionPlantilla").value(1))
                    .andExpect(jsonPath("$.valorDescripcion").value("Azul"));

            verify(descripcionPlantillaService)
                    .obtenerPorId(1L);
        }
    }

    // ==================================================
    // ESCRITURA
    // ==================================================

    @Nested
    @DisplayName("Creación y actualización")
    class Escritura {

        @Test
        @DisplayName("POST crear descripción")
        void crear() throws Exception {

            DescripcionPlantillaDTO input =
                    crearDTO(null, 100L, 10L, "Negro");

            DescripcionPlantillaDTO output =
                    crearDTO(1L, 100L, 10L, "Negro");

            when(descripcionPlantillaService.crear(any()))
                    .thenReturn(output);

            mockMvc.perform(
                            post("/api/v3/comercial/scos/100/descripciones")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(input))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idDescripcionPlantilla").value(1))
                    .andExpect(jsonPath("$.idSCOS").value(100))
                    .andExpect(jsonPath("$.valorDescripcion").value("Negro"));

            verify(descripcionPlantillaService)
                    .crear(any(DescripcionPlantillaDTO.class));
        }

        @Test
        @DisplayName("PUT actualizar descripción")
        void actualizar() throws Exception {

            DescripcionPlantillaDTO dto =
                    crearDTO(1L, 100L, 10L, "Verde");

            when(descripcionPlantillaService.actualizar(
                    eq(1L),
                    any(DescripcionPlantillaDTO.class)))
                    .thenReturn(dto);

            mockMvc.perform(
                            put("/api/v3/comercial/descripciones-plantilla/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.valorDescripcion").value("Verde"));

            verify(descripcionPlantillaService)
                    .actualizar(eq(1L), any(DescripcionPlantillaDTO.class));
        }
    }

    // ==================================================
    // ELIMINACIÓN
    // ==================================================

    @Nested
    @DisplayName("Eliminación")
    class Eliminacion {

        @Test
        @DisplayName("DELETE elimina correctamente")
        void eliminar() throws Exception {

            mockMvc.perform(
                            delete("/api/v3/comercial/descripciones-plantilla/1")
                    )
                    .andExpect(status().isNoContent());

            verify(descripcionPlantillaService)
                    .eliminar(1L);
        }
    }

    // ==================================================
    // EXCEPCIONES
    // ==================================================

    @Nested
    @DisplayName("Manejo de excepciones")
    class Excepciones {

        @Test
        @DisplayName("POST retorna error cuando el servicio falla")
        void crearConError() throws Exception {

            DescripcionPlantillaDTO dto =
                    crearDTO(null, 100L, 10L, "Error");

            when(descripcionPlantillaService.crear(any()))
                    .thenThrow(
                            new IllegalArgumentException(
                                    "Datos inválidos"
                            )
                    );

            mockMvc.perform(
                            post("/api/v3/comercial/scos/100/descripciones")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("PUT retorna error cuando no puede actualizar")
        void actualizarConError() throws Exception {

            DescripcionPlantillaDTO dto =
                    crearDTO(1L, 100L, 10L, "Error");

            when(descripcionPlantillaService.actualizar(
                    eq(1L),
                    any(DescripcionPlantillaDTO.class)))
                    .thenThrow(
                            new IllegalStateException(
                                    "No se puede actualizar"
                            )
                    );

            mockMvc.perform(
                            put("/api/v3/comercial/descripciones-plantilla/1")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(objectMapper.writeValueAsString(dto))
                    )
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("DELETE retorna error cuando no existe")
        void eliminarConError() throws Exception {

            doThrow(
                    new IllegalArgumentException(
                            "No existe"
                    )
            ).when(descripcionPlantillaService)
                    .eliminar(99L);

            mockMvc.perform(
                            delete("/api/v3/comercial/descripciones-plantilla/99")
                    )
                    .andExpect(status().isBadRequest());
        }
    }
}