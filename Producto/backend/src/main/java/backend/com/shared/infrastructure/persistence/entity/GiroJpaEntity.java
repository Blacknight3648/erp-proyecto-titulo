package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "giros")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GiroJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "giro_id")
    private Long giroId;

    @Size(max = 255)
    @Column(name = "codigo_sii", length = 255)
    private String codigoSii;

    @Size(max = 255)
    @Column(name = "nombre_giro", length = 255)
    private String nombreGiro;

    @Size(max = 255)
    @Column(name = "descripcion_giro", length = 255)
    private String descripcionGiro;

    @ManyToOne
    @JoinColumn(name = "rubro_id")
    private RubroJpaEntity rubro;

}
