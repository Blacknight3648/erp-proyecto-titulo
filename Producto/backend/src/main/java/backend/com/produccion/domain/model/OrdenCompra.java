package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class OrdenCompra {

    private Long idOC;
    private DocumentNumber numeroOC;
    private Long proveedorId;
    private EstadoOC estado;
    private LocalDate fechaEmision;
    private LocalDate fechaEntregaEstimada;
    private String observaciones;
    private BigDecimal totalNeto;
    private List<OrdenCompraItem> items = new ArrayList<>();

    public OrdenCompra(Long idOC, DocumentNumber numeroOC, Long proveedorId, EstadoOC estado,
            LocalDate fechaEmision, LocalDate fechaEntregaEstimada, String observaciones,
            BigDecimal totalNeto, List<OrdenCompraItem> items) {
        this.idOC = idOC;
        this.numeroOC = numeroOC;
        this.proveedorId = proveedorId;
        this.estado = estado != null ? estado : EstadoOC.EMITIDA;
        this.fechaEmision = fechaEmision != null ? fechaEmision : LocalDate.now();
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.observaciones = observaciones;
        this.totalNeto = totalNeto != null ? totalNeto : BigDecimal.ZERO;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static OrdenCompra emitir(DocumentNumber numeroOC, Long proveedorId,
            LocalDate fechaEntregaEstimada, String observaciones) {
        return new OrdenCompra(null, numeroOC, proveedorId, EstadoOC.EMITIDA,
                LocalDate.now(), fechaEntregaEstimada, observaciones, BigDecimal.ZERO, new ArrayList<>());
    }

    public void addItem(OrdenCompraItem item) {
        this.items.add(item);
        recalcularTotal();
    }

    public void recalcularTotal() {
        this.totalNeto = items.stream()
                .map(OrdenCompraItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void marcarEnviada() {
        transicionar(EstadoOC.ENVIADA);
    }

    public void marcarRecepcionParcial() {
        transicionar(EstadoOC.RECEPCIONADA_PARCIAL);
    }

    public void marcarRecepcionada() {
        transicionar(EstadoOC.RECEPCIONADA);
    }

    public void cerrar() {
        transicionar(EstadoOC.CERRADA);
    }

    private void transicionar(EstadoOC destino) {
        if (!this.estado.puedeTransicionarA(destino)) {
            throw new IllegalStateException(
                    "Transición inválida de OC: " + this.estado + " -> " + destino);
        }
        this.estado = destino;
    }

    public List<OrdenCompraItem> getItems() {
        return Collections.unmodifiableList(items);
    }
}
