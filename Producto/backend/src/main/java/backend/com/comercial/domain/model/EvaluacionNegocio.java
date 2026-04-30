package backend.com.comercial.domain.model;

import backend.com.shared.events.DomainEvent;
import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class EvaluacionNegocio {
    private Long evaluacionNegocioId;
    private DocumentNumber numeroEvn;
    private Long clienteId;
    private Long vendedorId;
    private EstadoEVN estado;
    private LocalDate fechaEvaluacion;
    private Long costeoId;
    private Long solicitudCotizacionId;
    private BigDecimal porcentajeComision;
    private String clienteNombre; 
    private String referencia; 
    private String vendedorNombre; 

    private TomaTallaje tomaTallaje;
    private List<GastoAdicional> gastosAdicionales = new ArrayList<>();
    private List<ItemEVN> items = new ArrayList<>();

    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    private EvaluacionNegocio(DocumentNumber numeroEvn, Long clienteId, Long vendedorId) {
        this.numeroEvn = numeroEvn;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.estado = EstadoEVN.BORRADOR;
        this.fechaEvaluacion = LocalDate.now();
        this.porcentajeComision = BigDecimal.ZERO;
    }

    public EvaluacionNegocio(Long evaluacionNegocioId, DocumentNumber numeroEvn, Long clienteId, Long vendedorId,
            EstadoEVN estado, LocalDate fechaEvaluacion,
            TomaTallaje tomaTallaje, List<GastoAdicional> gastosAdicionales, List<ItemEVN> items,
            Long costeoId, Long solicitudCotizacionId, BigDecimal porcentajeComision,
            String clienteNombre, String referencia, String vendedorNombre) {
        this.evaluacionNegocioId = evaluacionNegocioId;
        this.numeroEvn = numeroEvn;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.estado = estado;
        this.fechaEvaluacion = fechaEvaluacion;
        this.tomaTallaje = tomaTallaje;
        this.costeoId = costeoId;
        this.solicitudCotizacionId = solicitudCotizacionId;
        this.porcentajeComision = porcentajeComision != null ? porcentajeComision : BigDecimal.ZERO;
        this.clienteNombre = clienteNombre;
        this.referencia = referencia;
        this.vendedorNombre = vendedorNombre;

        if (gastosAdicionales != null) {
            this.gastosAdicionales.addAll(gastosAdicionales);
        }

        if (items != null) {
            this.items.addAll(items);
        }
    }

    public static EvaluacionNegocio crear(DocumentNumber numero, Long clienteId, Long vendedorId,
            Long costeoId, Long solicitudCotizacionId, BigDecimal porcentajeComision,
            String clienteNombre, String referencia, String vendedorNombre) {
        EvaluacionNegocio evn = new EvaluacionNegocio(numero, clienteId, vendedorId);
        evn.costeoId = costeoId;
        evn.solicitudCotizacionId = solicitudCotizacionId;
        evn.porcentajeComision = porcentajeComision != null ? porcentajeComision : BigDecimal.ZERO;
        evn.clienteNombre = clienteNombre;
        evn.referencia = referencia;
        evn.vendedorNombre = vendedorNombre;
        return evn;
    }

    public void addItem(ItemEVN item) {
        this.items.add(item);
    }

    public void addGastoAdicional(GastoAdicional gastoAdicional) {
        this.gastosAdicionales.add(gastoAdicional);
    }

    public void setTomaTallaje(TomaTallaje tomaTallaje) {
        this.tomaTallaje = tomaTallaje;
    }

    public void aprobar() {
        if (this.estado != EstadoEVN.BORRADOR && this.estado != EstadoEVN.EVALUACION) {
            throw new EVNBusinessException(
                    "La EVN solo puede ser aprobada si está en Borrador o Evaluación");
        }
        if (items.isEmpty()) {
            throw new EVNBusinessException("No se puede aprobar una EVN sin items");
        }
        if (getMontoTotal().getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new EVNBusinessException(
                    "El monto total debe ser mayor a cero para aprobar");
        }
        this.estado = EstadoEVN.APROBADA;
    }

    public void rechazar() {
        if (this.estado != EstadoEVN.BORRADOR && this.estado != EstadoEVN.EVALUACION) {
            throw new EVNBusinessException(
                    "La EVN solo puede ser rechazada si está en Borrador o Evaluación");
        }
        this.estado = EstadoEVN.RECHAZADA;
    }

    public void adjudicar() {
        if (this.estado != EstadoEVN.BORRADOR && this.estado != EstadoEVN.EVALUACION
                && this.estado != EstadoEVN.APROBADA) {
            throw new EVNBusinessException(
                    "La EVN solo puede ser adjudicada si está activa");
        }
        this.estado = EstadoEVN.ADJUDICADA;
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

    public backend.com.shared.valueobjects.Money getMontoTotal() {
        BigDecimal total = items.stream()
                .filter(i -> i.getPrecioUnitario() != null && i.getCantidad() != null)
                .map(i -> i.getPrecioUnitario().getAmount().multiply(new BigDecimal(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new backend.com.shared.valueobjects.Money(total, "CLP");
    }

    public backend.com.shared.valueobjects.Money getCostoTotal() {
        BigDecimal costoItems = items.stream()
                .filter(i -> i.getCostoUnitario() != null && i.getCantidad() != null)
                .map(i -> i.getCostoUnitario().getAmount().multiply(new BigDecimal(i.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal costoTallaje = (tomaTallaje != null && tomaTallaje.getCostoTotal() != null)
                ? tomaTallaje.getCostoTotal().getAmount()
                : BigDecimal.ZERO;

        BigDecimal costoGastos = gastosAdicionales.stream()
                .filter(g -> g.getMonto() != null)
                .map(g -> g.getMonto().getAmount())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new backend.com.shared.valueobjects.Money(costoItems.add(costoTallaje).add(costoGastos), "CLP");
    }

    public backend.com.shared.valueobjects.Money getMontoComision() {
        if (porcentajeComision == null || porcentajeComision.compareTo(BigDecimal.ZERO) == 0) {
            return new backend.com.shared.valueobjects.Money(BigDecimal.ZERO, "CLP");
        }
        BigDecimal montoVenta = getMontoTotal().getAmount();
        BigDecimal comision = montoVenta.multiply(porcentajeComision)
                .divide(new BigDecimal("100"), 2, java.math.RoundingMode.HALF_UP);
        return new backend.com.shared.valueobjects.Money(comision, "CLP");
    }

    public BigDecimal getMargenGanancia() {
        BigDecimal baseMargen = getMontoTotal().getAmount().subtract(getCostoTotal().getAmount());
        return baseMargen.subtract(getMontoComision().getAmount());
    }

    public BigDecimal getRentabilidadEsperada() {
        BigDecimal monto = getMontoTotal().getAmount();
        if (monto.compareTo(BigDecimal.ZERO) == 0)
            return BigDecimal.ZERO;

        BigDecimal margen = getMargenGanancia();
        return margen.divide(monto, 4, java.math.RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
    }
}
