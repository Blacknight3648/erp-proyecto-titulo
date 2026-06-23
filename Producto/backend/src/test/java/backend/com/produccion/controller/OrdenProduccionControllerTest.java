package backend.com.produccion.controller;

import backend.com.produccion.application.UseCase.CalcularAvanceUseCase;
import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.infrastructure.api.OrdenProduccionController;
import backend.com.shared.valueobjects.DocumentNumber;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests unitarios para el controlador de Ordenes de Producción.
 * Evalúa los mapeos HTTP de lectura, mutaciones de estado de negocio
 * y respuestas en formato JSON de forma aislada.
 */
@WebMvcTest(controllers = OrdenProduccionController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("OrdenProduccionController (API Web)")
class OrdenProduccionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrdenProduccionRepository repository;

    @MockitoBean
    private CalcularAvanceUseCase calcularAvanceUseCase;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // Helper para simular OrdenProduccion stubbeando getters clave para evitar NullPointerException en el mapeador
    private OrdenProduccion crearOrdenProduccionMock(Long id, String numero) {
        OrdenProduccion op = mock(OrdenProduccion.class);

        // Cambiado a getIdOP() e inyectando el parámetro 'id' correcto
        when(op.getIdOP()).thenReturn(id);

        // Cambiado a getNumeroOP() para que coincida con el @Getter de tu dominio
        when(op.getNumeroOP()).thenReturn(numero != null ? new DocumentNumber(numero) : null);

        return op;
    }

    // =========================================================================
    // CONSULTAS / OPERACIONES DE LECTURA (GET)
    // =========================================================================
    @Nested
    @DisplayName("Consultas de Ordenes de Produccion (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET / - Retorna la lista completa de ordenes de produccion con estado 200 OK")
        void getAll_retornaListaDeOrdenes() throws Exception {
            OrdenProduccion op1 = crearOrdenProduccionMock(1L, "OP-001");
            OrdenProduccion op2 = crearOrdenProduccionMock(2L, "OP-002");
            when(repository.findAll()).thenReturn(List.of(op1, op2));

            mockMvc.perform(get("/api/v1/produccion/ordenes-produccion"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].idOP").value(1L))
                    .andExpect(jsonPath("$[0].numeroOP").value("OP-001"))
                    .andExpect(jsonPath("$[1].numeroOP").value("OP-002"));

            verify(repository).findAll();
        }

        @Test
        @DisplayName("GET /{id} - Caso correcto: encuentra la OP y mapea la respuesta")
        void getById_existente_retornaOP() throws Exception {
            OrdenProduccion op = crearOrdenProduccionMock(1L, "OP-777");
            when(repository.findById(1L)).thenReturn(Optional.of(op));

            mockMvc.perform(get("/api/v1/produccion/ordenes-produccion/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idOP").value(1L))
                    .andExpect(jsonPath("$.numeroOP").value("OP-777"));

            verify(repository).findById(1L);
        }

        @Test
        @DisplayName("GET /{id} - Error/404: lanza EntityNotFoundException si la OP no existe")
        void getById_noExistente_lanzaExcepcion() throws Exception {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/produccion/ordenes-produccion/99"))
                    .andExpect(status().isNotFound());

            verify(repository).findById(99L);
        }

        @Test
        @DisplayName("GET /{id}/avance - Caso correcto: retorna el avance calculado de la OP")
        void avance_retornaAvanceCorrectamente() throws Exception {
            AvanceOPResponse avanceMock = mock(AvanceOPResponse.class);
            when(avanceMock.getOrdenProduccionId()).thenReturn(10L);
            when(avanceMock.getPorcentajeGlobal()).thenReturn(new BigDecimal("75.00"));
            when(avanceMock.getSemaforo()).thenReturn("AMARILLO");

            when(calcularAvanceUseCase.calcular(10L)).thenReturn(avanceMock);

            mockMvc.perform(get("/api/v1/produccion/ordenes-produccion/10/avance"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ordenProduccionId").value(10L))
                    .andExpect(jsonPath("$.porcentajeGlobal").value(75.00))
                    .andExpect(jsonPath("$.semaforo").value("AMARILLO"));

            verify(calcularAvanceUseCase).calcular(10L);
        }
    }

    // =========================================================================
    // MUTACIONES / OPERACIONES DE ESCRITURA (POST)
    // =========================================================================
    @Nested
    @DisplayName("Mutaciones de Ordenes de Produccion (POST)")
    class MutacionesPost {

        @Test
        @DisplayName("POST /recepcionar/{id} - Caso correcto: recepciona la OP, la guarda y retorna la respuesta mapeada")
        void recepcionar_existente_modificaEstadoYPersiste() throws Exception {
            OrdenProduccion opMock = crearOrdenProduccionMock(1L, "OP-RECEPCIONADA");
            when(repository.findById(1L)).thenReturn(Optional.of(opMock));
            when(repository.save(any(OrdenProduccion.class))).thenReturn(opMock);

            mockMvc.perform(post("/api/v1/produccion/ordenes-produccion/recepcionar/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idOP").value(1L))
                    .andExpect(jsonPath("$.numeroOP").value("OP-RECEPCIONADA"));

            // Verificaciones clave de interacción con la entidad y la persistencia
            verify(opMock).recepcionar();
            verify(repository).save(opMock);
        }

        @Test
        @DisplayName("POST /recepcionar/{id} - Error/404: lanza EntityNotFoundException si la OP no existe al intentar recepcionar")
        void recepcionar_noExistente_lanzaExcepcion() throws Exception {
            when(repository.findById(99L)).thenReturn(Optional.empty());

            mockMvc.perform(post("/api/v1/produccion/ordenes-produccion/recepcionar/99"))
                    .andExpect(status().isNotFound());

            verify(repository, never()).save(any());
        }
    }
}
