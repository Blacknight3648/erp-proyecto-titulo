package backend.com.shared.controller;

import backend.com.shared.application.dto.TipoContactoDTO;
import backend.com.shared.application.service.TipoContactoService;
import backend.com.shared.infrastructure.api.TipoContactoController;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TipoContactoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("TipoContactoController (API Web)")
class TipoContactoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoContactoService tipoContactoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private TipoContactoDTO buildDTO(Long id, String descripcion) {
        TipoContactoDTO dto = new TipoContactoDTO();
        dto.setTipoContactoId(id);
        dto.setDescripcionTipoContacto(descripcion);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void getAllTiposContacto() throws Exception {
            when(tipoContactoService.listarTodos()).thenReturn(List.of(
                    buildDTO(1L, "Email")
            ));

            mockMvc.perform(get("/api/v1/tipo-contacto"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].descripcionTipoContacto").value("Email"));
        }

        @Test
        void getTipoContactoById() throws Exception {
            when(tipoContactoService.obtenerPorId(1L)).thenReturn(Optional.of(buildDTO(1L, "Email")));

            mockMvc.perform(get("/api/v1/tipo-contacto/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoContactoId").value(1));
        }

        @Test
        void getTipoContactoByDescripcionTipoContacto() throws Exception {
            when(tipoContactoService.obtenerPorDescripcionTipoContacto("Email")).thenReturn(Optional.of(buildDTO(1L, "Email")));

            mockMvc.perform(get("/api/v1/tipo-contacto/descripcion/Email"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoContactoId").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void createTipoContacto() throws Exception {
            TipoContactoDTO input = new TipoContactoDTO();
            input.setDescripcionTipoContacto("Teléfono");

            TipoContactoDTO output = buildDTO(2L, "Teléfono");
            when(tipoContactoService.crearTipoContacto(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/tipo-contacto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoContactoId").value(2));
        }

        @Test
        void updateTipoContacto() throws Exception {
            TipoContactoDTO input = new TipoContactoDTO();
            input.setDescripcionTipoContacto("WhatsApp");

            TipoContactoDTO output = buildDTO(1L, "WhatsApp");
            when(tipoContactoService.actualizarTipoContacto(eq(1L), any())).thenReturn(output);

            mockMvc.perform(put("/api/v1/tipo-contacto/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.descripcionTipoContacto").value("WhatsApp"));
        }

        @Test
        void deleteTipoContacto() throws Exception {
            mockMvc.perform(delete("/api/v1/tipo-contacto/1"))
                    .andExpect(status().isOk());
        }
    }
}
