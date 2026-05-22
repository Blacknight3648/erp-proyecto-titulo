package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "tipo_cuenta_bancaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoCuentaBancariaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_cuenta_id")
    private Integer tipoCuentaId;

    @NotBlank(message = "La denominación de la cuenta no puede estar en blanco")
    @Column(name = "denominacion_cuenta", nullable = false, unique = true, length = 100)
    private String denominacionCuenta;
}
