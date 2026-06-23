package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.comercial.domain.enums.TipoItem;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notas_venta_items")
@Getter
@Setter
public class NotaVentaItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_item_nv")
    private Long idItemNV;

    @Column(name = "nro_item")
    private Integer nroItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nota_venta_id", nullable = false)
    private NotaVentaJpaEntity notaVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", nullable = true)
    private ArticuloJpaEntity articulo;

    @Column(length = 100)
    private String modelo;

    @Column(length = 100)
    private String tela;

    @Column(length = 255)
    private String composicion;

    @Column(length = 50)
    private String color;

    @Column(length = 20)
    private String talla;

    @Column(length = 20)
    private String genero;

    @Column(length = 50)
    private String codigo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id")
    private ProveedorJpaEntity proveedor;

    @Column(name = "lleva_logo", length = 50)
    private String llevaLogo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_item", length = 10)
    private TipoItem tipoItem;

    @Column(name = "requiere_ot")
    private Boolean requiereOt;

    @Column(name = "detalle_ot", columnDefinition = "TEXT")
    private String detalleOt;

    @Column(name = "logo_detalle", columnDefinition = "TEXT")
    private String logoDetalle;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(length = 3)
    private String monedaPrecioUnitario;

    @Column(precision = 12, scale = 2)
    private BigDecimal total;

    @Column(length = 3)
    private String monedaTotal;

    /** ID de la OP creada para este ítem. Referencia suave (sin FK cross-módulo). */
    @Column(name = "op_id")
    private Long opId;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaVentaItemTallaJpaEntity> tallas = new ArrayList<>();

    public void addTalla(NotaVentaItemTallaJpaEntity talla) {
        tallas.add(talla);
        talla.setItem(this);
    }
}
