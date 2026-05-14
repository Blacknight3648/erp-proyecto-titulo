package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "siglas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiglaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sigla_id")
    private Long siglaId;

    @Size(max = 255)
    @Column(name = "descripcion_sigla", length = 255)
    private String descripcionSigla;

    @Size(max = 100)
    @Column(name = "sigla_abreviatura", length = 100)
    private String siglaAbreviatura;
}
