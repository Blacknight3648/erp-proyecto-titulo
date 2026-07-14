package backend.com.shared.infrastructure.api;

import backend.com.shared.application.dto.NotificacionDTO;
import backend.com.shared.application.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService service;

    @GetMapping
    public List<NotificacionDTO> listar() {
        return service.listar();
    }

    @GetMapping("/no-leidas")
    public ResponseEntity<Long> contarNoLeidas() {
        return ResponseEntity.ok(service.contarNoLeidas());
    }

    @PatchMapping("/{id}/leida")
    public ResponseEntity<Void> marcarLeida(@PathVariable Long id) {
        service.marcarLeida(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/marcar-todas-leidas")
    public ResponseEntity<Void> marcarTodasLeidas() {
        service.marcarTodasLeidas();
        return ResponseEntity.ok().build();
    }
}
