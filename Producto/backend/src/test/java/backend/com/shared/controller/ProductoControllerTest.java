package backend.com.shared.controller;

import backend.com.shared.application.UseCase.ConsultarProductosUseCase;
import backend.com.shared.application.UseCase.EliminarProductoUseCase;
import backend.com.shared.application.UseCase.GuardarProductoUseCase;
import backend.com.shared.domain.model.Producto;
import backend.com.shared.infrastructure.api.ProductoController;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductoController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("ProductoController (API Web)")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ConsultarProductosUseCase consultarProductosUseCase;

    @MockitoBean
    private GuardarProductoUseCase guardarProductoUseCase;

    @MockitoBean
    private EliminarProductoUseCase eliminarProductoUseCase;

    @MockitoBean
    private backend.com.shared.infrastructure.persistence.repository.Jpa.IdempotencyTokenJpaRepository idempotencyTokenJpaRepository;

    private Producto buildProducto(Long id, String codigo, String nombre) {
        Producto producto = new Producto();
        producto.setProductoId(id);
        producto.setCodigoProducto(codigo);
        producto.setNombreProducto(nombre);
        return producto;
    }

    @Nested
    @DisplayName("Consultas (GET)")
    class Consultas {

        @Test
        void getAll() throws Exception {
            when(consultarProductosUseCase.listarTodos()).thenReturn(List.of(
                    buildProducto(1L, "PROD1", "Producto 1")
            ));

            mockMvc.perform(get("/api/v1/shared/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.size()").value(1))
                    .andExpect(jsonPath("$[0].codigoProducto").value("PROD1"));
        }

        @Test
        void getById() throws Exception {
            when(consultarProductosUseCase.obtenerPorId(1L)).thenReturn(Optional.of(buildProducto(1L, "PROD1", "Producto 1")));

            mockMvc.perform(get("/api/v1/shared/productos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.productoId").value(1));
        }
    }

    @Nested
    @DisplayName("Mutaciones (POST/PUT/DELETE)")
    class Mutaciones {

        @Test
        void create() throws Exception {
            Producto input = new Producto();
            input.setCodigoProducto("PROD-NEW");
            input.setNombreProducto("Nuevo Producto");

            Producto output = buildProducto(1L, "PROD-NEW", "Nuevo Producto");
            when(guardarProductoUseCase.ejecutar(any())).thenReturn(output);

            mockMvc.perform(post("/api/v1/shared/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(input)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.productoId").value(1));
        }

        @Test
        void deleteTest() throws Exception {
            when(consultarProductosUseCase.obtenerPorId(1L)).thenReturn(Optional.of(buildProducto(1L, "PROD1", "Producto 1")));
            mockMvc.perform(delete("/api/v1/shared/productos/1"))
                    .andExpect(status().isNoContent());
        }
    }
}
