package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.EspecificacionTecnica;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitudes_costos")
@Getter
@Setter
public class SolicitudCostosJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSCOS;

    @Column(unique = true, length = 20, nullable = false)
    private String numero;

    @Column(length = 20)
    private String estado;

    @Column(length = 20)
    private String tipo; // COSTEO

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJpaEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private VendedorJpaEntity vendedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "especificacion_tecnica_id")
    private EspecificacionTecnica especificacionTecnica;

    @Column(name = "articulo_descripcion", nullable = false)
    private String articuloDescripcion;

    @Column(name = "nombre_prenda")
    private String nombrePrenda;

    private String genero;
    private String tallaje;

    @Column(name = "es_muestra")
    private Boolean esMuestra;

    @Column(name = "has_logo")
    private Boolean hasLogo;

    private Integer cantidad;

    private LocalDate fecha;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private java.math.BigDecimal costoTotal;

    @OneToMany(mappedBy = "solicitudCostos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOSTelaJpaEntity> telas = new ArrayList<>();

    @OneToMany(mappedBy = "solicitudCostos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOSAccesorioJpaEntity> accesorios = new ArrayList<>();

    @OneToMany(mappedBy = "solicitudCostos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOSPlantillaJpaEntity> plantillas = new ArrayList<>();

    @OneToMany(mappedBy = "solicitudCostos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOTPrendaListaJpaEntity> prendas = new ArrayList<>();

    @OneToMany(mappedBy = "solicitudCostos", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOSLogotipoJpaEntity> logotipos = new ArrayList<>();

    public void addTela(SCOSTelaJpaEntity tela) {
        telas.add(tela);
        tela.setSolicitudCostos(this);
    }

    public void addAccesorio(SCOSAccesorioJpaEntity accesorio) {
        accesorios.add(accesorio);
        accesorio.setSolicitudCostos(this);
    }

    public void addPlantilla(SCOSPlantillaJpaEntity plantilla) {
        plantillas.add(plantilla);
        plantilla.setSolicitudCostos(this);
    }

    public void addPrenda(SCOTPrendaListaJpaEntity prenda) {
        prendas.add(prenda);
        prenda.setSolicitudCostos(this);
    }

    public void addLogotipo(SCOSLogotipoJpaEntity logotipo) {
        logotipos.add(logotipo);
        logotipo.setSolicitudCostos(this);
    }

    public void clearCollections() {
        telas.clear();
        accesorios.clear();
        plantillas.clear();
        prendas.clear();
        logotipos.clear();
    }
}
