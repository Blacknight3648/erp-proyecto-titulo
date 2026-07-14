package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.produccion.domain.enums.EstadoOC;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produccion_ordenes_compra", uniqueConstraints = {
        @UniqueConstraint(name = "uk_oc_numero", columnNames = "numero_oc")
})
@Getter
@Setter
public class OrdenCompraJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_oc")
    private Long idOC;

    @Column(name = "numero_oc", length = 30, nullable = false)
    private String numeroOC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_oc_proveedor"))
    private ProveedorJpaEntity proveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 25, nullable = false)
    private EstadoOC estado;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @Column(name = "total_neto", precision = 14, scale = 2)
    private BigDecimal totalNeto;

    @Column(name = "motivo_rechazo", length = 500)
    private String motivoRechazo;

    @Column(name = "version", nullable = false)
    private Integer version = 1;

    @OneToMany(mappedBy = "ordenCompra", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenCompraItemJpaEntity> items = new ArrayList<>();
}
