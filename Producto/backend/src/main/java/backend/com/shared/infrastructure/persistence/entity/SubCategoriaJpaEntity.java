package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Subcategoría de artículo, asociada a una Categoría padre.
 */
@Entity
@Table(name = "subcategoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoriaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subcategoria")
    private Integer idSubCategoria;

    @Column(name = "codigo_subcategoria", nullable = false, unique = true, length = 15)
    private String codigoSubcategoria;

    @Column(name = "nombre_subcategoria", nullable = false)
    private String nombreSubCategoria;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaJpaEntity categoria;
}
