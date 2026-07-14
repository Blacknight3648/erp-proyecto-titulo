package backend.com.produccion.infrastructure.api;

import backend.com.produccion.application.dto.DashboardOPKpisResponse;
import backend.com.produccion.application.dto.DashboardOPResponse;
import backend.com.produccion.application.dto.HistorialPrecioDTO;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.service.DashboardOPService;
import backend.com.produccion.application.service.HistorialPreciosService;
import backend.com.produccion.application.service.HojaCompraService;
import backend.com.produccion.application.service.OrdenCompraService;
import backend.com.produccion.application.service.OrdenServicioService;
import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.produccion.domain.enums.EstadoOS;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/api/v1/reportes")
@RequiredArgsConstructor
public class ReportesController {

    private final HojaCompraService hojaCompraService;
    private final OrdenCompraService ordenCompraService;
    private final OrdenServicioService ordenServicioService;
    private final DashboardOPService dashboardOPService;
    private final HistorialPreciosService historialPreciosService;

    /**
     * Dashboard operacional de OPs: alertas de retraso en etapas de manufactura.
     */
    @GetMapping("/dashboard-op")
    public ResponseEntity<DashboardOPResponse> dashboardOP() {
        return ResponseEntity.ok(dashboardOPService.calcular());
    }

    /**
     * KPIs de tiempos de ciclo por etapa y distribución de OPs por tamaño de lote.
     */
    @GetMapping("/dashboard-op/kpis")
    public ResponseEntity<DashboardOPKpisResponse> dashboardOPKpis() {
        return ResponseEntity.ok(dashboardOPService.calcularKpis());
    }

    /**
     * Hojas de Compra pendientes de aprobar (estado BORRADOR).
     */
    @GetMapping("/hcs-pendientes-aprobacion")
    public ResponseEntity<List<HojaCompraDTO>> hcsPendientesAprobacion() {
        return ResponseEntity.ok(hojaCompraService.listarPorEstado(EstadoHC.BORRADOR));
    }

    /**
     * OCs pendientes de recepción: emitidas, enviadas o con recepción parcial.
     */
    @GetMapping("/ocs-pendientes-recepcion")
    public ResponseEntity<List<OrdenCompraDTO>> ocsPendientesRecepcion() {
        List<OrdenCompraDTO> resultado = Stream.of(
                        ordenCompraService.listarPorEstado(EstadoOC.EMITIDA),
                        ordenCompraService.listarPorEstado(EstadoOC.ENVIADA),
                        ordenCompraService.listarPorEstado(EstadoOC.RECEPCIONADA_PARCIAL))
                .flatMap(List::stream)
                .toList();
        return ResponseEntity.ok(resultado);
    }

    /**
     * OSs en taller (despachadas pero aún no completamente recepcionadas o no cerradas).
     */
    @GetMapping("/oss-en-taller")
    public ResponseEntity<List<OrdenServicioDTO>> ossEnTaller() {
        List<OrdenServicioDTO> resultado = Stream.of(
                        ordenServicioService.listarPorEstado(EstadoOS.EN_PROCESO),
                        ordenServicioService.listarPorEstado(EstadoOS.RECEPCIONADA))
                .flatMap(List::stream)
                .toList();
        return ResponseEntity.ok(resultado);
    }

    /**
     * Historial de precios pagados por insumo/proveedor a través de las OC emitidas.
     */
    @GetMapping("/historial-precios")
    public ResponseEntity<List<HistorialPrecioDTO>> historialPrecios(
            @RequestParam(required = false) Integer articuloId,
            @RequestParam(required = false) Long proveedorId) {
        return ResponseEntity.ok(historialPreciosService.buscar(articuloId, proveedorId));
    }
}
