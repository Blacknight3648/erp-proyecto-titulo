package backend.com.produccion.domain.model;

import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class OrdenServicio {

    private Long idOS;
    private DocumentNumber numeroOS;
    private Long opId;
    private Long proveedorId;
    private TipoServicioOS tipoServicio;
    private EstadoOS estado;
    private LocalDate fechaEmision;
    private LocalDate fechaEntregaEstimada;
    private String descripcionTrabajo;
    private Integer cantidadPactada;
    private BigDecimal precioUnitario;
    private BigDecimal totalNeto;
    private String observaciones;
    private List<DespachoOS> despachos = new ArrayList<>();
    private List<RecepcionOS> recepciones = new ArrayList<>();

    public OrdenServicio(Long idOS, DocumentNumber numeroOS, Long opId, Long proveedorId,
            TipoServicioOS tipoServicio, EstadoOS estado, LocalDate fechaEmision,
            LocalDate fechaEntregaEstimada, String descripcionTrabajo, Integer cantidadPactada,
            BigDecimal precioUnitario, BigDecimal totalNeto, String observaciones,
            List<DespachoOS> despachos, List<RecepcionOS> recepciones) {
        this.idOS = idOS;
        this.numeroOS = numeroOS;
        this.opId = opId;
        this.proveedorId = proveedorId;
        this.tipoServicio = tipoServicio;
        this.estado = estado != null ? estado : EstadoOS.EMITIDA;
        this.fechaEmision = fechaEmision != null ? fechaEmision : LocalDate.now();
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.descripcionTrabajo = descripcionTrabajo;
        this.cantidadPactada = cantidadPactada != null ? cantidadPactada : 0;
        this.precioUnitario = precioUnitario != null ? precioUnitario : BigDecimal.ZERO;
        this.totalNeto = totalNeto != null ? totalNeto
                : this.precioUnitario.multiply(BigDecimal.valueOf(this.cantidadPactada));
        this.observaciones = observaciones;
        if (despachos != null) this.despachos.addAll(despachos);
        if (recepciones != null) this.recepciones.addAll(recepciones);
    }

    public static OrdenServicio emitir(DocumentNumber numeroOS, Long opId, Long proveedorId,
            TipoServicioOS tipoServicio, LocalDate fechaEntregaEstimada,
            String descripcionTrabajo, Integer cantidadPactada, BigDecimal precioUnitario,
            String observaciones) {
        return new OrdenServicio(null, numeroOS, opId, proveedorId, tipoServicio,
                EstadoOS.EMITIDA, LocalDate.now(), fechaEntregaEstimada, descripcionTrabajo,
                cantidadPactada, precioUnitario, null, observaciones,
                new ArrayList<>(), new ArrayList<>());
    }

    public void registrarDespacho(DespachoOS despacho) {
        if (despacho == null) {
            throw new IllegalArgumentException("El despacho no puede ser nulo");
        }
        if (this.estado == EstadoOS.CERRADA) {
            throw new IllegalStateException("No se puede despachar una OS CERRADA");
        }
        Integer totalDespachado = totalDespachado() + (despacho.getCantidadDespachada() != null ? despacho.getCantidadDespachada() : 0);
        if (totalDespachado > this.cantidadPactada) {
            throw new IllegalStateException(
                    "El total despachado (" + totalDespachado + ") excede la cantidad pactada (" + this.cantidadPactada + ")");
        }
        this.despachos.add(despacho);
        if (this.estado == EstadoOS.EMITIDA) {
            transicionar(EstadoOS.EN_PROCESO);
        }
    }

    public void registrarRecepcion(RecepcionOS recepcion) {
        if (recepcion == null) {
            throw new IllegalArgumentException("La recepción no puede ser nula");
        }
        if (this.estado == EstadoOS.EMITIDA) {
            throw new IllegalStateException(
                    "No se puede recepcionar una OS sin despachos previos");
        }
        if (this.estado == EstadoOS.CERRADA) {
            throw new IllegalStateException("No se puede recepcionar una OS CERRADA");
        }
        Integer totalRecibido = totalRecibido() + (recepcion.getCantidadRecibida() != null ? recepcion.getCantidadRecibida() : 0);
        if (totalRecibido > totalDespachado()) {
            throw new IllegalStateException(
                    "El total recibido (" + totalRecibido + ") excede lo despachado (" + totalDespachado() + ")");
        }
        this.recepciones.add(recepcion);
        if (this.estado == EstadoOS.EN_PROCESO) {
            transicionar(EstadoOS.RECEPCIONADA);
        }
    }

    public void cerrar() {
        transicionar(EstadoOS.CERRADA);
    }

    public Integer totalDespachado() {
        return despachos.stream()
                .map(DespachoOS::getCantidadDespachada)
                .filter(c -> c != null)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer totalRecibido() {
        return recepciones.stream()
                .map(RecepcionOS::getCantidadRecibida)
                .filter(c -> c != null)
                .mapToInt(Integer::intValue)
                .sum();
    }

    private void transicionar(EstadoOS destino) {
        if (!this.estado.puedeTransicionarA(destino)) {
            throw new IllegalStateException(
                    "Transición inválida de OS: " + this.estado + " -> " + destino);
        }
        this.estado = destino;
    }

    public List<DespachoOS> getDespachos() {
        return Collections.unmodifiableList(despachos);
    }

    public List<RecepcionOS> getRecepciones() {
        return Collections.unmodifiableList(recepciones);
    }
}
