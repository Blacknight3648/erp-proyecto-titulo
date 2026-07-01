package backend.com.produccion.infrastructure.persistence.entity;

import backend.com.produccion.domain.enums.CalidadTaller;
import backend.com.produccion.domain.enums.EstadoIdaLogo;
import backend.com.produccion.domain.enums.EstadoRecLogo;

import backend.com.produccion.domain.enums.EstadoOcMp;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "produccion_seguimiento_op")
@Getter
@Setter
public class SeguimientoOPJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeguimiento;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_produccion_id", nullable = false)
    private OrdenProduccionJpaEntity ordenProduccion;

    private LocalDate fechaRecepcionOp;
    private LocalDate finTizado;

    @Enumerated(EnumType.STRING)
    private EstadoOcMp estadoOcMp;

    private LocalDate recepcionCompras;
    private LocalDate inicioCorte;
    private LocalDate finCorte;
    private LocalDate inicioLogo;

    @Enumerated(EnumType.STRING)
    private EstadoIdaLogo estadoIdaLogo;

    private LocalDate regresoLogo;

    @Enumerated(EnumType.STRING)
    private EstadoRecLogo estadoRecLogo;

    private LocalDate inicioTallerExterno;
    private LocalDate finTallerExterno;

    @Enumerated(EnumType.STRING)
    private CalidadTaller calidadTaller;

    @Column(columnDefinition = "TEXT")
    private String obsTaller;

    private LocalDate finTerminacion;
    private LocalDate finPersonalizado;
}
