package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.produccion.domain.enums.EstadoOS;
import backend.com.produccion.domain.enums.TipoServicioOS;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produccion_ordenes_servicio", uniqueConstraints = {
        @UniqueConstraint(name = "uk_os_numero", columnNames = "numero_os")
})
@Getter
@Setter
public class OrdenServicioJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_os")
    private Long idOS;

    @Column(name = "numero_os", length = 30, nullable = false)
    private String numeroOS;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "op_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_os_op"))
    private OrdenProduccionJpaEntity ordenProduccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_os_proveedor"))
    private ProveedorJpaEntity proveedor;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_servicio", length = 20, nullable = false)
    private TipoServicioOS tipoServicio;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 20, nullable = false)
    private EstadoOS estado;

    @Column(name = "fecha_emision", nullable = false)
    private LocalDate fechaEmision;

    @Column(name = "fecha_entrega_estimada")
    private LocalDate fechaEntregaEstimada;

    @Column(name = "descripcion_trabajo", columnDefinition = "TEXT")
    private String descripcionTrabajo;

    @Column(name = "cantidad_pactada", nullable = false)
    private Integer cantidadPactada;

    @Column(name = "precio_unitario", precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "total_neto", precision = 14, scale = 2)
    private BigDecimal totalNeto;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DespachoOSJpaEntity> despachos = new ArrayList<>();

    @OneToMany(mappedBy = "ordenServicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecepcionOSJpaEntity> recepciones = new ArrayList<>();
}
