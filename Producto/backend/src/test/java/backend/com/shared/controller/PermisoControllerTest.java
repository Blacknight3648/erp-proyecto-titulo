package backend.com.shared.controller;

import backend.com.shared.application.service.PermisoService;
import backend.com.shared.infrastructure.api.PermisoController;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PermisoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("PermisoController (API Web)")
class PermisoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PermisoService permisoService;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    @Test
    void getAll() throws Exception {
        when(permisoService.listarPermisos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/permisos"))
                .andExpect(status().isOk());
    }
}
