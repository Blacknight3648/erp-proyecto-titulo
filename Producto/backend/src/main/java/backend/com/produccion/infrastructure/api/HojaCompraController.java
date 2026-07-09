package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.domain.enums.EstadoHC;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hojas-compra")
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

    @PatchMapping("/{idHC}/reabrir")
    public ResponseEntity<HojaCompraDTO> reabrir(@PathVariable Long idHC) {
        return ResponseEntity.ok(hojaCompraService.reabrir(idHC));
    }

    @PutMapping("/{idHC}/items/{idHCItem}/modificar")
    public ResponseEntity<HojaCompraDTO> modificarItem(
            @PathVariable Long idHC,
            @PathVariable Long idHCItem,
            @jakarta.validation.Valid @RequestBody backend.com.produccion.application.dto.ActualizarHojaCompraItemRequest request) {
        return ResponseEntity.ok(hojaCompraService.modificarItem(idHC, idHCItem, request));
    }

    /**
     * Agrega un insumo "no presupuestado" (no vino del cálculo automático
     * desde el Costeo/OP) a una HC ya APROBADA.
     */
    @PostMapping("/{idHC}/items")
    public ResponseEntity<HojaCompraDTO> agregarItemManual(
            @PathVariable Long idHC,
            @RequestBody backend.com.produccion.application.dto.HojaCompraItemDTO itemDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(hojaCompraService.agregarItemManual(idHC, itemDTO));
    }
}
