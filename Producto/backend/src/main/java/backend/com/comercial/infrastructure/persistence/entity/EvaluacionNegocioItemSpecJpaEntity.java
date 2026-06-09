package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "evaluacion_negocio_item_specs")
@Getter
@Setter
public class EvaluacionNegocioItemSpecJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSpec;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_negocio_item_id", nullable = false)
    private EvaluacionNegocioItemJpaEntity evaluacionNegocioItem;

    @Column(length = 100, nullable = false)
    private String clave;

    @Column(length = 500)
    private String valor;
}
