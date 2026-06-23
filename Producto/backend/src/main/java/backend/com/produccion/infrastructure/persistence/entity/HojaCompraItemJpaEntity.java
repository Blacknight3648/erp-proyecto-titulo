package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produccion_hoja_compra_items")
@Getter
@Setter
public class HojaCompraItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hc_item")
    private Long idHCItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hc_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hci_hc"))
    private HojaCompraJpaEntity hojaCompra;

    @Column(name = "tipo_insumo", length = 50, nullable = false)
    private String tipoInsumo;

    @Column(name = "articulo_id")
    private Integer articuloId; // referencia blanda a articulo(id_articulo); registro conserva nombreInsumo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "articulo_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ArticuloJpaEntity articulo;

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", insertable = false, updatable = false,
            foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProveedorJpaEntity proveedor;

    @Column(name = "nombre_insumo", length = 200)
    private String nombreInsumo;

    @Column(name = "consumo_unitario", precision = 12, scale = 4)
    private BigDecimal consumoUnitario;

    @Column(name = "cantidad_op")
    private Integer cantidadOP;

    @Column(name = "cantidad_requerida", precision = 12, scale = 4)
    private BigDecimal cantidadRequerida;

    @Column(name = "precio_unitario_ref", precision = 12, scale = 2)
    private BigDecimal precioUnitarioRef;

    @OneToMany(mappedBy = "hcItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HCItemOCItemLinkJpaEntity> ocLinks = new ArrayList<>();
}
