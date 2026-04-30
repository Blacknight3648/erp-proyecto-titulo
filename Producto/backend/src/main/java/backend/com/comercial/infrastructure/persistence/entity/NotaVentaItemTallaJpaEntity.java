package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "notas_venta_item_tallas")
@Getter
@Setter
public class NotaVentaItemTallaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idItemTalla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private NotaVentaItemJpaEntity item;

    @Column(nullable = false, length = 20)
    private String talla;

    @Column(nullable = false)
    private Integer cantidad;
}
