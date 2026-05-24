package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.service.RecepcionOCService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recepciones-oc")
@RequiredArgsConstructor
public class RecepcionOCController {

    private final RecepcionOCService recepcionOCService;

    @PostMapping("/oc/{ocId}")
    public ResponseEntity<RecepcionOCDTO> registrar(@PathVariable Long ocId,
                                                    @Valid @RequestBody RecepcionOCDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(recepcionOCService.registrar(ocId, request));
    }

    @GetMapping("/{idRecepcion}")
    public ResponseEntity<RecepcionOCDTO> obtenerPorId(@PathVariable Long idRecepcion) {
        return recepcionOCService.obtenerPorId(idRecepcion)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/oc/{ocId}")
    public ResponseEntity<List<RecepcionOCDTO>> listarPorOC(@PathVariable Long ocId) {
        return ResponseEntity.ok(recepcionOCService.listarPorOC(ocId));
    }
}
