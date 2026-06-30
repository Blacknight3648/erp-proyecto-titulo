package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.GenerarOCLoteRequest;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraItemDTO;
import backend.com.produccion.application.service.OrdenCompraService;
import backend.com.produccion.domain.enums.EstadoOC;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes-compra")
@RequiredArgsConstructor
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;

    @PostMapping("/consolidar")
    public ResponseEntity<OrdenCompraDTO> generarConsolidada(
            @Valid @RequestBody GenerarOCConsolidadaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenCompraService.generarConsolidada(request));
    }

    @PostMapping("/consolidar-lote")
    public ResponseEntity<List<OrdenCompraDTO>> generarLote(
            @Valid @RequestBody GenerarOCLoteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenCompraService.generarLote(request));
    }

    @GetMapping
    public ResponseEntity<List<OrdenCompraDTO>> listar(
            @RequestParam(required = false) EstadoOC estado,
            @RequestParam(required = false) Long proveedorId,
            @RequestParam(required = false) Long hcItemId) {
        if (estado != null) {
            return ResponseEntity.ok(ordenCompraService.listarPorEstado(estado));
        }
        if (proveedorId != null) {
            return ResponseEntity.ok(ordenCompraService.listarPorProveedor(proveedorId));
        }
        if (hcItemId != null) {
            return ResponseEntity.ok(ordenCompraService.listarPorHCItem(hcItemId));
        }
        return ResponseEntity.ok(ordenCompraService.listarTodas());
    }

    @GetMapping("/{idOC}")
    public ResponseEntity<OrdenCompraDTO> obtenerPorId(@PathVariable Long idOC) {
        return ordenCompraService.obtenerPorId(idOC)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{idOC}/enviar")
    public ResponseEntity<OrdenCompraDTO> marcarEnviada(@PathVariable Long idOC) {
        return ResponseEntity.ok(ordenCompraService.marcarEnviada(idOC));
    }

    @PatchMapping("/{idOC}/recepcionar")
    public ResponseEntity<OrdenCompraDTO> marcarRecepcionada(@PathVariable Long idOC) {
        return ResponseEntity.ok(ordenCompraService.marcarRecepcionada(idOC));
    }

    @PatchMapping("/{idOC}/cerrar")
    public ResponseEntity<OrdenCompraDTO> cerrar(@PathVariable Long idOC) {
        return ResponseEntity.ok(ordenCompraService.cerrar(idOC));
    }

    @PatchMapping("/{idOC}/items/{idOCItem}/precio")
    public ResponseEntity<OrdenCompraDTO> actualizarPrecio(@PathVariable Long idOC,
                                                           @PathVariable Long idOCItem,
                                                           @RequestParam BigDecimal precio) {
        return ResponseEntity.ok(ordenCompraService.actualizarPrecioItem(idOC, idOCItem, precio));
    }

    @DeleteMapping("/{idOC}")
    public ResponseEntity<Void> eliminar(@PathVariable Long idOC) {
        ordenCompraService.eliminar(idOC);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idOC}/items")
    public ResponseEntity<OrdenCompraDTO> agregarItem(@PathVariable Long idOC,
                                                      @Valid @RequestBody OrdenCompraItemDTO itemDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenCompraService.agregarItem(idOC, itemDTO));
    }

    @PutMapping("/{idOC}/items/{idOCItem}")
    public ResponseEntity<OrdenCompraDTO> actualizarItem(@PathVariable Long idOC,
                                                         @PathVariable Long idOCItem,
                                                         @Valid @RequestBody OrdenCompraItemDTO itemDTO) {
        return ResponseEntity.ok(ordenCompraService.actualizarItem(idOC, idOCItem, itemDTO));
    }

    @DeleteMapping("/{idOC}/items/{idOCItem}")
    public ResponseEntity<OrdenCompraDTO> eliminarItem(@PathVariable Long idOC,
                                                       @PathVariable Long idOCItem) {
        return ResponseEntity.ok(ordenCompraService.eliminarItem(idOC, idOCItem));
    }
}
