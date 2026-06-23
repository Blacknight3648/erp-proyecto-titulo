package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "categoria_tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriaTelaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria_tela")
    private Integer idCategoriaTela;

    @Column(name = "codigo_categoria_tela", nullable = false, unique = true, length = 10)
    private String codigoCategoriaTela;

    @Column(name = "nombre_categoria_tela", nullable = false, unique = true)
    private String nombreCategoriaTela;
}
