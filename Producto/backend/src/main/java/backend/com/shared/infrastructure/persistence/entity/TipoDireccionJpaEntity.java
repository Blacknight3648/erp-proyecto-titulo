package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_direccion")
@Data
@NoArgsConstructor
public class TipoDireccionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_direccion_id")
    private Integer tipoDireccionId;

    @NotBlank(message = "La descripción no puede estar vacía")
    @Column(name = "descripcion", length = 200, nullable = false)
    private String descripcion;

}
