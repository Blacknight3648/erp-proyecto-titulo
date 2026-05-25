package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.CrearOSRequest;
import backend.com.produccion.application.dto.DespachoOSDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOSDTO;
import backend.com.produccion.application.service.OrdenServicioService;
import backend.com.produccion.domain.model.EstadoOS;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes-servicio")
@RequiredArgsConstructor
public class OrdenServicioController {

    private final OrdenServicioService ordenServicioService;

    @PostMapping
    public ResponseEntity<OrdenServicioDTO> crear(@Valid @RequestBody CrearOSRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenServicioService.crear(request));
    }

    @GetMapping
    public ResponseEntity<List<OrdenServicioDTO>> listar(
            @RequestParam(required = false) EstadoOS estado,
            @RequestParam(required = false) Long opId,
            @RequestParam(required = false) Long proveedorId) {
        if (estado != null) return ResponseEntity.ok(ordenServicioService.listarPorEstado(estado));
        if (opId != null) return ResponseEntity.ok(ordenServicioService.listarPorOP(opId));
        if (proveedorId != null) return ResponseEntity.ok(ordenServicioService.listarPorProveedor(proveedorId));
        return ResponseEntity.ok(ordenServicioService.listarTodas());
    }

    @GetMapping("/{idOS}")
    public ResponseEntity<OrdenServicioDTO> obtenerPorId(@PathVariable Long idOS) {
        return ordenServicioService.obtenerPorId(idOS)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{idOS}/despachos")
    public ResponseEntity<OrdenServicioDTO> registrarDespacho(@PathVariable Long idOS,
                                                              @Valid @RequestBody DespachoOSDTO despacho) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenServicioService.registrarDespacho(idOS, despacho));
    }

    @PostMapping("/{idOS}/recepciones")
    public ResponseEntity<OrdenServicioDTO> registrarRecepcion(@PathVariable Long idOS,
                                                               @Valid @RequestBody RecepcionOSDTO recepcion) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ordenServicioService.registrarRecepcion(idOS, recepcion));
    }

    @PatchMapping("/{idOS}/cerrar")
    public ResponseEntity<OrdenServicioDTO> cerrar(@PathVariable Long idOS) {
        return ResponseEntity.ok(ordenServicioService.cerrar(idOS));
    }
}
