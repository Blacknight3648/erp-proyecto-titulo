package backend.com.shared.controller;

import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.application.service.ArticuloService;
import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.infrastructure.api.ArticuloController;
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

@WebMvcTest(controllers = ArticuloController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ArticuloController (API Web)")
class ArticuloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ArticuloService articuloService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ArticuloDTO buildDTO(Integer id, String codigo, String nombre) {
        ArticuloDTO dto = new ArticuloDTO();
        dto.setIdArticulo(id);
        dto.setCodigoArticulo(codigo);
        dto.setNombreArticulo(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(articuloService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "ART-01", "Articulo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/articulos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoArticulo").value("ART-01"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(articuloService.obtenerPorId(1)).thenReturn(buildDTO(1, "ART-01", "Articulo 1"));

            mockMvc.perform(get("/api/v3/maestros/articulos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idArticulo").value(1));
        }

        @Test
        void obtenerPorCodigo() throws Exception {
            when(articuloService.obtenerPorCodigo("ART-01")).thenReturn(buildDTO(1, "ART-01", "Articulo 1"));

            mockMvc.perform(get("/api/v3/maestros/articulos/codigo/ART-01"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoArticulo").value("ART-01"));
        }

        @Test
        void listarActivos() throws Exception {
            when(articuloService.listarActivos()).thenReturn(List.of(
                    buildDTO(1, "ART-01", "Articulo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/articulos/activos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void listarPorTipo() throws Exception {
            when(articuloService.listarPorTipo(TipoArticulo.TELA)).thenReturn(List.of(
                    buildDTO(1, "ART-01", "Articulo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/articulos/tipo/TELA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void buscarPorNombre() throws Exception {
            when(articuloService.buscarPorNombre("Articulo")).thenReturn(List.of(
                    buildDTO(1, "ART-01", "Articulo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/articulos/buscar").param("nombre", "Articulo"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            ArticuloDTO input = new ArticuloDTO();
            input.setCodigoArticulo("ART-NEW");
            input.setNombreArticulo("Articulo Nuevo");
            input.setTipoArticulo(TipoArticulo.TELA);
            input.setIdCategoriaTela(1);

            ArticuloDTO output = buildDTO(1, "ART-NEW", "Articulo Nuevo");
            when(articuloService.crearArticulo(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/articulos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idArticulo").value(1));
        }

        @Test
        void actualizar() throws Exception {
            ArticuloDTO input = new ArticuloDTO();
            input.setCodigoArticulo("ART-UPD");
            input.setNombreArticulo("Articulo Actualizado");
            input.setTipoArticulo(TipoArticulo.TELA);
            input.setIdCategoriaTela(1);

            ArticuloDTO output = buildDTO(1, "ART-UPD", "Articulo Actualizado");
            when(articuloService.actualizarArticulo(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/articulos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoArticulo").value("ART-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/articulos/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
