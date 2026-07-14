package backend.com.shared.infrastructure.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Detalle específico para artículos tipo ACCESORIO.
 * Relación @OneToOne con Articulo usando PK compartida (@MapsId).
 */
@Entity
@Table(name = "articulo_accesorio")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticuloAccesorioJpaEntity {

    @Id
    @Column(name = "id_articulo")
    private Integer id;

    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id_articulo")
    private ArticuloJpaEntity articulo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_accesorio")
    private TipoAccesorioJpaEntity tipoAccesorio;

    @OneToMany(mappedBy = "articuloAccesorio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<ArticuloAccesorioAtributoValorJpaEntity> atributos;

    @Column(name = "proveedor", length = 100)
    private String proveedor;

    @Column(name = "codigo_proveedor", length = 30)
    private String codigoProveedor;

    @Builder.Default
    @Column(name = "requiere_logo_cliente", nullable = false)
    private Boolean requiereLogoCliente = false;
}
