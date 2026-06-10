package backend.com.produccion.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class OrdenTrabajo {
    private Long idOT;
    private DocumentNumber numeroOT;
    private Long notaVentaId;
    private Long itemNVId;
    private Long ordenProduccionId;
    private Integer nroItem;
    private TipoOT tipoOT;
    private FaseProduccion fase;
    private EstadoOT estadoOT;
    private Integer cantidadTotal;
    private Integer cantidadProducida;
    private Integer cantidadMerma;
    private String observaciones;

    public OrdenTrabajo(Long id, DocumentNumber numeroOT, Long notaVentaId, Long itemNVId, Long ordenProduccionId,
            Integer nroItem, TipoOT tipoOT, FaseProduccion fase, EstadoOT estadoOT,
            Integer cantidadTotal, Integer cantidadProducida, Integer cantidadMerma,
            String observaciones) {
        this.idOT = id;
        this.numeroOT = numeroOT;
        this.notaVentaId = notaVentaId;
        this.itemNVId = itemNVId;
        this.ordenProduccionId = ordenProduccionId;
        this.nroItem = nroItem;
        this.tipoOT = tipoOT != null ? tipoOT : TipoOT.INTERNA;
        this.fase = fase;
        this.estadoOT = estadoOT != null ? estadoOT : EstadoOT.PENDIENTE;
        this.cantidadTotal = cantidadTotal != null ? cantidadTotal : 0;
        this.cantidadProducida = cantidadProducida != null ? cantidadProducida : 0;
        this.cantidadMerma = cantidadMerma != null ? cantidadMerma : 0;
        this.observaciones = observaciones;
    }

    public static OrdenTrabajo crearParaItem(DocumentNumber numero, Long notaVentaId, Long itemNVId,
            Integer nroItem, String obs) {
        return new OrdenTrabajo(null, numero, notaVentaId, itemNVId, null, nroItem, TipoOT.INTERNA, null,
                EstadoOT.PENDIENTE, 0, 0, 0, obs);
    }

    public static OrdenTrabajo crearParaFase(DocumentNumber numero, Long notaVentaId, Long itemNVId, Long opId,
            Integer nroItem, FaseProduccion fase, Integer cantidadTotal, String obs) {
        return new OrdenTrabajo(null, numero, notaVentaId, itemNVId, opId, nroItem, TipoOT.INTERNA, fase,
                EstadoOT.PENDIENTE, cantidadTotal, 0, 0, obs);
    }

    public void iniciar() {
        if (this.estadoOT != EstadoOT.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden iniciar OTs pendientes");
        }
        this.estadoOT = EstadoOT.EN_PROCESO;
    }

    public void finalizar() {
        this.estadoOT = EstadoOT.FINALIZADA;
    }

    public void registrarAvance(int producida, int merma) {
        if (producida < 0 || merma < 0) {
            throw new IllegalArgumentException("Las cantidades no pueden ser negativas");
        }
        if (this.estadoOT == EstadoOT.FINALIZADA) {
            throw new IllegalStateException("No se puede registrar avance en una OT finalizada");
        }
        this.cantidadProducida += producida;
        this.cantidadMerma += merma;
        if (this.estadoOT == EstadoOT.PENDIENTE && (producida > 0 || merma > 0)) {
            this.estadoOT = EstadoOT.EN_PROCESO;
        }
        if (this.cantidadTotal != null && this.cantidadTotal > 0
                && (this.cantidadProducida + this.cantidadMerma) >= this.cantidadTotal) {
            this.estadoOT = EstadoOT.FINALIZADA;
        }
    }

    public BigDecimal getPorcentajeAvance() {
        if (cantidadTotal == null || cantidadTotal == 0) {
            return BigDecimal.ZERO;
        }
        int procesado = (cantidadProducida != null ? cantidadProducida : 0)
                + (cantidadMerma != null ? cantidadMerma : 0);
        return new BigDecimal(procesado)
                .multiply(new BigDecimal("100"))
                .divide(new BigDecimal(cantidadTotal), 2, RoundingMode.HALF_UP);
    }
}
