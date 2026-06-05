package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * Precio de un artículo en una moneda específica.
 * Relación @ManyToOne a Articulo (un artículo puede tener múltiples precios).
 */
@Entity
@Table(name = "precio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrecioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio")
    private Integer idPrecio;

    @ManyToOne
    @JoinColumn(name = "id_articulo", nullable = false)
    private ArticuloJpaEntity articulo;

    @ManyToOne
    @JoinColumn(name = "id_moneda", nullable = false)
    private MonedaJpaEntity moneda;

    @Column(name = "tipo_precio", nullable = false, length = 10)
    private String tipoPrecio;

    @Column(name = "valor", precision = 18, scale = 2, nullable = false)
    private BigDecimal valor;
}
