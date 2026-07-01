package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.domain.repository.SeguimientoOPRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CalcularAvanceUseCase {

    private final SeguimientoOPRepository seguimientoOPRepository;

    public AvanceOPResponse calcular(Long ordenProduccionId) {
        Optional<SeguimientoOP> optSeg = seguimientoOPRepository.findByOrdenProduccionId(ordenProduccionId);
        AvanceOPResponse resp = new AvanceOPResponse();
        resp.setOrdenProduccionId(ordenProduccionId);

        if (optSeg.isEmpty()) {
            resp.setPorcentajeGlobal(BigDecimal.ZERO);
            resp.setSemaforo(semaforo(BigDecimal.ZERO));
            return resp;
        }

        SeguimientoOP seg = optSeg.get();
        resp.setFechaRecepcionOp(seg.getFechaRecepcionOp());
        resp.setFinTizado(seg.getFinTizado());
        resp.setEstadoOcMp(seg.getEstadoOcMp() != null ? seg.getEstadoOcMp().name() : null);
        resp.setRecepcionCompras(seg.getRecepcionCompras());
        resp.setInicioCorte(seg.getInicioCorte());
        resp.setFinCorte(seg.getFinCorte());
        resp.setInicioLogo(seg.getInicioLogo());
        resp.setEstadoIdaLogo(seg.getEstadoIdaLogo() != null ? seg.getEstadoIdaLogo().name() : null);
        resp.setRegresoLogo(seg.getRegresoLogo());
        resp.setEstadoRecLogo(seg.getEstadoRecLogo() != null ? seg.getEstadoRecLogo().name() : null);
        resp.setInicioTallerExterno(seg.getInicioTallerExterno());
        resp.setFinTallerExterno(seg.getFinTallerExterno());
        resp.setCalidadTaller(seg.getCalidadTaller() != null ? seg.getCalidadTaller().name() : null);
        resp.setObsTaller(seg.getObsTaller());
        resp.setFinTerminacion(seg.getFinTerminacion());
        resp.setFinPersonalizado(seg.getFinPersonalizado());

        int camposLlenos = 0;
        int totalCampos = 15;

        if (seg.getFechaRecepcionOp() != null) camposLlenos++;
        if (seg.getFinTizado() != null) camposLlenos++;
        if (seg.getEstadoOcMp() != null) camposLlenos++;
        if (seg.getRecepcionCompras() != null) camposLlenos++;
        if (seg.getInicioCorte() != null) camposLlenos++;
        if (seg.getFinCorte() != null) camposLlenos++;
        if (seg.getInicioLogo() != null) camposLlenos++;
        if (seg.getEstadoIdaLogo() != null) camposLlenos++;
        if (seg.getRegresoLogo() != null) camposLlenos++;
        if (seg.getEstadoRecLogo() != null) camposLlenos++;
        if (seg.getInicioTallerExterno() != null) camposLlenos++;
        if (seg.getFinTallerExterno() != null) camposLlenos++;
        if (seg.getCalidadTaller() != null) camposLlenos++;
        if (seg.getFinTerminacion() != null) camposLlenos++;
        if (seg.getFinPersonalizado() != null) camposLlenos++;

        BigDecimal pct = new BigDecimal(camposLlenos)
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(totalCampos), 2, RoundingMode.HALF_UP);

        resp.setPorcentajeGlobal(pct);
        resp.setSemaforo(semaforo(pct));
        
        return resp;
    }

    private String semaforo(BigDecimal pct) {
        if (pct == null)
            return "ROJO";
        int p = pct.intValue();
        if (p >= 100)
            return "VERDE";
        if (p >= 60)
            return "AMARILLO";
        if (p > 0)
            return "NARANJA";
        return "ROJO";
    }
}
