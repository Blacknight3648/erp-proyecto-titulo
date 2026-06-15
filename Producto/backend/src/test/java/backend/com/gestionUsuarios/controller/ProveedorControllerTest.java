package backend.com.gestionUsuarios.controller;

import backend.com.gestionUsuarios.application.dto.ProveedorDTO;
import backend.com.gestionUsuarios.application.service.ProveedorService;
import backend.com.gestionUsuarios.domain.model.Proveedor;
import backend.com.gestionUsuarios.infrastructure.api.ProveedorController;
import backend.com.gestionUsuarios.infrastructure.mapper.ProveedorMapper;
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

@WebMvcTest(controllers = ProveedorController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - ProveedorController")
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProveedorService proveedorService;

    @MockitoBean
    private ProveedorMapper proveedorMapper;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private Proveedor crearProveedorMock(Long id, String razonSocial) {
        Proveedor proveedor = new Proveedor();
        proveedor.setProveedorId(id);
        proveedor.setRazonSocialProveedor(razonSocial);
        return proveedor;
    }

    private ProveedorDTO crearProveedorDTOMock(Long id, String razonSocial) {
        ProveedorDTO dto = new ProveedorDTO();
        dto.setProveedorId(id);
        dto.setRazonSocialProveedor(razonSocial);
        dto.setRunProveedor("11111111-1");
        return dto;
    }

    @Nested
    @DisplayName("Consultas GET")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los proveedores")
        void listarTodos() throws Exception {
            Proveedor proveedor = crearProveedorMock(1L, "Proveedor SA");
            ProveedorDTO dto = crearProveedorDTOMock(1L, "Proveedor SA");

            when(proveedorService.listarTodos()).thenReturn(List.of(proveedor));
            when(proveedorMapper.toDTOList(any())).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/proveedores"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].razonSocialProveedor").value("Proveedor SA"));
        }

        @Test
        @DisplayName("Debe obtener proveedor por ID")
        void obtenerPorId() throws Exception {
            Proveedor proveedor = crearProveedorMock(1L, "Proveedor SA");
            ProveedorDTO dto = crearProveedorDTOMock(1L, "Proveedor SA");

            when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.of(proveedor));
            when(proveedorMapper.toDTO(proveedor)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/proveedores/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.razonSocialProveedor").value("Proveedor SA"));
        }

        @Test
        @DisplayName("Debe retornar 404 si proveedor no existe por ID")
        void obtenerPorId_NoExiste() throws Exception {
            when(proveedorService.obtenerPorId(1L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/proveedores/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe buscar por razón social")
        void buscarPorRazonSocial() throws Exception {
            Proveedor proveedor = crearProveedorMock(1L, "Proveedor SA");
            ProveedorDTO dto = crearProveedorDTOMock(1L, "Proveedor SA");

            when(proveedorService.buscarPorRazonSocial("Proveedor")).thenReturn(List.of(proveedor));
            when(proveedorMapper.toDTOList(any())).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/proveedores/razon-social/Proveedor"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].razonSocialProveedor").value("Proveedor SA"));
        }
    }

    @Nested
    @DisplayName("Mutaciones POST/PUT/DELETE")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear un proveedor")
        void crear() throws Exception {
            ProveedorDTO inputDto = crearProveedorDTOMock(null, "Proveedor SA");
            Proveedor proveedorMapped = crearProveedorMock(null, "Proveedor SA");
            Proveedor proveedorSaved = crearProveedorMock(1L, "Proveedor SA");
            ProveedorDTO outputDto = crearProveedorDTOMock(1L, "Proveedor SA");

            when(proveedorMapper.toDomain(any(ProveedorDTO.class))).thenReturn(proveedorMapped);
            when(proveedorService.crear(any(Proveedor.class))).thenReturn(proveedorSaved);
            when(proveedorMapper.toDTO(proveedorSaved)).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/proveedores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.proveedorId").value(1))
                    .andExpect(jsonPath("$.razonSocialProveedor").value("Proveedor SA"));
        }

        @Test
        @DisplayName("Debe actualizar un proveedor")
        void actualizar() throws Exception {
            ProveedorDTO inputDto = crearProveedorDTOMock(1L, "Proveedor Actualizado");
            Proveedor proveedorMapped = crearProveedorMock(1L, "Proveedor Actualizado");
            Proveedor proveedorSaved = crearProveedorMock(1L, "Proveedor Actualizado");
            ProveedorDTO outputDto = crearProveedorDTOMock(1L, "Proveedor Actualizado");

            when(proveedorMapper.toDomain(any(ProveedorDTO.class))).thenReturn(proveedorMapped);
            when(proveedorService.actualizar(eq(1L), any(Proveedor.class))).thenReturn(proveedorSaved);
            when(proveedorMapper.toDTO(proveedorSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/proveedores/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.razonSocialProveedor").value("Proveedor Actualizado"));
        }

        @Test
        @DisplayName("Debe eliminar un proveedor")
        void eliminar() throws Exception {
            doNothing().when(proveedorService).eliminar(1L);

            mockMvc.perform(delete("/api/v1/proveedores/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
