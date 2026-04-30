package backend.com.produccion.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class OrdenProduccion {
    private Long idOP;
    private DocumentNumber numeroOP;
    private Long notaVentaId;
    private EstadoOP estado;
    private LocalDate fechaInicio;
    private LocalDate fechaEntregaProgramada;
    private String observaciones;
    private List<OrdenProduccionItem> items = new ArrayList<>();

    public OrdenProduccion(Long id, DocumentNumber numero, Long notaVentaId, EstadoOP estado,
            LocalDate fechaInicio, LocalDate fechaEntregaProgramada, String observaciones,
            List<OrdenProduccionItem> items) {
        this.idOP = id;
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
        return new OrdenProduccion(null, numero, notaVentaId, EstadoOP.PENDIENTE, null, fechaEntrega, null,
                new ArrayList<>());
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
}
