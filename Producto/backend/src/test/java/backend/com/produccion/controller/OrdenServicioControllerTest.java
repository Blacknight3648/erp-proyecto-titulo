package backend.com.produccion.controller;

import backend.com.produccion.application.dto.DespachoOSDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOSDTO;
import backend.com.produccion.application.service.OrdenServicioService;
import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.domain.repository.OrdenServicioRepository;
import backend.com.produccion.infrastructure.api.OrdenServicioController;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrdenServicioController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("OrdenServicioControllerTest (API web)")
public class OrdenServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrdenServicioService ordenServicioService;

    @MockitoBean
    private OrdenServicioRepository repository;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    //CRUD

    //LISTAR LAS ÓRDENES DE SERVICIO POR ID

    @Test
    @DisplayName("Debe listar todas las órdenes de servicio")
    void debeListarTodas() throws Exception {

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.listarTodas())
                .thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/ordenes-servicio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOS").value(1));

        verify(ordenServicioService).listarTodas();
    }

    @Test
    @DisplayName("Debe obtener una Orden de Servicio por ID")
    void debeObtenerOrdenServicioPorId() throws Exception {

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.obtenerPorId(1L))
                .thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/v1/ordenes-servicio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOS").value(1));

        verify(ordenServicioService).obtenerPorId(1L);
    }

    //ESTADO DE EXCEPCIÓN

    @Test
    @DisplayName("Debe retornar 404 cuando la Orden de Servicio no existe")
    void debeRetornar404SiNoExiste() throws Exception {

        when(ordenServicioService.obtenerPorId(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/ordenes-servicio/1"))
                .andExpect(status().isNotFound());

        verify(ordenServicioService).obtenerPorId(1L);
    }

    @Test
    @DisplayName("Debe listar órdenes de servicio por estado")
    void debeListarPorEstado() throws Exception {

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.listarPorEstado(EstadoOS.EMITIDA))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/v1/ordenes-servicio")
                                .param("estado", "EMITIDA")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOS").value(1));

        verify(ordenServicioService).listarPorEstado(EstadoOS.EMITIDA);
    }

    @Test
    @DisplayName("Debe listar órdenes de servicio por OP")
    void debeListarPorOP() throws Exception {

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.listarPorOP(10L))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/v1/ordenes-servicio")
                                .param("opId", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOS").value(1));

        verify(ordenServicioService).listarPorOP(10L);
    }

    @Test
    @DisplayName("Debe listar órdenes de servicio por proveedor")
    void debeListarPorProveedor() throws Exception {

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.listarPorProveedor(5L))
                .thenReturn(List.of(dto));

        mockMvc.perform(
                        get("/api/v1/ordenes-servicio")
                                .param("proveedorId", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idOS").value(1));

        verify(ordenServicioService).listarPorProveedor(5L);
    }

    @Test
    @DisplayName("Debe registrar un despacho")
    void debeRegistrarDespacho() throws Exception {

        DespachoOSDTO despacho = new DespachoOSDTO();

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.registrarDespacho(eq(1L), any(DespachoOSDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(
                        post("/api/v1/ordenes-servicio/1/despachos")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(despacho)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOS").value(1));

        verify(ordenServicioService)
                .registrarDespacho(eq(1L), any(DespachoOSDTO.class));
    }

    @Test
    @DisplayName("Debe registrar una recepción")
    void debeRegistrarRecepcion() throws Exception {

        RecepcionOSDTO recepcion = new RecepcionOSDTO();

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.registrarRecepcion(eq(1L), any(RecepcionOSDTO.class)))
                .thenReturn(dto);

        mockMvc.perform(
                        post("/api/v1/ordenes-servicio/1/recepciones")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(recepcion)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idOS").value(1));

        verify(ordenServicioService)
                .registrarRecepcion(eq(1L), any(RecepcionOSDTO.class));
    }

    @Test
    @DisplayName("Debe cerrar una Orden de Servicio")
    void debeCerrarOrdenServicio() throws Exception {

        OrdenServicioDTO dto = new OrdenServicioDTO();
        dto.setIdOS(1L);

        when(ordenServicioService.cerrar(1L))
                .thenReturn(dto);

        mockMvc.perform(
                        patch("/api/v1/ordenes-servicio/1/cerrar"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idOS").value(1));

        verify(ordenServicioService).cerrar(1L);
    }

}
