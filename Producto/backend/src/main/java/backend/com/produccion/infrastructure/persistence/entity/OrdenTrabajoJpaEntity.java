package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.produccion.domain.enums.EstadoOT;
import backend.com.produccion.domain.enums.TipoOT;
import backend.com.produccion.domain.model.FaseProduccion;
import backend.com.shared.infrastructure.persistence.entity.AuditableJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produccion_orden_trabajo")
@Getter
@Setter
public class OrdenTrabajoJpaEntity extends AuditableJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOT;

    @Column(length = 30, nullable = false)
    private String numeroOT;

    @Column(name = "nota_venta_id", nullable = false)
    private Long notaVentaId;

    // FK directa al ítem de la NV (notas_venta_items.id_item_nv).
    // Columna simple para no acoplar entidades entre bounded contexts;
    // la constraint FK se declara a nivel de BD (ver V12__ot_item_nv_fk.sql).
    // Nullable: las OT históricas se rellenan por backfill en la migración.
    @Column(name = "item_nv_id")
    private Long itemNVId;

    @Column(name = "orden_produccion_id")
    private Long ordenProduccionId;

    @Column(name = "nro_item")
    private Integer nroItem;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TipoOT tipoOT;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private FaseProduccion fase;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoOT estadoOT;

    @Column(name = "cantidad_total")
    private Integer cantidadTotal;

    @Column(name = "cantidad_producida")
    private Integer cantidadProducida;

    @Column(name = "cantidad_merma")
    private Integer cantidadMerma;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
