package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.domain.model.EstadoHC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hojas-compra")
@RequiredArgsConstructor
public class HojaCompraController {

    private final HojaCompraService hojaCompraService;

    @PostMapping("/generar/{opId}")
    public ResponseEntity<HojaCompraDTO> generarDesdeOP(@PathVariable Long opId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hojaCompraService.generarDesdeOP(opId));
    }

    @GetMapping
    public ResponseEntity<List<HojaCompraDTO>> listar(@RequestParam(required = false) EstadoHC estado) {
        if (estado != null) {
            return ResponseEntity.ok(hojaCompraService.listarPorEstado(estado));
        }
        return ResponseEntity.ok(hojaCompraService.listarTodas());
    }

    @GetMapping("/{idHC}")
    public ResponseEntity<HojaCompraDTO> obtenerPorId(@PathVariable Long idHC) {
        return hojaCompraService.obtenerPorId(idHC)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/op/{opId}")
    public ResponseEntity<HojaCompraDTO> obtenerPorOP(@PathVariable Long opId) {
        return hojaCompraService.obtenerPorOpId(opId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{idHC}/aprobar")
    public ResponseEntity<HojaCompraDTO> aprobar(@PathVariable Long idHC) {
        return ResponseEntity.ok(hojaCompraService.aprobar(idHC));
    }

    @PatchMapping("/{idHC}/cerrar")
    public ResponseEntity<HojaCompraDTO> cerrar(@PathVariable Long idHC) {
        return ResponseEntity.ok(hojaCompraService.cerrar(idHC));
    }
}
