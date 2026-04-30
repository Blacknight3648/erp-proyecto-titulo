package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.produccion.domain.model.EstadoOT;
import backend.com.produccion.domain.model.TipoOT;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produccion_orden_trabajo")
@Getter
@Setter
public class OrdenTrabajoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOT;

    @Column(unique = true, length = 20, nullable = false)
    private String numeroOT;

    @Column(name = "nota_venta_id", nullable = false)
    private Long notaVentaId;

    @Column(name = "nro_item")
    private Integer nroItem;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private TipoOT tipoOT;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private EstadoOT estadoOT;

    @Column(columnDefinition = "TEXT")
    private String observaciones;
}
