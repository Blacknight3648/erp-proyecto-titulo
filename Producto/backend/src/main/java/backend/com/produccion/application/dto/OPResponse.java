package backend.com.produccion.application.dto;

import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OPResponse {
    private Long idOP;
    private String numeroOP;
    private Long notaVentaId;
    private String estado;
    private LocalDate fechaInicio;
    private LocalDate fechaEntregaProgramada;
    private String observaciones;
    private List<OPItemResponse> items;

    public static OPResponse fromDomain(OrdenProduccion op) {
        if (op == null)
            return null;
        OPResponse r = new OPResponse();
        r.idOP = op.getIdOP();
        r.numeroOP = op.getNumeroOP() != null ? op.getNumeroOP().getValue() : null;
        r.notaVentaId = op.getNotaVentaId();
        r.estado = op.getEstado() != null ? op.getEstado().name() : null;
        r.fechaInicio = op.getFechaInicio();
        r.fechaEntregaProgramada = op.getFechaEntregaProgramada();
        r.observaciones = op.getObservaciones();
        r.items = op.getItems() != null
                ? op.getItems().stream().map(OPItemResponse::fromDomain).collect(Collectors.toList())
                : List.of();
        return r;
    }

    @Data
    public static class OPItemResponse {
        private Long idOPItem;
        private Integer articuloId;
        private Integer nroItem;
        private String modelo;
        private String tela;
        private String color;
        private String talla;
        private String llevaLogo;
        private Integer cantidad;

        public static OPItemResponse fromDomain(OrdenProduccionItem item) {
            OPItemResponse r = new OPItemResponse();
            r.idOPItem = item.getIdOPItem();
            r.articuloId = item.getArticuloId();
            r.nroItem = item.getNroItem();
            r.modelo = item.getModelo();
            r.tela = item.getTela();
            r.color = item.getColor();
            r.talla = item.getTalla();
            r.llevaLogo = item.getLlevaLogo();
            r.cantidad = item.getCantidad();
            return r;
        }
    }
}
