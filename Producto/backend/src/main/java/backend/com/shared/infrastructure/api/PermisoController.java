package backend.com.shared.infrastructure.api;

import backend.com.shared.application.service.PermisoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
