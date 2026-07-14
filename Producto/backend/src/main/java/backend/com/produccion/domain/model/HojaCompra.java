package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoHC;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class HojaCompra {

    private Long idHC;
    private DocumentNumber numeroHC;
    private Long opId;
    private Long costeoVersionId;
    private EstadoHC estado;
    private LocalDate fechaGeneracion;
    private String observaciones;
    private List<HojaCompraItem> items = new ArrayList<>();

    public HojaCompra(Long idHC, DocumentNumber numeroHC, Long opId, Long costeoVersionId,
            EstadoHC estado, LocalDate fechaGeneracion, String observaciones,
            List<HojaCompraItem> items) {
        this.idHC = idHC;
        this.numeroHC = numeroHC;
        this.opId = opId;
        this.costeoVersionId = costeoVersionId;
        this.estado = estado != null ? estado : EstadoHC.BORRADOR;
        this.fechaGeneracion = fechaGeneracion != null ? fechaGeneracion : LocalDate.now();
        this.observaciones = observaciones;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static HojaCompra crearBorrador(DocumentNumber numeroHC, Long opId, Long costeoVersionId) {
        return new HojaCompra(null, numeroHC, opId, costeoVersionId,
                EstadoHC.BORRADOR, LocalDate.now(), null, new ArrayList<>());
    }

    public void aprobar() {
        if (!this.estado.puedeTransicionarA(EstadoHC.APROBADA)) {
            throw new IllegalStateException(
                    "No se puede aprobar una HC en estado " + this.estado);
        }
        this.estado = EstadoHC.APROBADA;
    }

    public void cerrar() {
        if (!this.estado.puedeTransicionarA(EstadoHC.CERRADA)) {
            throw new IllegalStateException(
                    "No se puede cerrar una HC en estado " + this.estado);
        }
        this.estado = EstadoHC.CERRADA;
    }

    public void reabrir() {
        this.estado = EstadoHC.BORRADOR;
    }

    public void addItem(HojaCompraItem item) {
        this.items.add(item);
    }

    public List<HojaCompraItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
