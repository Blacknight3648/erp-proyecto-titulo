package backend.com.gestionUsuarios.controller;

import backend.com.gestionUsuarios.application.dto.CreateUserDTO;
import backend.com.gestionUsuarios.application.dto.UserDTO;
import backend.com.gestionUsuarios.application.service.UserService;
import backend.com.gestionUsuarios.domain.model.User;
import backend.com.gestionUsuarios.infrastructure.api.UsuarioController;
import backend.com.gestionUsuarios.infrastructure.mapper.UserMapper;
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
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - UsuarioController")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private User crearUserMock(Long id, String email) {
        User user = new User();
        user.setUsuarioId(id);
        user.setUsuarioEmail(email);
        user.setEnabled(true);
        return user;
    }

    private UserDTO crearUserDTOMock(Long id, String email) {
        UserDTO dto = new UserDTO();
        dto.setUsuarioId(id);
        dto.setUsuarioEmail(email);
        dto.setEnabled(true);
        return dto;
    }

    private CreateUserDTO crearCreateUserDTOMock(String email) {
        CreateUserDTO dto = new CreateUserDTO();
        dto.setUsuarioEmail(email);
        dto.setUsuarioRun("11111111-1");
        return dto;
    }

    @Nested
    @DisplayName("Consultas GET")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los usuarios")
        void getAll() throws Exception {
            User user = crearUserMock(1L, "test@test.com");
            UserDTO dto = crearUserDTOMock(1L, "test@test.com");

            when(userService.listarUsuarios()).thenReturn(List.of(user));
            when(userMapper.toUserDTO(user)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/usuarios"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].usuarioEmail").value("test@test.com"));
        }

        @Test
        @DisplayName("Debe obtener usuario por ID")
        void getById() throws Exception {
            User user = crearUserMock(1L, "test@test.com");
            UserDTO dto = crearUserDTOMock(1L, "test@test.com");

            when(userService.obtenerUsuario(1L)).thenReturn(user);
            when(userMapper.toUserDTO(user)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/usuarios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.usuarioEmail").value("test@test.com"));
        }
    }

    @Nested
    @DisplayName("Mutaciones POST/PUT/PATCH/DELETE")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear un usuario")
        void create() throws Exception {
            CreateUserDTO inputDto = crearCreateUserDTOMock("nuevo@test.com");
            User userSaved = crearUserMock(1L, "nuevo@test.com");
            UserDTO outputDto = crearUserDTOMock(1L, "nuevo@test.com");

            when(userService.crearUsuario(any(CreateUserDTO.class))).thenReturn(userSaved);
            when(userMapper.toUserDTO(userSaved)).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/usuarios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.usuarioId").value(1))
                    .andExpect(jsonPath("$.usuarioEmail").value("nuevo@test.com"));
        }

        @Test
        @DisplayName("Debe actualizar un usuario")
        void update() throws Exception {
            CreateUserDTO inputDto = crearCreateUserDTOMock("update@test.com");
            User userSaved = crearUserMock(1L, "update@test.com");
            UserDTO outputDto = crearUserDTOMock(1L, "update@test.com");

            when(userService.actualizarUsuario(eq(1L), any(CreateUserDTO.class))).thenReturn(userSaved);
            when(userMapper.toUserDTO(userSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/usuarios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.usuarioEmail").value("update@test.com"));
        }

        @Test
        @DisplayName("Debe eliminar un usuario")
        void deleteUser() throws Exception {
            doNothing().when(userService).eliminarUsuario(1L);

            mockMvc.perform(delete("/api/v1/usuarios/1"))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Debe habilitar/deshabilitar un usuario")
        void toggleEnabled() throws Exception {
            User userSaved = crearUserMock(1L, "test@test.com");
            userSaved.setEnabled(false);
            
            UserDTO outputDto = crearUserDTOMock(1L, "test@test.com");
            outputDto.setEnabled(false);

            when(userService.toggleEnabled(1L)).thenReturn(userSaved);
            when(userMapper.toUserDTO(userSaved)).thenReturn(outputDto);

            mockMvc.perform(patch("/api/v1/usuarios/1/toggle-enabled"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.enabled").value(false));
        }

        @Test
        @DisplayName("Debe asignar roles a un usuario")
        void assignRoles() throws Exception {
            Set<String> roles = Set.of("ADMIN", "USER");
            User userSaved = crearUserMock(1L, "test@test.com");
            UserDTO outputDto = crearUserDTOMock(1L, "test@test.com");

            when(userService.asignarRoles(eq(1L), any())).thenReturn(userSaved);
            when(userMapper.toUserDTO(userSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/usuarios/1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(roles)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Debe asignar áreas a un usuario")
        void assignAreas() throws Exception {
            Set<String> areas = Set.of("TI", "Ventas");
            User userSaved = crearUserMock(1L, "test@test.com");
            UserDTO outputDto = crearUserDTOMock(1L, "test@test.com");

            when(userService.asignarAreas(eq(1L), any())).thenReturn(userSaved);
            when(userMapper.toUserDTO(userSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/usuarios/1/areas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(areas)))
                    .andExpect(status().isOk());
        }
    }
}
