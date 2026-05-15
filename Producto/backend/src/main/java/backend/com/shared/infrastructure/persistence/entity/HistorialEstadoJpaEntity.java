package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_estado", indexes = {
        @Index(name = "idx_historial_entidad", columnList = "tipo_entidad,entidad_id")
})
@Getter
@Setter
public class HistorialEstadoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_entidad", length = 50, nullable = false)
    private String tipoEntidad;

    @Column(name = "entidad_id", nullable = false)
    private Long entidadId;

    @Column(name = "estado_anterior", length = 50)
    private String estadoAnterior;

    @Column(name = "estado_nuevo", length = 50, nullable = false)
    private String estadoNuevo;

    @Column(length = 100)
    private String usuario;

    @Column(columnDefinition = "TEXT")
    private String observacion;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
