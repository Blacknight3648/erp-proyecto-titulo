package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "dato_bancario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DatoBancarioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dato_bancario_id")
    private Integer datoBancarioId;

    @NotBlank(message = "El número de cuenta no puede estar en blanco")
    @Column(name = "numero_cuenta", nullable = false, length = 50)
    private String numeroCuenta;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "banco_id", nullable = false)
    private BancoJpaEntity banco;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinColumn(name = "tipo_cuenta_id", nullable = false)
    private TipoCuentaBancariaJpaEntity tipoCuentaBancaria;
}
