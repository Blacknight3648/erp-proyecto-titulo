package backend.com.produccion.controller;

import backend.com.produccion.application.UseCase.GestionarOrdenTrabajoUseCase;
import backend.com.produccion.application.UseCase.RegistrarAvanceUseCase;
import backend.com.produccion.application.dto.RegistrarAvanceCommand;
import backend.com.produccion.application.dto.RegistroAvanceDTO;
import backend.com.produccion.domain.enums.EstadoOT;
import backend.com.produccion.domain.enums.FaseProduccion;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import backend.com.produccion.infrastructure.api.OrdenTrabajoController;
import backend.com.shared.valueobjects.DocumentNumber;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrdenTrabajoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("OrdenTrabajoController (API Web)")
class OrdenTrabajoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GestionarOrdenTrabajoUseCase gestionarOTUseCase;

    @MockitoBean
    private RegistrarAvanceUseCase registrarAvanceUseCase;

    @MockitoBean
    private OrdenTrabajoRepository repository;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // Helper para simular OrdenTrabajo stubbeando getters clave usados por el mapeador OTResponse
    private OrdenTrabajo crearOTMock(Long id, String numero, Long opId, EstadoOT estado, FaseProduccion fase) {
        OrdenTrabajo ot = mock(OrdenTrabajo.class);
        when(ot.getIdOT()).thenReturn(id);
        when(ot.getNumeroOT()).thenReturn(numero != null ? new DocumentNumber(numero) : null);
        when(ot.getOrdenProduccionId()).thenReturn(opId);
        when(ot.getEstadoOT()).thenReturn(estado);
        when(ot.getFase()).thenReturn(fase);
        return ot;
    }

    // =========================================================================
    // CONSULTAS / OPERACIONES DE LECTURA (GET)
    // =========================================================================
    @Nested
    @DisplayName("Consultas de Ordenes de Trabajo (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET /nota-venta/{nvId} - Retorna las OTs asociadas a la Nota de Venta")
        void getByNotaVenta_retornaListaDeOTs() throws Exception {
            OrdenTrabajo ot1 = crearOTMock(1L, "OT-001", 10L, EstadoOT.PENDIENTE, null);
            when(gestionarOTUseCase.listarPorNotaVenta(5L)).thenReturn(List.of(ot1));

            mockMvc.perform(get("/api/v1/produccion/ordenes-trabajo/nota-venta/5"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].idOT").value(1L))
                    .andExpect(jsonPath("$[0].numeroOT").value("OT-001"));

            verify(gestionarOTUseCase).listarPorNotaVenta(5L);
        }

        @Test
        @DisplayName("GET /orden-produccion/{opId} - Retorna las OTs (todas las fases) de una OP")
        void getByOrdenProduccion_retornaListaDeOTs() throws Exception {
            OrdenTrabajo ot1 = crearOTMock(1L, "OT-CORTE", 10L, EstadoOT.EN_PROCESO, FaseProduccion.CORTE);
            OrdenTrabajo ot2 = crearOTMock(2L, "OT-CONFECCION", 10L, EstadoOT.PENDIENTE, FaseProduccion.CONFECCION);
            when(repository.findByOrdenProduccionId(10L)).thenReturn(List.of(ot1, ot2));

            mockMvc.perform(get("/api/v1/produccion/ordenes-trabajo/orden-produccion/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].numeroOT").value("OT-CORTE"))
                    .andExpect(jsonPath("$[0].fase").value("CORTE"))
                    .andExpect(jsonPath("$[1].numeroOT").value("OT-CONFECCION"))
                    .andExpect(jsonPath("$[1].fase").value("CONFECCION"));

            verify(repository).findByOrdenProduccionId(10L);
        }

        @Test
        @DisplayName("GET /{id}/avances - Retorna la lista de avances registrados para una OT")
        void listarAvances_retornaListaDeAvances() throws Exception {
            RegistroAvanceDTO avance = new RegistroAvanceDTO();
            avance.setId(1L);
            avance.setOrdenTrabajoId(1L);
            avance.setCantidadProducida(10);
            avance.setCantidadMerma(0);
            avance.setUsuario("operador1");
            avance.setFecha(LocalDateTime.now());

            when(registrarAvanceUseCase.listarPorOT(1L)).thenReturn(List.of(avance));

            mockMvc.perform(get("/api/v1/produccion/ordenes-trabajo/1/avances"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].ordenTrabajoId").value(1L))
                    .andExpect(jsonPath("$[0].cantidadProducida").value(10))
                    .andExpect(jsonPath("$[0].usuario").value("operador1"));

            verify(registrarAvanceUseCase).listarPorOT(1L);
        }
    }

    // =========================================================================
    // MUTACIONES / OPERACIONES DE ESCRITURA (POST)
    // =========================================================================
    @Nested
    @DisplayName("Mutaciones de Ordenes de Trabajo (POST)")
    class MutacionesPost {

        @Test
        @DisplayName("POST /{id}/iniciar - Cambia el estado de la OT a EN_PROCESO")
        void iniciar_retornaOk() throws Exception {
            mockMvc.perform(post("/api/v1/produccion/ordenes-trabajo/1/iniciar"))
                    .andExpect(status().isOk());

            verify(gestionarOTUseCase).iniciarOT(1L);
        }

        @Test
        @DisplayName("POST /{id}/finalizar - Finaliza la OT manualmente")
        void finalizar_retornaOk() throws Exception {
            mockMvc.perform(post("/api/v1/produccion/ordenes-trabajo/1/finalizar"))
                    .andExpect(status().isOk());

            verify(gestionarOTUseCase).finalizarOT(1L);
        }

        @Test
        @DisplayName("POST /{id}/avance - Caso correcto: registra el avance y retorna el DTO resultante")
        void registrarAvance_payloadValido_retornaAvanceRegistrado() throws Exception {
            RegistrarAvanceCommand command = new RegistrarAvanceCommand();
            command.setCantidadProducida(15);
            command.setCantidadMerma(2);
            command.setMotivoMerma("Tela defectuosa");
            command.setUsuario("operador1");

            RegistroAvanceDTO resultado = new RegistroAvanceDTO();
            resultado.setId(1L);
            resultado.setOrdenTrabajoId(1L);
            resultado.setCantidadProducida(15);
            resultado.setCantidadMerma(2);
            resultado.setUsuario("operador1");

            when(registrarAvanceUseCase.registrar(eq(1L), any(RegistrarAvanceCommand.class))).thenReturn(resultado);

            mockMvc.perform(post("/api/v1/produccion/ordenes-trabajo/1/avance")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ordenTrabajoId").value(1L))
                    .andExpect(jsonPath("$.cantidadProducida").value(15))
                    .andExpect(jsonPath("$.cantidadMerma").value(2));

            verify(registrarAvanceUseCase).registrar(eq(1L), any(RegistrarAvanceCommand.class));
        }

        @Test
        @DisplayName("POST /{id}/avance - Error/400: payload sin usuario (obligatorio) es rechazado")
        void registrarAvance_sinUsuario_retorna400() throws Exception {
            RegistrarAvanceCommand command = new RegistrarAvanceCommand();
            command.setCantidadProducida(5);
            command.setCantidadMerma(0);

            mockMvc.perform(post("/api/v1/produccion/ordenes-trabajo/1/avance")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(command)))
                    .andExpect(status().isBadRequest());

            verify(registrarAvanceUseCase, never()).registrar(any(), any());
        }
    }
}
