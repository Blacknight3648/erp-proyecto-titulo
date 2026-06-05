package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "familia_tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamiliaTelaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_familia_tela")
    private Integer idFamiliaTela;

    @Column(name = "codigo_familia", nullable = false, unique = true, length = 10)
    private String codigoFamilia;

    @Column(name = "nombre_familia", nullable = false, length = 60)
    private String nombreFamilia;
}