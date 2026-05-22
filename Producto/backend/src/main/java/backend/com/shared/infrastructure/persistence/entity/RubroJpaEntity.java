package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "rubros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RubroJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rubroId;

    @Size(max = 255)
    @Column(name = "nombre_rubro", length = 255)
    private String nombreRubro;

    @Size(max = 255)
    @Column(name = "descripcion_rubro", length = 255)
    private String descripcionRubro;
}
