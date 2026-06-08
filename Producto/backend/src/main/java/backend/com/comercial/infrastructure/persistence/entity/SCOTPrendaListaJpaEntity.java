package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.ProductoJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "scot_prendas")
@Getter
@Setter
public class SCOTPrendaListaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSCOTPrendaLista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id")
    private SolicitudCotizacionJpaEntity solicitudCotizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id")
    private ProductoJpaEntity producto;

    @Column(length = 200, nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(length = 20)
    private String talla;

    @Column(length = 50)
    private String color;

    @Column(name = "proveedor_referencia", length = 200)
    private String proveedorReferencia;

    @Column(name = "link_referencia", length = 500)
    private String linkReferencia;

    @Column(length = 200)
    private String composicion;

    @Column(length = 50)
    private String peso;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "precio_unitario", precision = 14, scale = 2)
    private java.math.BigDecimal precioUnitario;

    @Column(name = "moneda_precio_unitario", length = 3)
    private String monedaPrecioUnitario;
}
