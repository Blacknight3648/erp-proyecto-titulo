package backend.com.comercial.domain.model;

import backend.com.shared.events.DomainEvent;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class NotaVenta {
    private Long idNV;
    private DocumentNumber numeroNV;
    private Long evaluacionNegocioId;
    private Long clienteId;
    private Long vendedorId;
    private EstadoNV estado;
    private Boolean esKit;
    private String detalleKit;
    private LocalDate fechaEmision;
    private LocalDate fechaEntregaEstimada;
    private Money montoSubtotal;
    private Money montoIva;
    private Money montoTotal;

    private List<ItemNV> items = new ArrayList<>();

    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    private NotaVenta(DocumentNumber numeroNV, Long evaluacionNegocioId, Long clienteId, Long vendedorId,
            Boolean esKit, String detalleKit, LocalDate fechaEntregaEstimada) {
        this.numeroNV = numeroNV;
        this.evaluacionNegocioId = evaluacionNegocioId;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.estado = EstadoNV.BORRADOR;
        this.esKit = esKit != null ? esKit : false;
        this.detalleKit = detalleKit;
        this.fechaEmision = LocalDate.now();
        this.fechaEntregaEstimada = fechaEntregaEstimada;

        this.montoSubtotal = new Money(BigDecimal.ZERO, "CLP");
        this.montoIva = new Money(BigDecimal.ZERO, "CLP");
        this.montoTotal = new Money(BigDecimal.ZERO, "CLP");
    }

    public NotaVenta(Long idNV, DocumentNumber numeroNV, Long evaluacionNegocioId, Long clienteId, Long vendedorId,
            EstadoNV estado, Boolean esKit, String detalleKit, LocalDate fechaEmision, LocalDate fechaEntregaEstimada,
            Money montoSubtotal, Money montoIva, Money montoTotal, List<ItemNV> items) {
        this.idNV = idNV;
        this.numeroNV = numeroNV;
        this.evaluacionNegocioId = evaluacionNegocioId;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.estado = estado;
        this.esKit = esKit;
        this.detalleKit = detalleKit;
        this.fechaEmision = fechaEmision;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.montoSubtotal = montoSubtotal;
        this.montoIva = montoIva;
        this.montoTotal = montoTotal;
        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static NotaVenta crear(DocumentNumber numeroNV, Long evaluacionNegocioId, Long clienteId,
            Long vendedorId, Boolean esKit, String detalleKit, LocalDate fechaEntregaEstimada) {
        return new NotaVenta(numeroNV, evaluacionNegocioId, clienteId, vendedorId, esKit, detalleKit,
                fechaEntregaEstimada);
    }

    public void addItem(ItemNV item) {
        this.items.add(item);
        calcularTotales();
    }

    public void calcularTotales() {
        Money subtotal = new Money(BigDecimal.ZERO, "CLP");

        for (ItemNV item : items) {
            subtotal = subtotal.add(item.getTotal());
        }

        this.montoSubtotal = subtotal;

        // Asumiendo un IVA del 19% (Chile). Esto podrÃ­a venir por parÃ¡metro o
        // configuraciÃ³n.
        BigDecimal ivaAmount = subtotal.getAmount().multiply(new BigDecimal("0.19"))
                .setScale(0, java.math.RoundingMode.HALF_UP);
        this.montoIva = new Money(ivaAmount, "CLP");

        this.montoTotal = this.montoSubtotal.add(this.montoIva);
    }

    public void aprobar() {
        if (this.estado != EstadoNV.BORRADOR) {
            throw new IllegalStateException("Solo las Notas de Venta en Borrador pueden ser aprobadas");
        }
        this.estado = EstadoNV.APROBADA;
        // AquÃ­ eventualmente agregaremos: this.addDomainEvent(new
        // NotaVentaAprobadaEvent(...))
    }

    public void cancelar() {
        if (this.estado == EstadoNV.COMPLETADA || this.estado == EstadoNV.ENTREGADA) {
            throw new IllegalStateException("No se puede cancelar una Nota de Venta completada o entregada");
        }
        this.estado = EstadoNV.CANCELADA;
    }

    protected void addDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
}
