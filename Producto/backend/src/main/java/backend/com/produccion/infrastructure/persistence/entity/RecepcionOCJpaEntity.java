package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "produccion_recepciones_oc")
@Getter
@Setter
public class RecepcionOCJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_recepcion")
    private Long idRecepcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oc_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_recoc_oc"))
    private OrdenCompraJpaEntity ordenCompra;

    @Column(name = "fecha_recepcion", nullable = false)
    private LocalDate fechaRecepcion;

    @Column(name = "numero_guia", length = 50)
    private String numeroGuia;

    @Column(name = "responsable", length = 100)
    private String responsable;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @OneToMany(mappedBy = "recepcion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecepcionOCItemJpaEntity> items = new ArrayList<>();
}
