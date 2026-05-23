package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "direccion")
@Data
@NoArgsConstructor
public class DireccionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "direccion_id")
    private Long direccionId;

    @Column(name = "calle", nullable = false, length = 150)
    private String calle;

    @Column(name = "numero", nullable = false, length = 20)
    private String numero;

    @Column(name = "depto", length = 20)
    private String depto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_direccion_id", nullable = false)
    private TipoDireccionJpaEntity tipoDireccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comuna_id", nullable = false)
    private ComunaJpaEntity comuna;

}
