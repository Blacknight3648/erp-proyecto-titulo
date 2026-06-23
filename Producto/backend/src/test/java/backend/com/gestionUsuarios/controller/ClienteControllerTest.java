package backend.com.gestionUsuarios.controller;

import backend.com.gestionUsuarios.application.dto.ClienteDTO;
import backend.com.gestionUsuarios.application.service.ClienteService;
import backend.com.gestionUsuarios.domain.model.Cliente;
import backend.com.gestionUsuarios.infrastructure.api.ClienteController;
import backend.com.gestionUsuarios.infrastructure.mapper.ClienteMapper;
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

@WebMvcTest(controllers = ClienteController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - ClienteController")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private ClienteMapper clienteMapper;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private Cliente crearClienteMock(Long id, String razonSocial) {
        Cliente cliente = new Cliente();
        cliente.setClienteId(id);
        cliente.setRazonSocial(razonSocial);
        return cliente;
    }

    private ClienteDTO crearClienteDTOMock(Long id, String razonSocial) {
        ClienteDTO dto = new ClienteDTO();
        dto.setClienteId(id);
        dto.setRazonSocial(razonSocial);
        dto.setRunCliente("11111111-1");
        return dto;
    }

    @Nested
    @DisplayName("Consultas GET")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todos los clientes")
        void listarTodos() throws Exception {
            Cliente cliente = crearClienteMock(1L, "Empresa SA");
            ClienteDTO dto = crearClienteDTOMock(1L, "Empresa SA");

            when(clienteService.listarTodos()).thenReturn(List.of(cliente));
            when(clienteMapper.toDTOList(any())).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/clientes"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].razonSocial").value("Empresa SA"));
        }

        @Test
        @DisplayName("Debe obtener cliente por ID")
        void obtenerPorId() throws Exception {
            Cliente cliente = crearClienteMock(1L, "Empresa SA");
            ClienteDTO dto = crearClienteDTOMock(1L, "Empresa SA");

            when(clienteService.obtenerPorId(1L)).thenReturn(Optional.of(cliente));
            when(clienteMapper.toDTO(cliente)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/clientes/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.razonSocial").value("Empresa SA"));
        }

        @Test
        @DisplayName("Debe retornar 404 si cliente no existe por ID")
        void obtenerPorId_NoExiste() throws Exception {
            when(clienteService.obtenerPorId(1L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/clientes/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Debe buscar por razón social")
        void buscarPorRazonSocial() throws Exception {
            Cliente cliente = crearClienteMock(1L, "Empresa SA");
            ClienteDTO dto = crearClienteDTOMock(1L, "Empresa SA");

            when(clienteService.buscarPorRazonSocial("Empresa")).thenReturn(List.of(cliente));
            when(clienteMapper.toDTOList(any())).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/clientes/razon-social/Empresa"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].razonSocial").value("Empresa SA"));
        }
    }

    @Nested
    @DisplayName("Mutaciones POST/PUT/DELETE")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear un cliente")
        void crear() throws Exception {
            ClienteDTO inputDto = crearClienteDTOMock(null, "Empresa SA");
            Cliente clienteMapped = crearClienteMock(null, "Empresa SA");
            Cliente clienteSaved = crearClienteMock(1L, "Empresa SA");
            ClienteDTO outputDto = crearClienteDTOMock(1L, "Empresa SA");

            when(clienteMapper.toDomain(any(ClienteDTO.class))).thenReturn(clienteMapped);
            when(clienteService.crear(any(Cliente.class))).thenReturn(clienteSaved);
            when(clienteMapper.toDTO(clienteSaved)).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/clientes")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.clienteId").value(1))
                    .andExpect(jsonPath("$.razonSocial").value("Empresa SA"));
        }

        @Test
        @DisplayName("Debe actualizar un cliente")
        void actualizar() throws Exception {
            ClienteDTO inputDto = crearClienteDTOMock(1L, "Empresa Actualizada");
            Cliente clienteMapped = crearClienteMock(1L, "Empresa Actualizada");
            Cliente clienteSaved = crearClienteMock(1L, "Empresa Actualizada");
            ClienteDTO outputDto = crearClienteDTOMock(1L, "Empresa Actualizada");

            when(clienteMapper.toDomain(any(ClienteDTO.class))).thenReturn(clienteMapped);
            when(clienteService.actualizar(eq(1L), any(Cliente.class))).thenReturn(clienteSaved);
            when(clienteMapper.toDTO(clienteSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/clientes/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.razonSocial").value("Empresa Actualizada"));
        }

        @Test
        @DisplayName("Debe eliminar un cliente")
        void eliminar() throws Exception {
            doNothing().when(clienteService).eliminar(1L);

            mockMvc.perform(delete("/api/v1/clientes/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
