package backend.com.gestionUsuarios.controller;

import backend.com.gestionUsuarios.application.dto.RoleDTO;
import backend.com.gestionUsuarios.application.service.RoleService;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.domain.model.Role;
import backend.com.gestionUsuarios.domain.repository.AreaRepository;
import backend.com.gestionUsuarios.infrastructure.api.RoleController;
import backend.com.gestionUsuarios.infrastructure.mapper.RoleMapper;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - RoleController")
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleService roleService;

    @MockitoBean
    private RoleMapper roleMapper;

    @MockitoBean
    private AreaRepository areaRepository;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.PermisoRepository permisoRepository;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private Role crearRoleMock(Long id, String nombre) {
        Role role = new Role();
        role.setId(id);
        role.setNombre(nombre);
        return role;
    }

    private RoleDTO crearRoleDTOMock(Long id, String nombre) {
        RoleDTO dto = new RoleDTO();
        dto.setId(id);
        dto.setNombre(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas GET")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los roles")
        void listarRoles() throws Exception {
            Role role = crearRoleMock(1L, "ADMIN");
            RoleDTO dto = crearRoleDTOMock(1L, "ADMIN");

            when(roleService.listarRoles()).thenReturn(List.of(role));
            when(roleMapper.toDTOList(any())).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/roles"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].nombre").value("ADMIN"));
        }

        @Test
        @DisplayName("Debe obtener rol por ID")
        void obtenerRole() throws Exception {
            Role role = crearRoleMock(1L, "ADMIN");
            RoleDTO dto = crearRoleDTOMock(1L, "ADMIN");

            when(roleService.obtenerRole(1L)).thenReturn(role);
            when(roleMapper.toDTO(role)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/roles/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("ADMIN"));
        }
    }

    @Nested
    @DisplayName("Mutaciones POST/PUT/PATCH/DELETE")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear un rol")
        void crear() throws Exception {
            RoleDTO inputDto = crearRoleDTOMock(null, "ADMIN");
            Role roleMapped = crearRoleMock(null, "ADMIN");
            Role roleSaved = crearRoleMock(1L, "ADMIN");
            RoleDTO outputDto = crearRoleDTOMock(1L, "ADMIN");

            when(roleMapper.toDomain(any(RoleDTO.class))).thenReturn(roleMapped);
            when(roleService.crearRole(any(Role.class))).thenReturn(roleSaved);
            when(roleMapper.toDTO(roleSaved)).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/roles")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("ADMIN"));
        }

        @Test
        @DisplayName("Debe actualizar un rol")
        void actualizar() throws Exception {
            RoleDTO inputDto = crearRoleDTOMock(1L, "ADMIN Modificado");
            Role roleMapped = crearRoleMock(1L, "ADMIN Modificado");
            Role roleSaved = crearRoleMock(1L, "ADMIN Modificado");
            RoleDTO outputDto = crearRoleDTOMock(1L, "ADMIN Modificado");

            when(roleMapper.toDomain(any(RoleDTO.class))).thenReturn(roleMapped);
            when(roleService.actualizarRole(eq(1L), any(Role.class))).thenReturn(roleSaved);
            when(roleMapper.toDTO(roleSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("ADMIN Modificado"));
        }

        @Test
        @DisplayName("Debe actualizar parcialmente un rol")
        void actualizarParcial() throws Exception {
            RoleDTO inputDto = new RoleDTO();
            inputDto.setNombre("ADMIN Patch");
            inputDto.setAreaId(10L);

            Role existente = crearRoleMock(1L, "ADMIN");
            Role actualizada = crearRoleMock(1L, "ADMIN Patch");
            RoleDTO outputDto = crearRoleDTOMock(1L, "ADMIN Patch");
            Area area = new Area();
            area.setAreaId(10L);

            when(roleService.obtenerRole(1L)).thenReturn(existente);
            when(areaRepository.findById(10L)).thenReturn(Optional.of(area));
            when(roleService.actualizarRole(eq(1L), any(Role.class))).thenReturn(actualizada);
            when(roleMapper.toDTO(actualizada)).thenReturn(outputDto);

            mockMvc.perform(patch("/api/v1/roles/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("ADMIN Patch"));
        }

        @Test
        @DisplayName("Debe eliminar un rol")
        void eliminar() throws Exception {
            doNothing().when(roleService).eliminarRole(1L);

            mockMvc.perform(delete("/api/v1/roles/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
