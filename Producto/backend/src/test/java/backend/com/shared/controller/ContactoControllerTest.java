package backend.com.shared.controller;

import backend.com.shared.application.dto.ContactoDTO;
import backend.com.shared.application.service.ContactoService;
import backend.com.shared.infrastructure.api.ContactoController;
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

@WebMvcTest(controllers = ContactoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ContactoController (API Web)")
class ContactoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ContactoService contactoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private ContactoDTO buildDTO(Long id, String nombre, String email) {
        ContactoDTO dto = new ContactoDTO();
        dto.setIdContacto(id);
        dto.setNombreContacto(nombre);
        dto.setEmailContacto(email);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void getAllContactos() throws Exception {
            when(contactoService.getAllContactos()).thenReturn(List.of(
                    buildDTO(1L, "Juan Perez", "juan@test.com")
            ));

            mockMvc.perform(get("/api/v1/contacto"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].nombreContacto").value("Juan Perez"));
        }

        @Test
        void getContactoById() throws Exception {
            when(contactoService.getContactoById(1L)).thenReturn(Optional.of(buildDTO(1L, "Juan Perez", "juan@test.com")));

            mockMvc.perform(get("/api/v1/contacto/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idContacto").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void createContacto() throws Exception {
            ContactoDTO input = new ContactoDTO();
            input.setNombreContacto("Nuevo Contacto");

            ContactoDTO output = buildDTO(1L, "Nuevo Contacto", "nuevo@test.com");
            when(contactoService.createContacto(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/contacto")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idContacto").value(1));
        }

        @Test
        void updateContacto() throws Exception {
            ContactoDTO input = new ContactoDTO();
            input.setNombreContacto("Contacto Actualizado");

            ContactoDTO output = buildDTO(1L, "Contacto Actualizado", "upd@test.com");
            when(contactoService.updateContacto(eq(1L), any())).thenReturn(Optional.of(output));

            mockMvc.perform(put("/api/v1/contacto/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreContacto").value("Contacto Actualizado"));
        }

        @Test
        void deleteContacto() throws Exception {
            mockMvc.perform(delete("/api/v1/contacto/1"))
                    .andExpect(status().isOk());
        }
    }
}
