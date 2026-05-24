package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "produccion_recepciones_os")
@Getter
@Setter
public class RecepcionOSJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion")
    private Long idRecepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "os_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_rec_os"))
    private OrdenServicioJpaEntity ordenServicio;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDate fechaRecepcion;

    @Column(name = "cantidad_recibida", nullable = false)
    private Integer cantidadRecibida;

    @Column(name = "cantidad_conforme")
    private Integer cantidadConforme;

    @Column(name = "cantidad_defectuosa")
    private Integer cantidadDefectuosa;

    @Column(name = "responsable", length = 100)
    private String responsable;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}
