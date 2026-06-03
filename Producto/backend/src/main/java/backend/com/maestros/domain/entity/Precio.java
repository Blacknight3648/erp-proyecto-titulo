package backend.com.maestros.domain.entity;

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
public class Precio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_precio")
    private Integer idPrecio;

    @ManyToOne
    @JoinColumn(name = "id_articulo", nullable = false)
    private Articulo articulo;

    @ManyToOne
    @JoinColumn(name = "id_moneda", nullable = false)
    private Moneda moneda;

    @Column(name = "tipo_precio", nullable = false, length = 10)
    private String tipoPrecio;

    @Column(name = "valor", precision = 18, scale = 2, nullable = false)
    private BigDecimal valor;
}
