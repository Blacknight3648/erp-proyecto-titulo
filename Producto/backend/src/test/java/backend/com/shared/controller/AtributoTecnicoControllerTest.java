package backend.com.shared.controller;

import backend.com.shared.application.dto.AtributoTecnicoDTO;
import backend.com.shared.application.service.AtributoTecnicoService;
import backend.com.shared.infrastructure.api.AtributoTecnicoController;
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

@WebMvcTest(controllers = AtributoTecnicoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AtributoTecnicoController (API Web)")
class AtributoTecnicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AtributoTecnicoService atributoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private AtributoTecnicoDTO buildDTO(Integer id, String codigo, String descripcion) {
        AtributoTecnicoDTO dto = new AtributoTecnicoDTO();
        dto.setIdAtributo(id);
        dto.setCodigoAtributo(codigo);
        dto.setDescripcionTecnica(descripcion);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            when(atributoService.listarTodos()).thenReturn(List.of(
                    buildDTO(1, "ATTR1", "Atributo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/atributos-tecnicos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoAtributo").value("ATTR1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            when(atributoService.obtenerPorId(1)).thenReturn(buildDTO(1, "ATTR1", "Atributo 1"));

            mockMvc.perform(get("/api/v3/maestros/atributos-tecnicos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idAtributo").value(1));
        }

        @Test
        void listarPorClasificacion() throws Exception {
            when(atributoService.listarPorClasificacion("RESISTENCIA")).thenReturn(List.of(
                    buildDTO(1, "ATTR1", "Atributo 1")
            ));

            mockMvc.perform(get("/api/v3/maestros/atributos-tecnicos/clasificacion/RESISTENCIA"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            AtributoTecnicoDTO input = new AtributoTecnicoDTO();
            input.setCodigoAtributo("ATTR-NEW");
            input.setDescripcionTecnica("Nuevo Atributo");

            AtributoTecnicoDTO output = buildDTO(1, "ATTR-NEW", "Nuevo Atributo");
            when(atributoService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/atributos-tecnicos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idAtributo").value(1));
        }

        @Test
        void actualizar() throws Exception {
            AtributoTecnicoDTO input = new AtributoTecnicoDTO();
            input.setCodigoAtributo("ATTR-UPD");
            input.setDescripcionTecnica("Atributo Actualizado");

            AtributoTecnicoDTO output = buildDTO(1, "ATTR-UPD", "Atributo Actualizado");
            when(atributoService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/atributos-tecnicos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoAtributo").value("ATTR-UPD"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/atributos-tecnicos/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
