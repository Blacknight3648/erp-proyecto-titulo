package backend.com.produccion.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "produccion_orden_items")
@Getter
@Setter
public class OrdenProduccionItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_op_item")
    private Long idOPItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_produccion_id", nullable = false)
    private OrdenProduccionJpaEntity ordenProduccion;

    @Column(name = "articulo_id", nullable = true)
    private Integer articuloId;

    @Column(name = "nro_item")
    private Integer nroItem;

    @Column(length = 100)
    private String modelo;

    @Column(length = 100)
    private String tela;

    @Column(length = 255)
    private String composicion;

    @Column(length = 50)
    private String color;

    @Column(length = 20)
    private String talla;

    @Column(length = 20)
    private String genero;

    @Column(length = 50)
    private String codigo;

    @Column(name = "lleva_logo", length = 50)
    private String llevaLogo;

    @Column(nullable = false)
    private Integer cantidad;
}
