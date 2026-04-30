package backend.com.produccion.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

@Getter
public class OrdenTrabajo {
    private Long idOT;
    private DocumentNumber numeroOT;
    private Long notaVentaId;
    private Integer nroItem; // Referencia al ítem específico de la NV
    private TipoOT tipoOT;
    private EstadoOT estadoOT;
    private String observaciones;

    public OrdenTrabajo(Long id, DocumentNumber numeroOT, Long notaVentaId, Integer nroItem,
            TipoOT tipoOT, EstadoOT estadoOT, String observaciones) {
        this.idOT = id;
        this.numeroOT = numeroOT;
        this.notaVentaId = notaVentaId;
        this.nroItem = nroItem;
        this.tipoOT = tipoOT != null ? tipoOT : TipoOT.INTERNA;
        this.estadoOT = estadoOT != null ? estadoOT : EstadoOT.PENDIENTE;
        this.observaciones = observaciones;
    }

    public static OrdenTrabajo crearParaItem(DocumentNumber numero, Long notaVentaId, Integer nroItem, String obs) {
        return new OrdenTrabajo(null, numero, notaVentaId, nroItem, TipoOT.INTERNA, EstadoOT.PENDIENTE, obs);
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
}
