package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produccion_orden_compra_items")
@Getter
@Setter
public class OrdenCompraItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oc_item")
    private Long idOCItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_oci_oc"))
    private OrdenCompraJpaEntity ordenCompra;

    @Column(name = "tipo_insumo", length = 50, nullable = false)
    private String tipoInsumo;

    @Column(name = "insumo_id", nullable = false)
    private Long insumoId;

    @Column(name = "nombre_insumo", length = 200)
    private String nombreInsumo;

    @Column(name = "cantidad_requerida", precision = 12, scale = 4)
    private BigDecimal cantidadRequerida;

    @Column(name = "cantidad_stock", precision = 12, scale = 4)
    private BigDecimal cantidadStock;

    @Column(name = "cantidad_comprada", precision = 12, scale = 4)
    private BigDecimal cantidadComprada;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "subtotal", precision = 14, scale = 2)
    private BigDecimal subtotal;

    @OneToMany(mappedBy = "ocItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HCItemOCItemLinkJpaEntity> hcLinks = new ArrayList<>();
}
