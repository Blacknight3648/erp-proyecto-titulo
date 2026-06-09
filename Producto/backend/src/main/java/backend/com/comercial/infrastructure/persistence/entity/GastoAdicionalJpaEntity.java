package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "gastos_adicionales")
@Getter
@Setter
public class GastoAdicionalJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluacion_negocio_id", nullable = false)
    private EvaluacionNegocioJpaEntity evaluacionNegocio;

    @Column(name = "tipo_gasto", length = 30, nullable = false)
    private String tipoGasto;

    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal monto;

    @Column(length = 3)
    private String moneda;

    @OneToMany(mappedBy = "gastoAdicional", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<GastoAdicionalDetalleJpaEntity> detalles = new java.util.ArrayList<>();

    public void addDetalle(GastoAdicionalDetalleJpaEntity detalle) {
        detalles.add(detalle);
        detalle.setGastoAdicional(this);
    }
}
