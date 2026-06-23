package backend.com.shared.controller;

import backend.com.shared.application.dto.HistorialEstadoDTO;
import backend.com.shared.application.service.HistorialEstadoService;
import backend.com.shared.infrastructure.api.HistorialEstadoController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HistorialEstadoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("HistorialEstadoController (API Web)")
class HistorialEstadoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistorialEstadoService historialEstadoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    @Test
    void consultar() throws Exception {
        HistorialEstadoDTO dto = new HistorialEstadoDTO();
        dto.setId(1L);
        dto.setTipoEntidad("Pedido");
        dto.setEntidadId(10L);
        dto.setEstadoNuevo("COMPLETADO");

        when(historialEstadoService.consultar("Pedido", 10L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/v1/historial-estado/Pedido/10")
                        .header("X-User", "test-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].estadoNuevo").value("COMPLETADO"));
    }
}
