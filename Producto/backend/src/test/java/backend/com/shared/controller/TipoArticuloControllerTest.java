package backend.com.shared.controller;

import backend.com.shared.application.dto.TipoArticuloDTO;
import backend.com.shared.application.service.TipoArticuloService;
import backend.com.shared.infrastructure.api.TipoArticuloController;
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

@WebMvcTest(controllers = TipoArticuloController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TipoArticuloController (API Web)")
class TipoArticuloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoArticuloService tipoArticuloService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private TipoArticuloDTO buildDTO(Integer id, String codigo, String nombre) {
        TipoArticuloDTO dto = new TipoArticuloDTO();
        dto.setIdTipoArticulo(id);
        dto.setCodigo(codigo);
        dto.setNombre(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(tipoArticuloService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "TA1", "Tipo Articulo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/tipos-articulo"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigo").value("TA1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(tipoArticuloService.obtenerPorId(1)).thenReturn(buildDTO(1, "TA1", "Tipo Articulo 1"));

            mockMvc.perform(get("/api/v3/maestros/tipos-articulo/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idTipoArticulo").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            TipoArticuloDTO input = new TipoArticuloDTO();
            input.setIdTipoArticulo(1);
            input.setCodigo("TA-NEW");
            input.setNombre("Nuevo Tipo Articulo");

            TipoArticuloDTO output = buildDTO(1, "TA-NEW", "Nuevo Tipo Articulo");
            when(tipoArticuloService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/tipos-articulo")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idTipoArticulo").value(1));
        }

        @Test
        void actualizar() throws Exception {
            TipoArticuloDTO input = new TipoArticuloDTO();
            input.setIdTipoArticulo(1);
            input.setCodigo("TA-UPD");
            input.setNombre("Tipo Articulo Actualizado");

            TipoArticuloDTO output = buildDTO(1, "TA-UPD", "Tipo Articulo Actualizado");
            when(tipoArticuloService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/tipos-articulo/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigo").value("TA-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/tipos-articulo/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
