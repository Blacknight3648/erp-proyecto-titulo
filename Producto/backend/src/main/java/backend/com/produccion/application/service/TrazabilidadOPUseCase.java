package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.CosteoVersionInfoDTO;
import backend.com.produccion.application.dto.HojaCompraDTO;
import backend.com.produccion.application.dto.OrdenCompraDTO;
import backend.com.produccion.application.dto.OrdenServicioDTO;
import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.dto.TrazabilidadOPDTO;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrazabilidadOPUseCase {

    private final OrdenProduccionRepository ordenProduccionRepository;
    private final CosteoVersionRepository costeoVersionRepository;
    private final HojaCompraRepository hojaCompraRepository;
    private final OrdenCompraRepository ordenCompraRepository;
    private final OrdenServicioService ordenServicioService;
    private final HojaCompraService hojaCompraService;
    private final RecepcionOCService recepcionOCService;

    @Transactional(readOnly = true)
    public TrazabilidadOPDTO ejecutar(Long opId) {
        if (opId == null) {
            throw new IllegalArgumentException("opId es obligatorio");
        }

        OrdenProduccion op = ordenProduccionRepository.findById(opId)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Producción no encontrada: " + opId));

        CosteoVersionInfoDTO versionInfo = null;
        if (op.getCosteoVersionId() != null) {
            CosteoVersion version = costeoVersionRepository.findById(op.getCosteoVersionId()).orElse(null);
            if (version != null) {
                versionInfo = CosteoVersionInfoDTO.builder()
                        .idCosteoVersion(version.getIdCosteoVersion())
                        .costeoId(version.getCosteoId())
                        .numeroVersion(version.getNumeroVersion())
                        .fechaCreacion(version.getFechaCreacion())
                        .motivoCambio(version.getMotivoCambio())
                        .usuarioCreador(version.getUsuarioCreador())
                        .build();
            }
        }

        HojaCompraDTO hcDTO = hojaCompraService.obtenerPorOpId(opId).orElse(null);

        // Buscar todas las OCs que consumen items de esta HC (deduplicadas)
        List<OrdenCompraDTO> ocsDTO = new ArrayList<>();
        List<RecepcionOCDTO> recepcionesDTO = new ArrayList<>();
        if (hcDTO != null) {
            Map<Long, OrdenCompra> ocsUnicas = new LinkedHashMap<>();
            HojaCompra hc = hojaCompraRepository.findByOpId(opId).orElse(null);
            if (hc != null) {
                for (HojaCompraItem hcItem : hc.getItems()) {
                    List<OrdenCompra> ocs = ordenCompraRepository.findAllByHcItemId(hcItem.getIdHCItem());
                    for (OrdenCompra oc : ocs) {
                        ocsUnicas.put(oc.getIdOC(), oc);
                    }
                }
            }
            for (OrdenCompra oc : ocsUnicas.values()) {
                ocsDTO.add(toOCDTO(oc));
                recepcionesDTO.addAll(recepcionOCService.listarPorOC(oc.getIdOC()));
            }
        }

        List<OrdenServicioDTO> ossDTO = ordenServicioService.listarPorOP(opId);

        return TrazabilidadOPDTO.builder()
                .opId(op.getIdOP())
                .numeroOP(op.getNumeroOP() != null ? op.getNumeroOP().getValue() : null)
                .notaVentaId(op.getNotaVentaId())
                .estadoOP(op.getEstado())
                .fechaEntregaProgramada(op.getFechaEntregaProgramada())
                .costeoVersion(versionInfo)
                .hojaCompra(hcDTO)
                .ordenesCompra(ocsDTO)
                .recepcionesOC(recepcionesDTO)
                .ordenesServicio(ossDTO)
                .build();
    }

    /**
     * Convierte el dominio OrdenCompra a DTO mínimo para la vista de trazabilidad
     * (sin recursividad de items, solo metadata principal).
     */
    private OrdenCompraDTO toOCDTO(OrdenCompra oc) {
        return OrdenCompraDTO.builder()
                .idOC(oc.getIdOC())
                .numeroOC(oc.getNumeroOC() != null ? oc.getNumeroOC().getValue() : null)
                .proveedorId(oc.getProveedorId())
                .estado(oc.getEstado())
                .fechaEmision(oc.getFechaEmision())
                .fechaEntregaEstimada(oc.getFechaEntregaEstimada())
                .observaciones(oc.getObservaciones())
                .totalNeto(oc.getTotalNeto())
                .items(List.of())
                .build();
    }
}
