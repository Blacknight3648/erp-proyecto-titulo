package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoOC;
import backend.com.shared.exception.BusinessRuleException;
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
    /** Motivo del último rechazo (solo poblado cuando estado == RECHAZADA). */
    private String motivoRechazo;
    /**
     * Iteración de reingreso de la OC. Arranca en 1 y se incrementa cada vez que
     * se reingresa (rechazar → reingresar) una OC previamente RECHAZADA. El
     * número de documento ({@code numeroOC}) no cambia entre reingresos.
     */
    private Integer version = 1;

    public OrdenCompra(Long idOC, DocumentNumber numeroOC, Long proveedorId, EstadoOC estado,
            LocalDate fechaEmision, LocalDate fechaEntregaEstimada, String observaciones,
            BigDecimal totalNeto, List<OrdenCompraItem> items) {
        this(idOC, numeroOC, proveedorId, estado, fechaEmision, fechaEntregaEstimada, observaciones,
                totalNeto, items, null, 1);
    }

    public OrdenCompra(Long idOC, DocumentNumber numeroOC, Long proveedorId, EstadoOC estado,
            LocalDate fechaEmision, LocalDate fechaEntregaEstimada, String observaciones,
            BigDecimal totalNeto, List<OrdenCompraItem> items, String motivoRechazo, Integer version) {
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
        this.motivoRechazo = motivoRechazo;
        this.version = version != null ? version : 1;
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

    /**
     * Rechaza la OC (solo desde EMITIDA). Exige un motivo y lo conserva.
     */
    public void rechazar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new BusinessRuleException("Debe indicarse el motivo del rechazo de la Orden de Compra");
        }
        transicionar(EstadoOC.RECHAZADA);
        this.motivoRechazo = motivo;
    }

    /**
     * Reingresa una OC RECHAZADA: vuelve a EMITIDA con el MISMO número de
     * documento, limpia el motivo de rechazo e incrementa la versión (nueva
     * iteración de reingreso). Permite corregir el proveedor en el mismo
     * paso (el motivo típico de rechazo es que el proveedor no tenía stock).
     */
    public void reingresar(Long nuevoProveedorId) {
        transicionar(EstadoOC.EMITIDA);
        this.motivoRechazo = null;
        this.version = (this.version == null ? 1 : this.version) + 1;
        if (nuevoProveedorId != null) {
            this.proveedorId = nuevoProveedorId;
        }
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
