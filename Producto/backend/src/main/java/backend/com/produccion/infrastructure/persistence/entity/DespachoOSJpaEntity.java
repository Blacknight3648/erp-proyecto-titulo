package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "produccion_despachos_os")
@Getter
@Setter
public class DespachoOSJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despacho")
    private Long idDespacho;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "os_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_desp_os"))
    private OrdenServicioJpaEntity ordenServicio;

    @Column(name = "fecha_despacho", nullable = false)
    private LocalDate fechaDespacho;

    @Column(name = "cantidad_despachada", nullable = false)
    private Integer cantidadDespachada;

    @Column(name = "descripcion", length = 500)
    private String descripcion;

    @Column(name = "responsable", length = 100)
    private String responsable;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
}
