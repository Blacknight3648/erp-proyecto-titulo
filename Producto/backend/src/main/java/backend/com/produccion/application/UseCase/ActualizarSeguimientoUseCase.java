package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.ActualizarSeguimientoCommand;
import backend.com.produccion.application.dto.SeguimientoOPDTO;
import backend.com.produccion.domain.enums.CalidadTaller;
import backend.com.produccion.domain.enums.EstadoIdaLogo;
import backend.com.produccion.domain.enums.EstadoRecLogo;
import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.domain.repository.SeguimientoOPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ActualizarSeguimientoUseCase {

    private final SeguimientoOPRepository seguimientoRepository;

    @Transactional
    public SeguimientoOPDTO actualizar(Long opId, ActualizarSeguimientoCommand cmd) {
        SeguimientoOP seg = seguimientoRepository.findByOrdenProduccionId(opId)
                .orElseGet(() -> new SeguimientoOP(opId));

        seg.setFechaRecepcionOp(cmd.getFechaRecepcionOp());
        seg.setFinTizado(cmd.getFinTizado());
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

        return SeguimientoOPDTO.from(saved);
    }
}
