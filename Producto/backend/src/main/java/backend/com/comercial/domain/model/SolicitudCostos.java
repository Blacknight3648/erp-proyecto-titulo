package backend.com.comercial.domain.model;

import backend.com.shared.events.DomainEvent;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.math.BigDecimal;

@Getter
public class SolicitudCostos {
    private Long idSCOS;
    private DocumentNumber numeroSCOS;
    private String estado;
    private String tipo;
    private Long clienteId;
    private String clienteNombre;
    private Long vendedorId;
    private String vendedorNombre;
    private Long especificacionTecnicaId;
    private String articuloDescripcion;
    private String nombrePrenda;
    private Boolean esMuestra;
    private Boolean hasLogo;
    private Integer cantidad;
    private String genero;
    private String tallaje;
    private LocalDate fecha;
    private List<SCOSTela> telas = new ArrayList<>();
    private List<SCOSAccesorio> accesorios = new ArrayList<>();
    private List<SCOSPlantilla> plantillas = new ArrayList<>();
    private List<SCOTPrendaLista> productos = new ArrayList<>();
    private List<SCOSLogotipo> logotipos = new ArrayList<>();
    private BigDecimal costoTotal;

    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    // Constructor for creating new records
    private SolicitudCostos(DocumentNumber numeroSCOS, String tipo, Long clienteId, Long vendedorId,
            Long especificacionTecnicaId, String articuloDescripcion, String nombrePrenda,
            Boolean esMuestra, Boolean hasLogo, Integer cantidad, String genero, String tallaje) {
        this.numeroSCOS = numeroSCOS;
        this.tipo = tipo;
        this.clienteId = clienteId;
        this.vendedorId = vendedorId;
        this.especificacionTecnicaId = especificacionTecnicaId;
        this.articuloDescripcion = articuloDescripcion;
        this.nombrePrenda = nombrePrenda;
        this.esMuestra = esMuestra != null ? esMuestra : false;
        this.hasLogo = hasLogo != null ? hasLogo : false;
        this.cantidad = cantidad != null ? cantidad : 0;
        this.genero = genero;
        this.tallaje = tallaje;
        this.estado = "PENDIENTE";
        this.fecha = LocalDate.now();
        this.costoTotal = BigDecimal.ZERO;
    }

    public SolicitudCostos(Long idSCOS, DocumentNumber numeroSCOS, String estado, String tipo, Long clienteId,
            String clienteNombre, Long vendedorId, String vendedorNombre, Long especificacionTecnicaId, 
            String articuloDescripcion, String nombrePrenda,
            Boolean esMuestra, Boolean hasLogo, Integer cantidad, String genero, String tallaje, LocalDate fecha,
            List<SCOSTela> telas, List<SCOSAccesorio> accesorios, List<SCOSPlantilla> plantillas,
            List<SCOTPrendaLista> productos, List<SCOSLogotipo> logotipos,
            BigDecimal costoTotal) {
        this.idSCOS = idSCOS;
        this.numeroSCOS = numeroSCOS;
        this.estado = estado;
        this.tipo = tipo;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.vendedorId = vendedorId;
        this.vendedorNombre = vendedorNombre;
        this.especificacionTecnicaId = especificacionTecnicaId;
        this.articuloDescripcion = articuloDescripcion;
        this.nombrePrenda = nombrePrenda;
        this.esMuestra = esMuestra;
        this.hasLogo = hasLogo;
        this.cantidad = cantidad;
        this.genero = genero;
        this.tallaje = tallaje;
        this.fecha = fecha;
        this.telas = telas != null ? new ArrayList<>(telas) : new ArrayList<>();
        this.accesorios = accesorios != null ? new ArrayList<>(accesorios) : new ArrayList<>();
        this.plantillas = plantillas != null ? new ArrayList<>(plantillas) : new ArrayList<>();
        this.productos = productos != null ? new ArrayList<>(productos) : new ArrayList<>();
        this.logotipos = logotipos != null ? new ArrayList<>(logotipos) : new ArrayList<>();
        this.costoTotal = costoTotal;
    }

    public static SolicitudCostos crear(DocumentNumber numero, String tipo, Long clienteId, Long vendedorId,
            Long especificacionTecnicaId, String articuloDescripcion, String nombrePrenda,
            Boolean esMuestra, Boolean hasLogo, Integer cantidad, String genero, String tallaje) {
        return new SolicitudCostos(numero, tipo, clienteId, vendedorId, especificacionTecnicaId,
                articuloDescripcion, nombrePrenda, esMuestra, hasLogo, cantidad, genero, tallaje);
    }

    public void addTela(SCOSTela tela) {
        this.telas.add(tela);
    }

    public void addAccesorio(SCOSAccesorio accesorio) {
        if (accesorio != null) {
            this.accesorios.add(accesorio);
        }
    }

    public void addPlantilla(SCOSPlantilla plantilla) {
        this.plantillas.add(plantilla);
    }

    public void addProducto(SCOTPrendaLista producto) {
        this.productos.add(producto);
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
