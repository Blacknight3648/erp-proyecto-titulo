package backend.com.maestros.domain.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Categoría de artículo (ej: Telas, Accesorios, Prendas Listas).
 */
@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "codigo_categoria", nullable = false, unique = true, length = 10)
    private String codigoCategoria;

    @Column(name = "nombre_categoria", nullable = false, unique = true)
    private String nombreCategoria;
}
