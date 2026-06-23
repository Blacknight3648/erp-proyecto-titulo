package backend.com.gestionUsuarios.controller;

import backend.com.gestionUsuarios.application.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.application.dto.VendedorDTO;
import backend.com.gestionUsuarios.application.service.VendedorService;
import backend.com.gestionUsuarios.infrastructure.api.VendedorController;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VendedorController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - VendedorController")
class VendedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VendedorService vendedorService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private VendedorDTO crearVendedorDTOMock(Long id, String codigo) {
        VendedorDTO dto = new VendedorDTO();
        dto.setVendedorId(id);
        dto.setCodigoVendedor(codigo);
        dto.setNombreUsuario("Juan");
        dto.setApellidosUsuario("Perez");
        return dto;
    }

    private VendedorCreateDTO crearVendedorCreateDTOMock(String codigo) {
        VendedorCreateDTO dto = new VendedorCreateDTO();
        dto.setUsuarioId(1L);
        dto.setCodigoVendedor(codigo);
        return dto;
    }

    @Nested
    @DisplayName("Consultas GET")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los vendedores")
        void findAll() throws Exception {
            VendedorDTO dto = crearVendedorDTOMock(1L, "VEND-1");

            when(vendedorService.findAll()).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/vendedores"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoVendedor").value("VEND-1"));
        }

        @Test
        @DisplayName("Debe obtener vendedor por ID")
        void findById() throws Exception {
            VendedorDTO dto = crearVendedorDTOMock(1L, "VEND-1");

            when(vendedorService.findById(1L)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/vendedores/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoVendedor").value("VEND-1"));
        }

        @Test
        @DisplayName("Debe obtener vendedor por ID de usuario")
        void findByUsuarioId() throws Exception {
            VendedorDTO dto = crearVendedorDTOMock(1L, "VEND-1");

            when(vendedorService.findByUsuarioId(1L)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/vendedores/usuario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoVendedor").value("VEND-1"));
        }
    }

    @Nested
    @DisplayName("Mutaciones POST/PUT/DELETE")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear un vendedor")
        void create() throws Exception {
            VendedorCreateDTO inputDto = crearVendedorCreateDTOMock("VEND-1");
            VendedorDTO outputDto = crearVendedorDTOMock(1L, "VEND-1");

            when(vendedorService.create(any(VendedorCreateDTO.class))).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/vendedores")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.vendedorId").value(1))
                    .andExpect(jsonPath("$.codigoVendedor").value("VEND-1"));
        }

        @Test
        @DisplayName("Debe actualizar un vendedor")
        void update() throws Exception {
            VendedorCreateDTO inputDto = crearVendedorCreateDTOMock("VEND-UPD");
            VendedorDTO outputDto = crearVendedorDTOMock(1L, "VEND-UPD");

            when(vendedorService.update(eq(1L), any(VendedorCreateDTO.class))).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/vendedores/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.codigoVendedor").value("VEND-UPD"));
        }

        @Test
        @DisplayName("Debe eliminar un vendedor")
        void deleteVendedor() throws Exception {
            doNothing().when(vendedorService).delete(1L);

            mockMvc.perform(delete("/api/v1/vendedores/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
