package backend.com.maestros.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Detalle específico para artículos tipo PRENDA_LISTA.
 * Relación @OneToOne con Articulo usando PK compartida (@MapsId).
 */
@Entity
@Table(name = "articulo_prenda")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloPrenda {

    @Id
    @Column(name = "id_articulo")
    private Integer id;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_articulo")
    private Articulo articulo;

    @Column(name = "marca", length = 60)
    private String marca;

    @Column(name = "tallas_disponibles", length = 100)
    private String tallasDisponibles;

    @Column(name = "proveedor", length = 100)
    private String proveedor;

    @Column(name = "codigo_proveedor", length = 30)
    private String codigoProveedor;

    @Column(name = "requiere_logo_cliente", nullable = false)
    private Boolean requiereLogoCliente = false;

    @Column(name = "tiene_estampado", nullable = false)
    private Boolean tieneEstampado = false;

    @Column(name = "ubicacion_logo", length = 60)
    private String ubicacionLogo;
}
