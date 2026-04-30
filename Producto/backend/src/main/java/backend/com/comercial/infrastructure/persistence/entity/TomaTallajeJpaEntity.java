package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "toma_tallaje")
@Getter
@Setter
public class TomaTallajeJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTomaTallaje;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_negocio_id", nullable = false, unique = true)
    private EvaluacionNegocioJpaEntity evaluacionNegocio;

    @Column(name = "costo_total", precision = 12, scale = 2)
    private BigDecimal costoTotal;

    @Column(length = 3)
    private String moneda;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    @Column(name = "metadata_json", columnDefinition = "TEXT")
    private String metadataJson;
}
