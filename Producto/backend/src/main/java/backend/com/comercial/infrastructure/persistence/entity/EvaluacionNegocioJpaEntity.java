package backend.com.comercial.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.AuditableJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "evaluaciones_negocio")
@Getter
@Setter
public class EvaluacionNegocioJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEVN;

    @Column(unique = true, length = 20, nullable = false)
    private String numero;

    @Column(name = "referencia", length = 300)
    private String referencia;

    @Column(name = "cliente_nombre", length = 200)
    private String clienteNombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private ClienteJpaEntity cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendedor_id")
    private VendedorJpaEntity vendedor;

    @Column(length = 20, nullable = false)
    private String estado;

    @Column(name = "fecha_evaluacion", nullable = false)
    private LocalDate fechaEvaluacion;

    @Column(name = "porcentaje_comision", precision = 5, scale = 2)
    private java.math.BigDecimal porcentajeComision;

    @OneToOne(mappedBy = "evaluacionNegocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private TomaTallajeJpaEntity tomaTallaje;

    @OneToMany(mappedBy = "evaluacionNegocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GastoAdicionalJpaEntity> gastosAdicionales = new ArrayList<>();

    @OneToMany(mappedBy = "evaluacionNegocio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EvaluacionNegocioItemJpaEntity> items = new ArrayList<>();

    public void addItem(EvaluacionNegocioItemJpaEntity item) {
        items.add(item);
        item.setEvaluacionNegocio(this);
    }

    public void addGastoAdicional(GastoAdicionalJpaEntity gasto) {
        gastosAdicionales.add(gasto);
        gasto.setEvaluacionNegocio(this);
    }

    public void setTomaTallajeEntity(TomaTallajeJpaEntity tomaTallaje) {
        this.tomaTallaje = tomaTallaje;
        if (tomaTallaje != null) {
            tomaTallaje.setEvaluacionNegocio(this);
        }
    }
}
