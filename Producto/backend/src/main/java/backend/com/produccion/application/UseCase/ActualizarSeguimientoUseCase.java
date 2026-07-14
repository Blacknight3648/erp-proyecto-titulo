package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.ActualizarSeguimientoCommand;
import backend.com.produccion.application.dto.SeguimientoOPDTO;
import backend.com.produccion.domain.enums.CalidadTaller;
import backend.com.produccion.domain.enums.EstadoIdaLogo;
import backend.com.produccion.domain.enums.EstadoRecLogo;
import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.SeguimientoOPRepository;
import backend.com.shared.application.service.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActualizarSeguimientoUseCase {

    private final SeguimientoOPRepository seguimientoRepository;
    private final OrdenProduccionRepository ordenProduccionRepository;
    private final NotificacionService notificacionService;

    @Transactional
    public SeguimientoOPDTO actualizar(Long opId, ActualizarSeguimientoCommand cmd) {
        SeguimientoOP seg = seguimientoRepository.findByOrdenProduccionId(opId)
                .orElseGet(() -> new SeguimientoOP(opId));

        boolean logoYaRecibido = seg.getRegresoLogo() != null;
        boolean comprasYaRecibidas = seg.getRecepcionCompras() != null;

        seg.setFechaRecepcionOp(cmd.getFechaRecepcionOp());
        seg.setFinTizado(cmd.getFinTizado());
        
        if (cmd.getEstadoOcMp() != null) {
            seg.setEstadoOcMp(backend.com.produccion.domain.enums.EstadoOcMp.valueOf(cmd.getEstadoOcMp()));
        } else {
            seg.setEstadoOcMp(null);
        }
        
        seg.setRecepcionCompras(cmd.getRecepcionCompras());
        seg.setInicioCorte(cmd.getInicioCorte());
        seg.setFinCorte(cmd.getFinCorte());
        seg.setInicioLogo(cmd.getInicioLogo());

        if (cmd.getEstadoIdaLogo() != null) {
            seg.setEstadoIdaLogo(EstadoIdaLogo.valueOf(cmd.getEstadoIdaLogo()));
        } else {
            seg.setEstadoIdaLogo(null);
        }

        seg.setRegresoLogo(cmd.getRegresoLogo());

        if (cmd.getEstadoRecLogo() != null) {
            seg.setEstadoRecLogo(EstadoRecLogo.valueOf(cmd.getEstadoRecLogo()));
        } else {
            seg.setEstadoRecLogo(null);
        }

        seg.setInicioTallerExterno(cmd.getInicioTallerExterno());
        seg.setFinTallerExterno(cmd.getFinTallerExterno());

        if (cmd.getCalidadTaller() != null) {
            seg.setCalidadTaller(CalidadTaller.valueOf(cmd.getCalidadTaller()));
        } else {
            seg.setCalidadTaller(null);
        }

        seg.setObsTaller(cmd.getObsTaller());
        seg.setFinTerminacion(cmd.getFinTerminacion());
        seg.setFinPersonalizado(cmd.getFinPersonalizado());

        SeguimientoOP saved = seguimientoRepository.save(seg);

        if (!logoYaRecibido && saved.getRegresoLogo() != null) {
            notificacionService.crear("LOGO", "Logo recepcionado para " + numeroOP(opId), "PRODUCCION", "medium");
        }
        if (!comprasYaRecibidas && saved.getRecepcionCompras() != null) {
            notificacionService.crear("MP", "Recepción de compras registrada para " + numeroOP(opId), "BODEGA", "normal");
        }

        return SeguimientoOPDTO.from(saved);
    }

    private String numeroOP(Long opId) {
        return ordenProduccionRepository.findById(opId)
                .map(op -> op.getNumeroOP() != null ? op.getNumeroOP().getValue() : ("OP #" + opId))
                .orElse("OP #" + opId);
    }
}
