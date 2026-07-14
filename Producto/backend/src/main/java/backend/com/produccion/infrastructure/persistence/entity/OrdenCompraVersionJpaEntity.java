package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "produccion_oc_versiones", uniqueConstraints = @UniqueConstraint(columnNames = {
        "oc_id", "numero_version" }))
@Getter
@Setter
public class OrdenCompraVersionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOCVersion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_id", nullable = false)
    private OrdenCompraJpaEntity ordenCompra;

    @Column(name = "numero_version", nullable = false)
    private Integer numeroVersion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "motivo_cambio", nullable = true)
    private String motivoCambio;

    @Column(name = "usuario_creador", nullable = false)
    private String usuarioCreador;

    @OneToMany(mappedBy = "ocVersion", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<OrdenCompraItemVersionJpaEntity> items = new java.util.ArrayList<>();

    @Column(name = "proveedor_id")
    private Long proveedorId;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @Column(name = "total_neto", precision = 14, scale = 2)
    private BigDecimal totalNeto;

}
