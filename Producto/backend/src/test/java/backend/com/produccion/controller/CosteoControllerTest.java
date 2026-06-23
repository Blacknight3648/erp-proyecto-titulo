package backend.com.produccion.controller;

import backend.com.produccion.application.UseCase.GestionarCosteoUseCase;
import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import backend.com.produccion.infrastructure.api.CosteoController;
import backend.com.produccion.infrastructure.api.HojaCompraController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean; // Nueva ubicación estándar
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CosteoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("CosteoController (API Web)")
class CosteoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Sustitución de @MockBean por @MockitoBean para cumplir con Spring Boot 3.4+
    @MockitoBean
    private GestionarCosteoUseCase gestionarCosteoUseCase;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    // --- OBJETOS HELPERS PARA REUTILIZAR ---
    private CosteoDTO crearCosteoDTOTest(Long id, String numero) {
        CosteoDTO dto = new CosteoDTO();
        dto.setIdCosteo(id);
        dto.setNumeroCosteo(numero);
        dto.setSolicitudCostosId(100L);
        return dto;
    }

    // =========================================================================
    // NEGOCIO / OPERACIONES DE LECTURA (GET)
    // =========================================================================
    @Nested
    @DisplayName("Consultas de Costeos (GET)")
    class ConsultasGet {

        @Test
        @DisplayName("GET /scos - Retorna lista completa de costeos con estado 200 OK")
        void obtenerTodosLosCosteos() throws Exception {
            List<CosteoDTO> costeos = List.of(crearCosteoDTOTest(1L, "C-001"), crearCosteoDTOTest(2L, "C-002"));
            when(gestionarCosteoUseCase.obtenerTodos()).thenReturn(costeos);

            mockMvc.perform(get("/api/v1/produccion/costeos/scos"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.size()").value(2))
                    .andExpect(jsonPath("$[0].numeroCosteo").value("C-001"));
        }

        @Test
        @DisplayName("GET /scos/{scosId} - Caso correcto: encuentra el costeo por SCOS")
        void obtenerPorScosIdEncontrado() throws Exception {
            CosteoDTO costeo = crearCosteoDTOTest(1L, "C-777");
            when(gestionarCosteoUseCase.obtenerPorSCOS(100L)).thenReturn(Optional.of(costeo));

            mockMvc.perform(get("/api/v1/produccion/costeos/scos/100"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroCosteo").value("C-777"));
        }

        @Test
        @DisplayName("GET /scos/{scosId} - Error/404: no encuentra el costeo solicitado")
        void obtenerPorScosIdNoEncontrado() throws Exception {
            when(gestionarCosteoUseCase.obtenerPorSCOS(999L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/v1/produccion/costeos/scos/999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("GET /{idCosteo}/resumen-evn - Caso correcto: mapea el resumen DTO esperado")
        void obtenerResumenParaEvn() throws Exception {
            CosteoResumenEVNDTO resumen = CosteoResumenEVNDTO.builder()
                    .idCosteo(1L)
                    .numeroCosteo("C-123")
                    .modelo("Modelo Test")
                    .build();

            when(gestionarCosteoUseCase.obtenerResumenEVN(1L)).thenReturn(Optional.of(resumen));

            mockMvc.perform(get("/api/v1/produccion/costeos/1/resumen-evn"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroCosteo").value("C-123"))
                    .andExpect(jsonPath("$.modelo").value("Modelo Test"));
        }
    }

    // =========================================================================
    // ESCRITURA Y ESPECIFICACIONES DE MUTACIÓN (POST/PUT)
    // =========================================================================
    @Nested
    @DisplayName("Escritura y Mutación de Costeos (POST/PUT)")
    class EscrituraYMutacion {

        @Test
        @DisplayName("POST / - Caso correcto: registra un nuevo costeo de manera exitosa")
        void crearCosteoExitosamente() throws Exception {
            CosteoDTO inputDto = crearCosteoDTOTest(null, "C-NUEVO");
            CosteoDTO outputDto = crearCosteoDTOTest(50L, "C-NUEVO");

            when(gestionarCosteoUseCase.registrarCosteo(any(CosteoDTO.class))).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/produccion/costeos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idCosteo").value(50L))
                    .andExpect(jsonPath("$.numeroCosteo").value("C-NUEVO"));
        }

        @Test
        @DisplayName("PUT /{idCosteo} - Caso correcto: actualiza los datos del costeo existente")
        void actualizarCosteoExitosamente() throws Exception {
            CosteoDTO inputDto = crearCosteoDTOTest(1L, "C-MODIFICADO");
            when(gestionarCosteoUseCase.actualizarCosteo(eq(1L), any(CosteoDTO.class))).thenReturn(inputDto);

            mockMvc.perform(put("/api/v1/produccion/costeos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.numeroCosteo").value("C-MODIFICADO"));
        }
    }

    // =========================================================================
    // CONTROL DE EXCEPCIONES (MANEJO DE ERRORES GLOBALES)
    // =========================================================================
    @Nested
    @DisplayName("Control de Excepciones de Negocio")
    class ManejoExcepciones {

        @Test
        @DisplayName("POST / - Error/400 o 500: Lanza IllegalArgumentException ante payload corrupto")
        void lanzarExcepcionCuandoIdEsInvalido() throws Exception {
            CosteoDTO badDto = crearCosteoDTOTest(null, "ERROR");

            when(gestionarCosteoUseCase.registrarCosteo(any(CosteoDTO.class)))
                    .thenThrow(new IllegalArgumentException("El número de solicitud de costos es obligatorio"));

            mockMvc.perform(post("/api/v1/produccion/costeos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(badDto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("PUT /{idCosteo} - Error/409: Lanza IllegalStateException cuando el estado actual impide la edición")
        void lanzarExcepcionPorEstadoInvalido() throws Exception {
            CosteoDTO dto = crearCosteoDTOTest(1L, "C-BLOQUEADO");

            when(gestionarCosteoUseCase.actualizarCosteo(eq(1L), any(CosteoDTO.class)))
                    .thenThrow(new IllegalStateException("No se puede editar un costeo que ya está cerrado"));

            mockMvc.perform(put("/api/v1/produccion/costeos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isConflict());
        }
    }
}