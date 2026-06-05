package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "subcategoria_tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubCategoriaTelaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subcategoria_tela")
    private Integer idSubCategoriaTela;

    @Column(name = "codigo_subcategoria_tela", nullable = false, unique = true, length = 15)
    private String codigoSubCategoriaTela;

    @Column(name = "nombre_subcategoria_tela", nullable = false)
    private String nombreSubCategoriaTela;

    @ManyToOne
    @JoinColumn(name = "id_categoria_tela")
    private CategoriaTelaJpaEntity categoriaTela;
}
