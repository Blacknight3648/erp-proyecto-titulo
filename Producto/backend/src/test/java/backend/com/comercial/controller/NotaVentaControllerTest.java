package backend.com.comercial.controller;

import backend.com.comercial.application.UseCase.ConsultarTrazabilidadUseCase;
import backend.com.comercial.application.UseCase.CrearNVUseCase;
import backend.com.comercial.application.UseCase.GestionarNVUseCase;
import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.domain.enums.EstadoNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.comercial.infrastructure.api.NotaVentaController;
import backend.com.shared.application.dto.DocumentTraceDTO;
import backend.com.shared.application.dto.FirmaAprobacionRequest;
import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = NotaVentaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("NotaVentaController (API Web)")
public class NotaVentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private CrearNVUseCase crearNVUseCase;
    @MockitoBean private ConsultarTrazabilidadUseCase consultarTrazabilidadUseCase;
    @MockitoBean private GestionarNVUseCase gestionarNVUseCase;
    @MockitoBean private NotaVentaRepository repository;
    @MockitoBean private HistorialEstadoService historialService;
    @MockitoBean private NumeroDocumentoService numeroDocumentoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private NVResponse nvResponse(Long id, String numero) {
        NVResponse r = new NVResponse();
        r.setIdNV(id);
        r.setNumeroNV(numero);
        return r;
    }

    @Nested
    class Consultas {

        @Test
        @DisplayName("GET / - listar NV")
        void listar() throws Exception {
            when(repository.findAll()).thenReturn(List.of());
            mockMvc.perform(get("/api/v1/comercial/notas-venta"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /{id} - OK")
        void obtenerPorId() throws Exception {
            NotaVenta domain = new NotaVenta(
                    1L,
                    new DocumentNumber("NV-001"),
                    1L,
                    1L,
                    1L,
                    EstadoNV.APROBADA,
                    false,
                    null,
                    LocalDate.now(),
                    LocalDate.now().plusDays(5),
                    new Money(BigDecimal.ZERO, "CLP"),
                    new Money(BigDecimal.ZERO, "CLP"),
                    new Money(BigDecimal.ZERO, "CLP"),
                    List.of()
            );

            when(repository.findById(1L)).thenReturn(Optional.of(domain));

            mockMvc.perform(get("/api/v1/comercial/notas-venta/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroNV").value("NV-001"));
        }

        @Test
        @DisplayName("GET /{id} - 404")
        void obtenerPorId404() throws Exception {
            when(repository.findById(1L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/comercial/notas-venta/1"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("GET /next-number")
        void nextNumber() throws Exception {
            when(numeroDocumentoService.previsualizar("NV"))
                    .thenReturn(new backend.com.shared.valueobjects.DocumentNumber("NV-0000020"));

            mockMvc.perform(get("/api/v1/comercial/notas-venta/next-number"))
                    .andExpect(status().isOk())
                    .andExpect(content().string("NV-0000020"));
        }

        @Test
        @DisplayName("GET /{id}/trazabilidad")
        void trazabilidad() throws Exception {
            when(consultarTrazabilidadUseCase.ejecutar(1L))
                    .thenReturn(List.of(DocumentTraceDTO.builder()
                            .tipoDocumento("NV")
                            .id(1L)
                            .numero("NV-001")
                            .estado("CREADA")
                            .fecha(LocalDate.now())
                            .build()));

            mockMvc.perform(get("/api/v1/comercial/notas-venta/1/trazabilidad"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class Escritura {

        @Test
        @DisplayName("POST / crear NV")
        void crear() throws Exception {
            CrearNVCommand cmd = new CrearNVCommand();
            cmd.setEvaluacionNegocioId(1L);
            cmd.setClienteId(1L);
            cmd.setVendedorId(1L);
            cmd.setFechaEntregaEstimada(LocalDate.now());
            
            NVResponse response = nvResponse(1L, "NV-001");

            when(crearNVUseCase.ejecutar(any())).thenReturn(response);

            mockMvc.perform(post("/api/v1/comercial/notas-venta")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(cmd)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idNV").value(1));
        }
    }

    @Nested
    class Acciones {

        @Test
        @DisplayName("PATCH /aprobar")
        void aprobar() throws Exception {
            FirmaAprobacionRequest req = new FirmaAprobacionRequest();
            req.setAprobador("Juan");

            when(gestionarNVUseCase.aprobar(eq(1L), any(), any()))
                    .thenReturn(nvResponse(1L, "NV-001"));

            mockMvc.perform(patch("/api/v1/comercial/notas-venta/1/aprobar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("PATCH /cancelar")
        void cancelar() throws Exception {
            FirmaAprobacionRequest req = new FirmaAprobacionRequest();
            req.setAprobador("Juan");
            req.setMotivo("Cancelado por X");

            when(gestionarNVUseCase.cancelar(eq(1L), any(), any()))
                    .thenReturn(nvResponse(1L, "NV-001"));

            mockMvc.perform(patch("/api/v1/comercial/notas-venta/1/cancelar")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(req)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    class Historial {

        @Test
        @DisplayName("GET /historial")
        void historial() throws Exception {
            when(historialService.consultar("NV", 1L))
                    .thenReturn(List.of(new HistorialEstadoDTO()));

            mockMvc.perform(get("/api/v1/comercial/notas-venta/1/historial")
                            .header("X-User", "test-user"))
                    .andExpect(status().isOk());
        }
    }
}
