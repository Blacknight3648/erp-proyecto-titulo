package backend.com.shared.controller;

import backend.com.shared.application.dto.PrecioDTO;
import backend.com.shared.application.service.PrecioService;
import backend.com.shared.infrastructure.api.PrecioController;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PrecioController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PrecioController (API Web)")
class PrecioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PrecioService precioService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private PrecioDTO buildDTO(Integer id, Integer idArticulo, Integer idMoneda, BigDecimal valor) {
        PrecioDTO dto = new PrecioDTO();
        dto.setIdPrecio(id);
        dto.setIdArticulo(idArticulo);
        dto.setIdMoneda(idMoneda);
        dto.setTipoPrecio("VENTA");
        dto.setValor(valor);
        return dto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void obtenerPorId() throws Exception {
            when(precioService.obtenerPorId(1)).thenReturn(buildDTO(1, 10, 20, new BigDecimal("100.00")));

            mockMvc.perform(get("/api/v3/maestros/precios/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.idPrecio").value(1));
        }

        @Test
        void listarPorArticulo() throws Exception {
            when(precioService.listarPorArticulo(10)).thenReturn(List.of(
                    buildDTO(1, 10, 20, new BigDecimal("100.00"))
            ));

            mockMvc.perform(get("/api/v3/maestros/precios/articulo/10"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void crear() throws Exception {
            PrecioDTO input = new PrecioDTO();
            input.setIdArticulo(10);
            input.setIdMoneda(20);
            input.setTipoPrecio("VENTA");
            input.setValor(new BigDecimal("150.00"));

            PrecioDTO output = buildDTO(1, 10, 20, new BigDecimal("150.00"));
            when(precioService.crear(any())).thenReturn(output);

            mockMvc.perform(post("/api/v3/maestros/precios")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.idPrecio").value(1));
        }

        @Test
        void actualizar() throws Exception {
            PrecioDTO input = new PrecioDTO();
            input.setIdArticulo(10);
            input.setIdMoneda(20);
            input.setTipoPrecio("COSTO");
            input.setValor(new BigDecimal("90.00"));

            PrecioDTO output = buildDTO(1, 10, 20, new BigDecimal("90.00"));
            output.setTipoPrecio("COSTO");
            when(precioService.actualizar(eq(1), any())).thenReturn(output);

            mockMvc.perform(put("/api/v3/maestros/precios/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoPrecio").value("COSTO"));
        }

        @Test
        void eliminar() throws Exception {
            mockMvc.perform(delete("/api/v3/maestros/precios/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
