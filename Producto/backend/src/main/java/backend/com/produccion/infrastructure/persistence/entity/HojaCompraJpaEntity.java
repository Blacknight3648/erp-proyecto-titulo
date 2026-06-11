package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import backend.com.produccion.domain.enums.EstadoHC;

@Entity
@Table(name = "produccion_hojas_compra", uniqueConstraints = {
        @UniqueConstraint(name = "uk_hc_numero", columnNames = "numero_hc"),
        @UniqueConstraint(name = "uk_hc_op", columnNames = "op_id")
})
@Getter
@Setter
public class HojaCompraJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_hc")
    private Long idHC;

    @Column(name = "numero_hc", length = 20, nullable = false)
    private String numeroHC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hc_op"))
    private OrdenProduccionJpaEntity ordenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costeo_version_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hc_costeo_version"))
    private CosteoVersionJpaEntity costeoVersion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoHC estado;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDate fechaGeneracion;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "hojaCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HojaCompraItemJpaEntity> items = new ArrayList<>();
}
