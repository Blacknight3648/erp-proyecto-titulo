package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "produccion_recepcion_oc_items")
@Getter
@Setter
public class RecepcionOCItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion_item")
    private Long idRecepcionItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recepcion_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reci_recoc"))
    private RecepcionOCJpaEntity recepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_item_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_reci_oci"))
    private OrdenCompraItemJpaEntity ocItem;

    @Column(name = "cantidad_recibida", precision = 12, scale = 4, nullable = false)
    private BigDecimal cantidadRecibida;

    @Column(name = "cantidad_conforme", precision = 12, scale = 4)
    private BigDecimal cantidadConforme;

    @Column(name = "cantidad_rechazada", precision = 12, scale = 4)
    private BigDecimal cantidadRechazada;

    @Column(name = "motivo_rechazo", columnDefinition = "TEXT")
    private String motivoRechazo;
}
