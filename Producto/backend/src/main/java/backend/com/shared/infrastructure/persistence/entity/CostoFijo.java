package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "costo_fijo")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CostoFijo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long costoFijoId;

    @Column(nullable = false, length = 100)
    private String nombreCostoFijo;

    @Column(nullable = false)
    private Double monto;

    @Column(length = 50)
    private String periodo;
}
