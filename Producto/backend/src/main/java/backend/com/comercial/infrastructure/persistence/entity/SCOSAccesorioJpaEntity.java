package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "scos_accesorios")
@Getter
@Setter
public class SCOSAccesorioJpaEntity {
    @Transient
    private String tempId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSCOSAccesorio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_costos_id", nullable = true)
    private SolicitudCostosJpaEntity solicitudCostos;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id")
    private ArticuloJpaEntity articulo;

    private String tipo;
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private ProveedorJpaEntity proveedor;

    @Column(name = "proveedor_referencia")
    private String proveedorReferencia;

    private Integer cantidad;

    @Column(precision = 10, scale = 4)
    private BigDecimal consumo;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(length = 3)
    private String monedaPrecioUnitario;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(length = 3)
    private String monedaCostoTotal;
}
