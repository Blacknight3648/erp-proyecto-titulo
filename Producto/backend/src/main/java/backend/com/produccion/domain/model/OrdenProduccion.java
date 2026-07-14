package backend.com.produccion.domain.model;

import backend.com.produccion.domain.enums.EstadoOP;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class OrdenProduccion {
    private Long idOP;
    private Long costeoVersionId;
    private DocumentNumber numeroOP;
    private Long notaVentaId;
    private EstadoOP estado;
    private LocalDate fechaInicio;
    private LocalDate fechaEntregaProgramada;
    private String observaciones;
    private SeguimientoOP seguimiento;
    private List<OrdenProduccionItem> items = new ArrayList<>();

    public OrdenProduccion(Long id, Long costeoVersionId, DocumentNumber numero, Long notaVentaId, EstadoOP estado,
            LocalDate fechaInicio, LocalDate fechaEntregaProgramada, String observaciones,
            List<OrdenProduccionItem> items) {
        this.idOP = id;
        this.costeoVersionId = costeoVersionId;
        this.numeroOP = numero;
        this.notaVentaId = notaVentaId;
        this.estado = estado != null ? estado : EstadoOP.PENDIENTE;
        this.fechaInicio = fechaInicio;
        this.fechaEntregaProgramada = fechaEntregaProgramada;
        this.observaciones = observaciones;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static OrdenProduccion crearNueva(DocumentNumber numero, Long notaVentaId, LocalDate fechaEntrega) {
        OrdenProduccion op = new OrdenProduccion(null, null, numero, notaVentaId, EstadoOP.PENDIENTE, null, fechaEntrega, null,
                new ArrayList<>());
        op.seguimiento = new SeguimientoOP();
        return op;
    }

    public void vincularCosteoVersion(Long costeoVersionId) {
        if (this.costeoVersionId != null) {
            throw new IllegalStateException("La OP ya tiene un costeo asignado");
        }
        this.costeoVersionId = costeoVersionId;
    }

    public void recepcionar() {
        if (this.estado != EstadoOP.PENDIENTE) {
            throw new IllegalStateException("Solo las OPs pendientes pueden ser recepcionadas");
        }
        this.estado = EstadoOP.EN_PROCESO;
        this.fechaInicio = LocalDate.now();
    }

    public void addItem(OrdenProduccionItem item) {
        this.items.add(item);
    }

    public List<OrdenProduccionItem> getItems() {
        return Collections.unmodifiableList(items);
    }

    public void setSeguimiento(SeguimientoOP seguimiento) {
        this.seguimiento = seguimiento;
        if (seguimiento != null) {
            seguimiento.setOrdenProduccionId(this.idOP);
        }
    }
}
