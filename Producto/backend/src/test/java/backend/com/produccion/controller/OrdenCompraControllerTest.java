package backend.com.produccion.controller;

import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.service.OrdenCompraService;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.infrastructure.api.OrdenCompraController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrdenCompraController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("OrdenCompraControllerTest (API web)")
class OrdenCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdenCompraService ordenCompraService;

    @MockitoBean
    private OrdenCompraRepository repository;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    @Test
    @DisplayName("Debe obtener una OC por ID")
    void debeObtenerOCPorId() throws Exception {

        OrdenCompraDTO dto = new OrdenCompraDTO();
        dto.setIdOC(1L);

        when(ordenCompraService.obtenerPorId(1L))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/ordenes-compra/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOC").value(1));

        verify(ordenCompraService).obtenerPorId(1L);
    }

    @Test
    @DisplayName("Debe retornar 404 cuando la OC no existe")
    void debeRetornar404SiNoExiste() throws Exception {

        when(ordenCompraService.obtenerPorId(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/ordenes-compra/1"))
                .andExpect(status().isNotFound());

        verify(ordenCompraService).obtenerPorId(1L);
    }

    @Test
    @DisplayName("Debe listar todas las órdenes de compra")
    void debeListarTodas() throws Exception {

        OrdenCompraDTO dto = new OrdenCompraDTO();
        dto.setIdOC(1L);

        when(ordenCompraService.listarTodas())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/ordenes-compra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOC").value(1));

        verify(ordenCompraService).listarTodas();
    }

    @Test
    @DisplayName("Debe listar por estado")
    void debeListarPorEstado() throws Exception {

        OrdenCompraDTO dto = new OrdenCompraDTO();
        dto.setIdOC(1L);

        when(ordenCompraService.listarPorEstado(EstadoOC.EMITIDA))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/v1/ordenes-compra")
                                .param("estado", "EMITIDA")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOC").value(1));

        verify(ordenCompraService).listarPorEstado(EstadoOC.EMITIDA);
    }

    @Test
    @DisplayName("Debe marcar una OC como enviada")
    void debeMarcarEnviada() throws Exception {

        OrdenCompraDTO dto = new OrdenCompraDTO();
        dto.setIdOC(1L);

        when(ordenCompraService.marcarEnviada(1L))
                .thenReturn(dto);

        mockMvc.perform(
                        patch("/api/v1/ordenes-compra/1/enviar")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOC").value(1));

        verify(ordenCompraService).marcarEnviada(1L);
    }

    @Test
    @DisplayName("Debe eliminar una OC")
    void debeEliminar() throws Exception {

        doNothing().when(ordenCompraService).eliminar(1L);

        mockMvc.perform(
                        delete("/api/v1/ordenes-compra/1")
                )
                .andExpect(status().isNoContent());

        verify(ordenCompraService).eliminar(1L);
    }
}