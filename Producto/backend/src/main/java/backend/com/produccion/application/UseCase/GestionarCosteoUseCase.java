package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.dto.CosteoResumenEVNDTO;
import backend.com.produccion.application.service.CosteoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GestionarCosteoUseCase {

    private final CosteoService costeoService;
    private final backend.com.produccion.domain.repository.CosteoVersionRepository costeoVersionRepository;
    private final backend.com.produccion.domain.repository.CosteoRepository costeoRepository;

    public CosteoDTO registrarCosteo(CosteoDTO costeoDTO) {
        if (costeoDTO == null)
            throw new IllegalArgumentException("El costeo no puede ser nulo");
        // El número correlativo propio (C-0000001) lo asigna CosteoService.save
        // de forma atómica y transaccional.
        return costeoService.save(costeoDTO);
    }

    public CosteoDTO actualizarCosteo(Long idCosteo, CosteoDTO costeoDTO) {
        if (idCosteo == null)
            throw new IllegalArgumentException("El id del costeo es obligatorio para actualizar");
        if (costeoDTO == null)
            throw new IllegalArgumentException("El costeo no puede ser nulo");
        costeoDTO.setIdCosteo(idCosteo);
        // En actualización se conserva el número existente (CosteoService.save).
        return costeoService.save(costeoDTO);
    }

    // --- Transiciones del ciclo de vida del Costeo ---

    public CosteoDTO costear(Long idCosteo) {
        return costeoService.costear(idCosteo);
    }

    public CosteoDTO aprobar(Long idCosteo, String usuario, String rol) {
        return costeoService.aprobar(idCosteo, usuario, rol);
    }

    public CosteoDTO rechazar(Long idCosteo, String motivo, String usuario, String rol) {
        return costeoService.rechazar(idCosteo, motivo, usuario, rol);
    }

    public CosteoDTO reabrir(Long idCosteo) {
        return costeoService.reabrir(idCosteo);
    }

    public Optional<CosteoDTO> obtenerPorSCOS(Long scosId) {
        return costeoService.findBySolicitudCostosId(scosId);
    }

    public java.util.List<CosteoDTO> obtenerTodos() {
        return costeoService.findAll();
    }

    public java.util.List<CosteoDTO> obtenerTodosPorSCOS(Long scosId) {
        return costeoService.findAllBySolicitudCostosId(scosId);
    }

    public java.util.List<CosteoDTO> obtenerCosteosDisponiblesParaEVN() {
        return costeoService.obtenerDisponiblesParaEVN();
    }

    public Optional<CosteoResumenEVNDTO> obtenerResumenEVN(Long idCosteo) {
        if (idCosteo == null)
            throw new IllegalArgumentException("El id del costeo es obligatorio");
        return costeoService.obtenerResumenEVN(idCosteo);
    }

    public java.util.List<backend.com.produccion.application.dto.HistorialVersionCosteoDTO> obtenerHistorialVersiones(Long idCosteo) {
        java.util.List<backend.com.produccion.application.dto.HistorialVersionCosteoDTO> historial = new java.util.ArrayList<>();
        
        // 1. Obtener los snapshots históricos (las versiones cerradas/rechazadas)
        java.util.List<backend.com.produccion.domain.model.CosteoVersion> snapshots = costeoVersionRepository.findAllByCosteoId(idCosteo);
        
        java.math.BigDecimal costoAnterior = null;
        
        for (backend.com.produccion.domain.model.CosteoVersion snapshot : snapshots) {
            java.math.BigDecimal matPrima = snapshot.getCostoTotalMateriaPrima() != null ? snapshot.getCostoTotalMateriaPrima().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal mo = snapshot.getTotalManoObra() != null ? snapshot.getTotalManoObra().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal flete = snapshot.getTotalFlete() != null ? snapshot.getTotalFlete().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal embalaje = snapshot.getTotalEmbalaje() != null ? snapshot.getTotalEmbalaje().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal etiquetas = snapshot.getTotalEtiquetas() != null ? snapshot.getTotalEtiquetas().getAmount() : java.math.BigDecimal.ZERO;
            
            java.math.BigDecimal costoTotal = matPrima.add(mo).add(flete).add(embalaje).add(etiquetas);
                
            java.math.BigDecimal costoFijo = snapshot.getPorcentajeCostoFijo() != null
                ? matPrima.multiply(snapshot.getPorcentajeCostoFijo()).divide(new java.math.BigDecimal("100"), java.math.RoundingMode.HALF_UP)
                : java.math.BigDecimal.ZERO;
                
            costoTotal = costoTotal.add(costoFijo);
            
            historial.add(backend.com.produccion.application.dto.HistorialVersionCosteoDTO.builder()
                .id("v" + snapshot.getNumeroVersion())
                .version(snapshot.getNumeroVersion())
                .accion(snapshot.getMotivoCambio() != null && snapshot.getMotivoCambio().startsWith("Rechazo") ? "RECHAZO" : "MODIFICACION")
                .fechaFormateada(snapshot.getFechaCreacion() != null ? java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm").format(snapshot.getFechaCreacion()) : null)
                .costoTotal(costoTotal)
                .costoAnterior(costoAnterior)
                .usuario(snapshot.getUsuarioCreador())
                .motivo(snapshot.getMotivoCambio())
                .build());
                
            costoAnterior = costoTotal;
        }
        
        // 2. Obtener el estado activo actual
        costeoRepository.findById(idCosteo).ifPresent(costeo -> {
            java.math.BigDecimal matPrimaAct = costeo.getCostoTotalMateriaPrima() != null ? costeo.getCostoTotalMateriaPrima().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal moAct = costeo.getCostoManoObra() != null ? costeo.getCostoManoObra().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal fleteAct = costeo.getCostoFlete() != null ? costeo.getCostoFlete().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal embalajeAct = costeo.getCostoEmbalaje() != null ? costeo.getCostoEmbalaje().getAmount() : java.math.BigDecimal.ZERO;
            java.math.BigDecimal etiquetasAct = costeo.getCostoEtiquetas() != null ? costeo.getCostoEtiquetas().getAmount() : java.math.BigDecimal.ZERO;
            
            java.math.BigDecimal costoTotalAct = matPrimaAct.add(moAct).add(fleteAct).add(embalajeAct).add(etiquetasAct);
                
            java.math.BigDecimal costoFijoAct = costeo.getPorcentajeCostoFijo() != null
                ? matPrimaAct.multiply(costeo.getPorcentajeCostoFijo()).divide(new java.math.BigDecimal("100"), java.math.RoundingMode.HALF_UP)
                : java.math.BigDecimal.ZERO;
                
            costoTotalAct = costoTotalAct.add(costoFijoAct);
            
            String accion = switch (costeo.getEstado()) {
                case BORRADOR -> "CREACION"; // o "MODIFICACION" si ya hubo versiones
                case COSTEADO -> "MODIFICACION";
                case APROBADO -> "APROBACION";
                case RECHAZADO -> "RECHAZO";
            };
            
            if (accion.equals("CREACION") && !snapshots.isEmpty()) {
                accion = "MODIFICACION";
            }
            
            // Para encontrar el costo anterior, lo sacamos del último iterado
            java.math.BigDecimal cAnt = snapshots.isEmpty() ? null : historial.get(historial.size()-1).getCostoTotal();
            
            historial.add(backend.com.produccion.application.dto.HistorialVersionCosteoDTO.builder()
                .id("active")
                .version(costeo.getVersion())
                .accion(accion)
                .fechaFormateada("Actual")
                .costoTotal(costoTotalAct)
                .costoAnterior(cAnt)
                .usuario("Sistema") // O el último usuario que modificó
                .motivo(costeo.getMotivoRechazo())
                .build());
        });
        
        // Ordenamos descendentemente (más reciente primero)
        java.util.Collections.reverse(historial);
        
        return historial;
    }
}
