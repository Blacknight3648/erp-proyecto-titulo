package backend.com.shared.infrastructure.api;

import backend.com.shared.application.UseCase.ConsultarProductosUseCase;
import backend.com.shared.application.UseCase.EliminarProductoUseCase;
import backend.com.shared.application.UseCase.GuardarProductoUseCase;
import backend.com.shared.domain.model.Producto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shared/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ConsultarProductosUseCase consultarProductosUseCase;
    private final GuardarProductoUseCase guardarProductoUseCase;
    private final EliminarProductoUseCase eliminarProductoUseCase;

    @GetMapping
    public List<Producto> getAll() {
        return consultarProductosUseCase.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> getById(@PathVariable Long id) {
        return consultarProductosUseCase.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> create(@RequestBody Producto producto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(guardarProductoUseCase.ejecutar(producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (consultarProductosUseCase.obtenerPorId(id).isPresent()) {
            eliminarProductoUseCase.ejecutar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
