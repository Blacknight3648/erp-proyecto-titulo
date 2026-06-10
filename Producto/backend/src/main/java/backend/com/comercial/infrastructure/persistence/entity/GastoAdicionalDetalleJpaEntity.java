package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "gasto_adicional_detalles")
@Getter
@Setter
public class GastoAdicionalDetalleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gasto_adicional_id", nullable = false)
    private GastoAdicionalJpaEntity gastoAdicional;

    @Column(length = 100, nullable = false)
    private String clave;

    @Column(length = 500)
    private String valor;
}
