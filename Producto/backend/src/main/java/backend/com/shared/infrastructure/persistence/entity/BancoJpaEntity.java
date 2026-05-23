package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "banco")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BancoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banco_id")
    private Integer bancoId;

    @NotBlank(message = "El nombre del banco no puede estar en blanco")
    @Column(name = "nombre_banco", nullable = false, length = 150)
    private String nombreBanco;

    @NotBlank(message = "El código del banco no puede estar en blanco")
    @Column(name = "codigo_banco", nullable = false, unique = true, length = 20)
    private String codigoBanco;
}
