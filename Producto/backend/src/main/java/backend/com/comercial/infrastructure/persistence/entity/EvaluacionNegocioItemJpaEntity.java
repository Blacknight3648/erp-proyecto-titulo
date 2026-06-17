package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "evaluacion_negocio_items")
@Getter
@Setter
public class EvaluacionNegocioItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEVNI;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_negocio_id", nullable = false)
    private EvaluacionNegocioJpaEntity evaluacionNegocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private ProveedorJpaEntity proveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id")
    private ArticuloJpaEntity articulo;

    // === Campos descriptivos del ítem ===
    @Column(name = "nro_item")
    private Integer nroItem;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "modelo", length = 200)
    private String modelo;

    @Column(name = "tela", length = 200)
    private String tela;

    @Column(name = "composicion", length = 200)
    private String composicion;

    @Column(name = "genero", length = 50)
    private String genero;

    @Column(name = "codigo_interno", length = 100)
    private String codigoInterno;

    @Column(name = "codigo_proveedor", length = 100)
    private String codigoProveedor;

    @Column(name = "proveedor_nombre", length = 200)
    private String proveedorNombre;

    // === Cantidades y precios ===
    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 12, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    @Column(length = 3)
    private String monedaPrecioUnitario;

    @Column(name = "costo_unitario", precision = 12, scale = 2)
    private BigDecimal costoUnitario;

    @Column(length = 3)
    private String monedaCostoUnitario;

    // === Costos individuales ===
    @Column(name = "costo_producto", precision = 12, scale = 2)
    private BigDecimal costoProducto;

    @Column(name = "costo_logo", precision = 12, scale = 2)
    private BigDecimal costoLogo;

    @Column(name = "costo_orden_trabajo", precision = 12, scale = 2)
    private BigDecimal costoOrdenTrabajo;

    // === Clasificación ===
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", length = 10)
    private TipoItem tipoItem;

    @OneToMany(mappedBy = "evaluacionNegocioItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<EvaluacionNegocioItemSpecJpaEntity> specs = new java.util.ArrayList<>();

    public void addSpec(EvaluacionNegocioItemSpecJpaEntity spec) {
        specs.add(spec);
        spec.setEvaluacionNegocioItem(this);
    }

    // === Vínculo con costeo/SCOS (solo ítems tipo OP; sin FK cross-módulo) ===
    @Column(name = "costeo_id")
    private Long costeoId;

    @Column(name = "solicitud_costos_id")
    private Long solicitudCostosId;
}
