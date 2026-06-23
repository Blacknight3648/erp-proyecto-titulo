package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "gramaje_tela")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GramajeTelaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gramaje")
    private Integer idGramaje;

    @Column(name = "codigo_gramaje", nullable = false, unique = true, length = 10)
    private String codigoGramaje;

    @Column(name = "valor_gramos_m2", precision = 8, scale = 2, nullable = false)
    private BigDecimal valorGramosM2;

    @Column(name = "categoria_vestuario", length = 60)
    private String categoriaVestuario;
}