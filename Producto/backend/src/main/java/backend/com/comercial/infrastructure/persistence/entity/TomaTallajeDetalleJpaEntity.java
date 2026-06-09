package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "toma_tallaje_detalles")
@Getter
@Setter
public class TomaTallajeDetalleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toma_tallaje_id", nullable = false)
    private TomaTallajeJpaEntity tomaTallaje;

    @Column(name = "identificador_persona", length = 200, nullable = false)
    private String identificadorPersona;

    @Column(length = 50, nullable = false)
    private String talla;

    @Column(nullable = false)
    private Integer cantidad;
}
