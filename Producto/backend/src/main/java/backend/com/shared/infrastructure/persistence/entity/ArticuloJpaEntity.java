package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "articulo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_articulo")
    private Integer idArticulo;

    @Column(name = "codigo_articulo", nullable = false, unique = true, length = 20)
    private String codigoArticulo;

    @Column(name = "nombre_articulo", nullable = false, length = 100)
    private String nombreArticulo;

    @Column(name = "descripcion_articulo", length = 200)
    private String descripcionArticulo;

    @Column(name = "codigo_barra", length = 50)
    private String codigoBarra;

    @ManyToOne
    @JoinColumn(name = "id_tipo_articulo", nullable = false)
    private TipoArticuloJpaEntity tipoArticulo;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @Column(name = "stock_actual", precision = 12, scale = 4, nullable = false)
    private java.math.BigDecimal stockActual = java.math.BigDecimal.ZERO;

    @ManyToOne
    @JoinColumn(name = "id_categoria_tela")
    private CategoriaTelaJpaEntity categoriaTela;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria_tela")
    private SubCategoriaTelaJpaEntity subCategoriaTela;

    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticuloTelaJpaEntity detalleTela;

    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticuloPrendaJpaEntity detallePrenda;

    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticuloAccesorioJpaEntity detalleAccesorio;
}
