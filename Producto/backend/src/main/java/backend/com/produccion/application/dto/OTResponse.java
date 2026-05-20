package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.OrdenTrabajo;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OTResponse {
    private Long idOT;
    private String numeroOT;
    private Long notaVentaId;
    private Long ordenProduccionId;
    private Integer nroItem;
    private String tipoOT;
    private String fase;
    private String estadoOT;
    private Integer cantidadTotal;
    private Integer cantidadProducida;
    private Integer cantidadMerma;
    private BigDecimal porcentajeAvance;
    private String observaciones;

    public static OTResponse fromDomain(OrdenTrabajo ot) {
        if (ot == null) return null;
        OTResponse r = new OTResponse();
        r.idOT = ot.getIdOT();
        r.numeroOT = ot.getNumeroOT() != null ? ot.getNumeroOT().getValue() : null;
        r.notaVentaId = ot.getNotaVentaId();
        r.ordenProduccionId = ot.getOrdenProduccionId();
        r.nroItem = ot.getNroItem();
        r.tipoOT = ot.getTipoOT() != null ? ot.getTipoOT().name() : null;
        r.fase = ot.getFase() != null ? ot.getFase().name() : null;
        r.estadoOT = ot.getEstadoOT() != null ? ot.getEstadoOT().name() : null;
        r.cantidadTotal = ot.getCantidadTotal();
        r.cantidadProducida = ot.getCantidadProducida();
        r.cantidadMerma = ot.getCantidadMerma();
        r.porcentajeAvance = ot.getPorcentajeAvance();
        r.observaciones = ot.getObservaciones();
        return r;
    }
}
