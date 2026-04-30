package backend.com.comercial.domain.model;

import backend.com.shared.events.DomainEvent;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class SolicitudCotizacion {
    private Long idSCOT;
    private DocumentNumber numeroSCOT;
    private String estado;
    private String tipo;
    private Long clienteId;
    private Long vendedorId;
    private Long especificacionTecnicaId;
    private String articuloDescripcion;
    private Boolean esMuestra;
    private Boolean hasLogo;
    private Integer cantidad;
    private LocalDate fecha;

    private List<SCOTPrendaLista> prendas = new ArrayList<>();
    private List<SCOSLogotipo> logotipos = new ArrayList<>();

    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    private SolicitudCotizacion(DocumentNumber numeroSCOT, String tipo, Long clienteId, Long vendedorId,
            Long especificacionTecnicaId, String articuloDescripcion,
            Boolean esMuestra, Boolean hasLogo, Integer cantidad) {
        this.numeroSCOT = numeroSCOT;
        this.tipo = tipo;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.especificacionTecnicaId = especificacionTecnicaId;
        this.articuloDescripcion = articuloDescripcion;
        this.esMuestra = esMuestra != null ? esMuestra : false;
        this.hasLogo = hasLogo != null ? hasLogo : false;
        this.cantidad = cantidad != null ? cantidad : 0;
        this.estado = "PENDIENTE";
        this.fecha = LocalDate.now();
    }

    public SolicitudCotizacion(Long idSCOT, DocumentNumber numeroSCOT, String estado, String tipo, Long clienteId,
            Long vendedorId, Long especificacionTecnicaId, String articuloDescripcion,
            Boolean esMuestra, Boolean hasLogo, Integer cantidad, LocalDate fecha,
            List<SCOTPrendaLista> prendas, List<SCOSLogotipo> logotipos) {
        this.idSCOT = idSCOT;
        this.numeroSCOT = numeroSCOT;
        this.estado = estado;
        this.tipo = tipo;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.especificacionTecnicaId = especificacionTecnicaId;
        this.articuloDescripcion = articuloDescripcion;
        this.esMuestra = esMuestra;
        this.hasLogo = hasLogo;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.prendas = prendas != null ? new ArrayList<>(prendas) : new ArrayList<>();
        this.logotipos = logotipos != null ? new ArrayList<>(logotipos) : new ArrayList<>();
    }

    public static SolicitudCotizacion crear(DocumentNumber numero, String tipo, Long clienteId, Long vendedorId,
            Long especificacionTecnicaId, String articuloDescripcion,
            Boolean esMuestra, Boolean hasLogo, Integer cantidad) {
        return new SolicitudCotizacion(numero, tipo, clienteId, vendedorId, especificacionTecnicaId,
                articuloDescripcion, esMuestra, hasLogo, cantidad);
    }

    public void addPrenda(SCOTPrendaLista prenda) {
        this.prendas.add(prenda);
    }

    public void addLogotipo(SCOSLogotipo logotipo) {
        if (logotipo != null) {
            this.logotipos.add(logotipo);
        }
    }


    public void aprobar() {
        this.estado = "APROBADA";
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
