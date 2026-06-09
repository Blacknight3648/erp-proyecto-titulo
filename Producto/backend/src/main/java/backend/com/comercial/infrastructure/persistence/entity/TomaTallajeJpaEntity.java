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

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "fecha_programada")
    private LocalDate fechaProgramada;

    // Inputs para el cálculo del costo de toma de tallaje
    @Column(name = "dias_x_recinto")
    private Integer diasXRecinto;

    @Column(name = "pers_x_recinto")
    private Integer persXRecinto;

    @Column(name = "colaccion_x_pers", precision = 12, scale = 2)
    private BigDecimal colaccionXPers;

    @Column(name = "asignacion_x_pers", precision = 12, scale = 2)
    private BigDecimal asignacionXPers;

    @Column(name = "peajes", precision = 12, scale = 2)
    private BigDecimal peajes;

    @Column(name = "bencina_x_lt", precision = 10, scale = 2)
    private BigDecimal bencinaXLt;

    @Column(name = "km_total", precision = 10, scale = 2)
    private BigDecimal kmTotal;

    @Column(name = "rend_km_lt", precision = 8, scale = 2)
    private BigDecimal rendKmLt;

    @Column(name = "recintos")
    private Integer recintos;

    @OneToMany(mappedBy = "tomaTallaje", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<TomaTallajeDetalleJpaEntity> detalles = new java.util.ArrayList<>();

    public void addDetalle(TomaTallajeDetalleJpaEntity detalle) {
        detalles.add(detalle);
        detalle.setTomaTallaje(this);
    }
}
