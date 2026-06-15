package backend.com.comercial.controller;

import backend.com.comercial.application.UseCase.*;
import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.infrastructure.api.EvaluacionNegocioController;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.application.service.NumeroDocumentoService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static backend.com.comercial.domain.enums.EstadoEVN.ADJUDICADA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = EvaluacionNegocioController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("EvaluacionNegocioController (API Web)")
class EvaluacionNegocioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private CrearEVNUseCase crearEVNUseCase;
    @MockitoBean private ActualizarEVNUseCase actualizarEVNUseCase;
    @MockitoBean private AdjudicarEVNUseCase adjudicarEVNUseCase;
    @MockitoBean private AprobarEVNUseCase aprobarEVNUseCase;

    @MockitoBean private EvaluacionNegocioRepository repository;
    @MockitoBean private HistorialEstadoService historialService;
    @MockitoBean private NumeroDocumentoService numeroDocumentoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // =========================
    // HELPERS
    // =========================
    private EVNResponse evn(Long id, String numero) {
        EVNResponse r = new EVNResponse();
        r.setId(1L);
        r.setNumero(numero);
        return r;
    }

    // =========================
    // GET
    // =========================
    @Nested
    class Consultas {

        @Test
        @DisplayName("GET / - listar EVN")
        void listar() throws Exception {

            when(repository.findAll()).thenReturn(List.of());
            mockMvc.perform(get("/api/v1/comercial/evaluaciones-negocio"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /{id} - OK")
        void obtenerPorId() throws Exception {

            EvaluacionNegocio domain = new EvaluacionNegocio(
                    1L,
                    new DocumentNumber("EVN-001"),
                    1L,
                    1L,
                    EstadoEVN.ADJUDICADA,
                    LocalDate.of(2026, 6, 15),
                    null,                         // tomaTallaje
                    List.of(),                              // gastosAdicionales
                    List.of(),                              // items
                    BigDecimal.valueOf(5),
                    "Cliente Test",
                    "Licitación Hospital",
                    "Vendedor Test"
            );

            when(repository.findById(1L))
                    .thenReturn(Optional.of(domain));

            mockMvc.perform(get("/api/v1/comercial/evaluaciones-negocio/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /{id} - 404")
        void obtenerPorId404() throws Exception {

            when(repository.findById(1L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/comercial/evaluaciones-negocio/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("GET /next-number")
        void nextNumber() throws Exception {

            when(numeroDocumentoService.siguiente("EVN")).thenReturn(10L);

            mockMvc.perform(get("/api/v1/comercial/evaluaciones-negocio/next-number"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("10"));
        }
    }

    // =========================
    // POST / PUT
    // =========================
    @Nested
    class Escritura {

        @Test
        @DisplayName("POST / crear EVN")
        void crear() throws Exception {

            CrearEVNCommand cmd = new CrearEVNCommand();

            EVNResponse response = evn(1L, "EVN-001");

            when(crearEVNUseCase.ejecutar(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/comercial/evaluaciones-negocio")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cmd)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("PUT / actualizar EVN")
        void actualizar() throws Exception {

            CrearEVNCommand cmd = new CrearEVNCommand();
            EVNResponse response = evn(1L, "EVN-MOD");

            when(actualizarEVNUseCase.ejecutar(eq(1L), any())).thenReturn(response);

            mockMvc.perform(put("/api/v1/comercial/evaluaciones-negocio/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cmd)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numero").value("EVN-MOD"));
        }
    }

    // =========================
    // PATCH ACTIONS
    // =========================
    @Nested
    class Acciones {

        @Test
        @DisplayName("PATCH /adjudicar")
        void adjudicar() throws Exception {

            FirmaAprobacionRequest req = new FirmaAprobacionRequest();
            req.setAprobador("Juan");

            when(adjudicarEVNUseCase.ejecutar(eq(1L), any(), any()))
                    .thenReturn(evn(1L, "EVN-001"));

            mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/1/adjudicar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("PATCH /aprobar")
        void aprobar() throws Exception {

            FirmaAprobacionRequest req = new FirmaAprobacionRequest();
            req.setAprobador("Juan");

            when(aprobarEVNUseCase.aprobar(eq(1L), any(), any()))
                    .thenReturn(evn(1L, "EVN-001"));

            mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/1/aprobar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("PATCH /rechazar")
        void rechazar() throws Exception {

            FirmaAprobacionRequest req = new FirmaAprobacionRequest();
            req.setAprobador("Juan");

            when(aprobarEVNUseCase.rechazar(eq(1L), any(), any()))
                    .thenReturn(evn(1L, "EVN-001"));

            mockMvc.perform(patch("/api/v1/comercial/evaluaciones-negocio/1/rechazar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk());
        }
    }

    // =========================
    // HISTORIAL
    // =========================
    @Nested
    class Historial {

        @Test
        @DisplayName("GET /historial")
        void historial() throws Exception {

            when(historialService.consultar("EVN", 1L))
                    .thenReturn(List.of(new HistorialEstadoDTO()));

            mockMvc.perform(get("/api/v1/comercial/evaluaciones-negocio/1/historial")
                            .header("X-User", "test-user"))
                    .andExpect(status().isOk());
        }
    }
}
