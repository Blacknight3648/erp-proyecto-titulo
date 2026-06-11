package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.EspecificacionTecnica;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "solicitudes_cotizacion")
@Getter
@Setter
public class SolicitudCotizacionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSCOT;

    @Column(unique = true, length = 20, nullable = false)
    private String numero;

    @Column(length = 20)
    private String estado;

    @Column(length = 20)
    private String tipo; // COSTEO o COTIZACION

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

    @Column(name = "es_muestra")
    private Boolean esMuestra;

    @Column(name = "has_logo")
    private Boolean hasLogo;

    private Integer cantidad;

    private LocalDate fecha;

    @Column(name = "costo_total_calculado", precision = 14, scale = 2)
    private BigDecimal costoTotalCalculado;

    @Column(length = 3)
    private String monedaCostoTotal;

    @Column(name = "venta_asociada_id")
    private Long ventaAsociadaId;

    @OneToMany(mappedBy = "solicitudCotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOTPrendaListaJpaEntity> prendas = new ArrayList<>();

    @OneToMany(mappedBy = "solicitudCotizacion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SCOSLogotipoJpaEntity> logotipos = new ArrayList<>();

    public void addPrenda(SCOTPrendaListaJpaEntity prenda) {
        prendas.add(prenda);
        prenda.setSolicitudCotizacion(this);
    }

    public void addLogotipo(SCOSLogotipoJpaEntity logotipo) {
        logotipos.add(logotipo);
        logotipo.setSolicitudCotizacion(this);
    }
}
