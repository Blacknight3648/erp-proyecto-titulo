package backend.com.shared.controller;

import backend.com.shared.application.dto.GiroDTO;
import backend.com.shared.application.service.GiroService;
import backend.com.shared.domain.model.Giro;
import backend.com.shared.infrastructure.api.GiroController;
import backend.com.shared.infrastructure.mapper.GiroMapper;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GiroController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("GiroController (API Web)")
class GiroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private GiroService giroService;

    @MockitoBean
    private GiroMapper giroMapper;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private GiroDTO buildDTO(Long id, String nombre) {
        GiroDTO dto = new GiroDTO();
        dto.setGiroId(id);
        dto.setNombreGiro(nombre);
        return dto;
    }

    private Giro buildModel(Long id, String nombre) {
        Giro model = new Giro();
        model.setGiroId(id);
        model.setNombreGiro(nombre);
        return model;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void listarTodos() throws Exception {
            Giro giro = buildModel(1L, "Giro 1");
            when(giroService.listarTodos()).thenReturn(List.of(giro));
            when(giroMapper.toDTOList(any())).thenReturn(List.of(buildDTO(1L, "Giro 1")));

            mockMvc.perform(get("/api/v1/giros"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].nombreGiro").value("Giro 1"));
        }

        @Test
        void obtenerPorId() throws Exception {
            Giro giro = buildModel(1L, "Giro 1");
            when(giroService.obtenerPorId(1L)).thenReturn(Optional.of(giro));
            when(giroMapper.toDTO(giro)).thenReturn(buildDTO(1L, "Giro 1"));

            mockMvc.perform(get("/api/v1/giros/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.giroId").value(1));
        }

        @Test
        void obtenerPorCodigoSii() throws Exception {
            Giro giro = buildModel(1L, "Giro 1");
            when(giroService.obtenerPorCodigoSii("123")).thenReturn(Optional.of(giro));
            when(giroMapper.toDTO(giro)).thenReturn(buildDTO(1L, "Giro 1"));

            mockMvc.perform(get("/api/v1/giros/codigo/123"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.giroId").value(1));
        }

        @Test
        void buscarPorNombreGiro() throws Exception {
            when(giroService.buscarPorNombreGiro("Giro 1")).thenReturn(List.of(buildModel(1L, "Giro 1")));
            when(giroMapper.toDTOList(any())).thenReturn(List.of(buildDTO(1L, "Giro 1")));

            mockMvc.perform(get("/api/v1/giros/nombre/Giro 1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void obtenerPorDescripcionGiro() throws Exception {
            when(giroService.obtenerPorDescripcionGiro("Desc")).thenReturn(List.of(buildModel(1L, "Giro 1")));
            when(giroMapper.toDTOList(any())).thenReturn(List.of(buildDTO(1L, "Giro 1")));

            mockMvc.perform(get("/api/v1/giros/descripcion/Desc"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }

        @Test
        void obtenerOCrearPorNombreGiro() throws Exception {
            Giro giro = buildModel(1L, "Giro 1");
            when(giroService.obtenerOCrearPorNombreGiro("Giro 1")).thenReturn(Optional.of(giro));
            when(giroMapper.toDTO(giro)).thenReturn(buildDTO(1L, "Giro 1"));

            mockMvc.perform(get("/api/v1/giros/obtenerocrear/Giro 1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.giroId").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            GiroDTO input = new GiroDTO();
            input.setNombreGiro("Nuevo Giro");

            Giro domainInput = buildModel(null, "Nuevo Giro");
            Giro domainOutput = buildModel(1L, "Nuevo Giro");
            GiroDTO outputDTO = buildDTO(1L, "Nuevo Giro");

            when(giroMapper.toDomain(any(GiroDTO.class))).thenReturn(domainInput);
            when(giroService.crear(any())).thenReturn(domainOutput);
            when(giroMapper.toDTO(domainOutput)).thenReturn(outputDTO);

            mockMvc.perform(post("/api/v1/giros")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.giroId").value(1));
        }

        @Test
        void actualizar() throws Exception {
            GiroDTO input = new GiroDTO();
            input.setNombreGiro("Giro Actualizado");

            Giro domainInput = buildModel(null, "Giro Actualizado");
            Giro domainOutput = buildModel(1L, "Giro Actualizado");
            GiroDTO outputDTO = buildDTO(1L, "Giro Actualizado");

            when(giroMapper.toDomain(any(GiroDTO.class))).thenReturn(domainInput);
            when(giroService.actualizar(eq(1L), any())).thenReturn(domainOutput);
            when(giroMapper.toDTO(domainOutput)).thenReturn(outputDTO);

            mockMvc.perform(put("/api/v1/giros/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombreGiro").value("Giro Actualizado"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v1/giros/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
