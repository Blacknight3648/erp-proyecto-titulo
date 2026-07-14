package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Getter
@Setter
public class NotificacionJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String tipo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mensaje;

    @Column(length = 30)
    private String categoria;

    @Column(length = 20)
    private String prioridad;

    @Column(nullable = false)
    private boolean leida;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
