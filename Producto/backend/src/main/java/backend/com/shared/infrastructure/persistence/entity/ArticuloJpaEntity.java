package backend.com.shared.infrastructure.persistence.entity;

import backend.com.shared.domain.enums.TipoArticulo;
import jakarta.persistence.*;
import lombok.*;
import backend.com.shared.infrastructure.persistence.entity.ArticuloPrendaJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloTelaJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloAccesorioJpaEntity;

/**
 * Tabla maestra de artículos – equivalente a product.template en Odoo.
 * No usa herencia JPA. El campo {@code tipoArticulo} determina qué tabla
 * satélite (articulo_tela, articulo_prenda, articulo_accesorio) complementa al
 * registro.
 */
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_articulo", nullable = false, length = 15)
    private TipoArticulo tipoArticulo;

    @Column(name = "procedencia", length = 1)
    private String procedencia;

    @Column(name = "lotes", nullable = false)
    private Boolean lotes = false;

    @Column(name = "seriales", nullable = false)
    private Boolean seriales = false;

    @Column(name = "compras", nullable = false)
    private Boolean compras = true;

    @Column(name = "comision")
    private Integer comision;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // ── Relaciones catálogo ──────────────────────────────────

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaJpaEntity categoria;

    @ManyToOne
    @JoinColumn(name = "id_subcategoria")
    private SubCategoriaJpaEntity subCategoria;

    @ManyToOne
    @JoinColumn(name = "id_unidad_medida", nullable = false)
    private UnidadMedidaJpaEntity unidadMedida;

    // ── Satélites opcionales (composición, no herencia) ──────

    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticuloTelaJpaEntity detalleTela;

    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticuloPrendaJpaEntity detallePrenda;

    @OneToOne(mappedBy = "articulo", cascade = CascadeType.ALL, orphanRemoval = true)
    private ArticuloAccesorioJpaEntity detalleAccesorio;
}
