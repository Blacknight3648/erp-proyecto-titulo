package backend.com.shared.infrastructure.api;

import backend.com.shared.application.service.PermisoService;
import backend.com.shared.domain.model.Permiso;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/permisos")
@RequiredArgsConstructor
public class PermisoController {

    private final PermisoService permisoService;

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(permisoService.listarPermisos());
    }

    @PostMapping
    public ResponseEntity<Permiso> crear(@RequestBody Permiso permiso) {
        Permiso nuevo = permisoService.crearPermiso(permiso);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Permiso> actualizar(@PathVariable Long id, @RequestBody Permiso permisoActualizado) {
        Permiso actualizado = permisoService.actualizarPermiso(id, permisoActualizado);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        permisoService.eliminarPermiso(id);
        return ResponseEntity.noContent().build();
    }
}
