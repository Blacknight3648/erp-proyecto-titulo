package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produccion_oc_item_versiones")
@Getter
@Setter
public class OrdenCompraItemVersionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOCItemVersion;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @JoinColumn(name = "oc_version_id", nullable = false)
    private OrdenCompraVersionJpaEntity ocVersion;

    @Column(name = "oc_item_id", nullable = false)
    private Long ocItemId;

    @Column(name = "tipo_insumo", length = 30, nullable = false)
    private String tipoInsumo;

    @Column(name = "articulo_id")
    private Integer articuloId; // referencia blanda a articulo(id_articulo); snapshot conserva nombreInsumo

    @Column(name = "nombre_insumo")
    private String nombreInsumo;

    @Column(precision = 12, scale = 2)
    private BigDecimal cantidadComprada;

    @Column(precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(precision = 14, scale = 2)
    private BigDecimal subtotal;

}
