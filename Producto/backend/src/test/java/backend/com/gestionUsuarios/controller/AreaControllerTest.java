package backend.com.gestionUsuarios.controller;

import backend.com.gestionUsuarios.application.dto.AreaDTO;
import backend.com.gestionUsuarios.application.service.AreaService;
import backend.com.gestionUsuarios.domain.model.Area;
import backend.com.gestionUsuarios.infrastructure.api.AreaController;
import backend.com.gestionUsuarios.infrastructure.mapper.AreaMapper;
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

@WebMvcTest(controllers = AreaController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("Pruebas Unitarias - AreaController")
class AreaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AreaService areaService;

    @MockitoBean
    private AreaMapper areaMapper;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private Area crearAreaMock(Long id, String nombre) {
        Area area = new Area();
        area.setAreaId(id);
        area.setNombre(nombre);
        return area;
    }

    private AreaDTO crearAreaDTOMock(Long id, String nombre) {
        AreaDTO dto = new AreaDTO();
        dto.setAreaId(id);
        dto.setNombre(nombre);
        return dto;
    }

    @Nested
    @DisplayName("Consultas GET")
    class ConsultasTests {

        @Test
        @DisplayName("Debe listar todas las áreas")
        void listarAreas() throws Exception {
            Area area = crearAreaMock(1L, "TI");
            AreaDTO dto = crearAreaDTOMock(1L, "TI");

            when(areaService.listarAreas()).thenReturn(List.of(area));
            when(areaMapper.toDTOList(any())).thenReturn(List.of(dto));

            mockMvc.perform(get("/api/v1/areas"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].nombre").value("TI"));
        }

        @Test
        @DisplayName("Debe obtener área por ID")
        void obtenerArea() throws Exception {
            Area area = crearAreaMock(1L, "TI");
            AreaDTO dto = crearAreaDTOMock(1L, "TI");

            when(areaService.obtenerArea(1L)).thenReturn(area);
            when(areaMapper.toDTO(area)).thenReturn(dto);

            mockMvc.perform(get("/api/v1/areas/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("TI"));
        }
    }

    @Nested
    @DisplayName("Mutaciones POST/PUT/PATCH/DELETE")
    class MutacionesTests {

        @Test
        @DisplayName("Debe crear un área")
        void crear() throws Exception {
            AreaDTO inputDto = crearAreaDTOMock(null, "TI");
            Area areaMapped = crearAreaMock(null, "TI");
            Area areaSaved = crearAreaMock(1L, "TI");
            AreaDTO outputDto = crearAreaDTOMock(1L, "TI");

            when(areaMapper.toDomain(any(AreaDTO.class))).thenReturn(areaMapped);
            when(areaService.crearArea(any(Area.class))).thenReturn(areaSaved);
            when(areaMapper.toDTO(areaSaved)).thenReturn(outputDto);

            mockMvc.perform(post("/api/v1/areas")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.areaId").value(1))
                    .andExpect(jsonPath("$.nombre").value("TI"));
        }

        @Test
        @DisplayName("Debe actualizar un área")
        void actualizar() throws Exception {
            AreaDTO inputDto = crearAreaDTOMock(1L, "TI Modificado");
            Area areaMapped = crearAreaMock(1L, "TI Modificado");
            Area areaSaved = crearAreaMock(1L, "TI Modificado");
            AreaDTO outputDto = crearAreaDTOMock(1L, "TI Modificado");

            when(areaMapper.toDomain(any(AreaDTO.class))).thenReturn(areaMapped);
            when(areaService.actualizarArea(eq(1L), any(Area.class))).thenReturn(areaSaved);
            when(areaMapper.toDTO(areaSaved)).thenReturn(outputDto);

            mockMvc.perform(put("/api/v1/areas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("TI Modificado"));
        }

        @Test
        @DisplayName("Debe actualizar parcialmente un área")
        void actualizarParcial() throws Exception {
            AreaDTO inputDto = new AreaDTO();
            inputDto.setNombre("TI Patch");

            Area existente = crearAreaMock(1L, "TI");
            Area actualizada = crearAreaMock(1L, "TI Patch");
            AreaDTO outputDto = crearAreaDTOMock(1L, "TI Patch");

            when(areaService.obtenerArea(1L)).thenReturn(existente);
            when(areaService.actualizarArea(eq(1L), any(Area.class))).thenReturn(actualizada);
            when(areaMapper.toDTO(actualizada)).thenReturn(outputDto);

            mockMvc.perform(patch("/api/v1/areas/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(inputDto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("TI Patch"));
        }

        @Test
        @DisplayName("Debe eliminar un área")
        void eliminar() throws Exception {
            doNothing().when(areaService).eliminarArea(1L);

            mockMvc.perform(delete("/api/v1/areas/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
