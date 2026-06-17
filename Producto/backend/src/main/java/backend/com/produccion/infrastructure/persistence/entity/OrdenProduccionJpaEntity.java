package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.produccion.domain.enums.EstadoOP;
import backend.com.shared.infrastructure.persistence.entity.AuditableJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden_produccion")
@Getter
@Setter
public class OrdenProduccionJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOP;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "costeo_version_id", nullable = false, foreignKey = @ForeignKey(name = "fk_op_costeo_version"))
    private CosteoVersionJpaEntity costeoVersion;

    @Column(unique = true, length = 20, nullable = false)
    private String numeroOP;

    @Column(name = "nota_venta_id", nullable = false)
    private Long notaVentaId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoOP estado;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_entrega_programada")
    private LocalDate fechaEntregaProgramada;

    @Column(columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "ordenProduccion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenProduccionItemJpaEntity> items = new ArrayList<>();
}
