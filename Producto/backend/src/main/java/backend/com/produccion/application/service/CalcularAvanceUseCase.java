package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalcularAvanceUseCase {

    private final OrdenTrabajoRepository otRepository;

    public AvanceOPResponse calcular(Long ordenProduccionId) {
        List<OrdenTrabajo> ots = otRepository.findByOrdenProduccionId(ordenProduccionId);

        AvanceOPResponse resp = new AvanceOPResponse();
        resp.setOrdenProduccionId(ordenProduccionId);

        int totalUnidades = 0;
        int totalProducidas = 0;
        int totalMermas = 0;
        for (OrdenTrabajo ot : ots) {
            totalUnidades += ot.getCantidadTotal() != null ? ot.getCantidadTotal() : 0;
            totalProducidas += ot.getCantidadProducida() != null ? ot.getCantidadProducida() : 0;
            totalMermas += ot.getCantidadMerma() != null ? ot.getCantidadMerma() : 0;
        }
        resp.setTotalUnidades(totalUnidades);
        resp.setTotalProducidas(totalProducidas);
        resp.setTotalMermas(totalMermas);

        BigDecimal pct = BigDecimal.ZERO;
        if (totalUnidades > 0) {
            pct = new BigDecimal(totalProducidas + totalMermas)
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(totalUnidades), 2, RoundingMode.HALF_UP);
        }
        resp.setPorcentajeGlobal(pct);
        resp.setSemaforo(semaforo(pct));

        resp.setOrdenesTrabajo(ots.stream().map(this::toAvanceOT).collect(Collectors.toList()));
        return resp;
    }

    private AvanceOPResponse.AvanceOT toAvanceOT(OrdenTrabajo ot) {
        AvanceOPResponse.AvanceOT a = new AvanceOPResponse.AvanceOT();
        a.setIdOT(ot.getIdOT());
        a.setNumeroOT(ot.getNumeroOT() != null ? ot.getNumeroOT().getValue() : null);
        a.setFase(ot.getFase() != null ? ot.getFase().name() : null);
        a.setEstado(ot.getEstadoOT() != null ? ot.getEstadoOT().name() : null);
        a.setCantidadTotal(ot.getCantidadTotal());
        a.setCantidadProducida(ot.getCantidadProducida());
        a.setCantidadMerma(ot.getCantidadMerma());
        a.setPorcentajeAvance(ot.getPorcentajeAvance());
        return a;
    }

    private String semaforo(BigDecimal pct) {
        if (pct == null) return "ROJO";
        int p = pct.intValue();
        if (p >= 100) return "VERDE";
        if (p >= 60) return "AMARILLO";
        if (p > 0) return "NARANJA";
        return "ROJO";
    }
}
