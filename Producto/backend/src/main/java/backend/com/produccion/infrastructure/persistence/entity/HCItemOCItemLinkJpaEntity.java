package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Tabla puente N:M entre HCItem y OCItem.
 * Permite consolidar varias HCs en una OC y/o partir un HCItem en varias OCs.
 */
@Entity
@Table(name = "produccion_hc_item_oc_item")
@Getter
@Setter
public class HCItemOCItemLinkJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_link")
    private Long idLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hc_item_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_link_hci"))
    private HojaCompraItemJpaEntity hcItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_item_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_link_oci"))
    private OrdenCompraItemJpaEntity ocItem;

    @Column(name = "cantidad_asignada", precision = 12, scale = 4, nullable = false)
    private BigDecimal cantidadAsignada;
}
