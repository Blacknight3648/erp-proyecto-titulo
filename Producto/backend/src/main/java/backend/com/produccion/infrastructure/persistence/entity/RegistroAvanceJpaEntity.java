package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "produccion_registro_avance", indexes = {
        @Index(name = "idx_avance_ot", columnList = "orden_trabajo_id")
})
@Getter
@Setter
public class RegistroAvanceJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "orden_trabajo_id", nullable = false)
    private Long ordenTrabajoId;

    @Column(name = "cantidad_producida", nullable = false)
    private Integer cantidadProducida;

    @Column(name = "cantidad_merma", nullable = false)
    private Integer cantidadMerma;

    @Column(name = "motivo_merma", length = 255)
    private String motivoMerma;

    @Column(length = 100)
    private String usuario;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
