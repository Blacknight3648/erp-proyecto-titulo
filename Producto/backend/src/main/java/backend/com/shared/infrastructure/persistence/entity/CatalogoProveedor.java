package backend.com.shared.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "catalogo_proveedor")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CatalogoProveedor extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long catalogoProveedorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private ProveedorJpaEntity proveedor;

    @Column(name = "codigo_proveedor", length = 50, nullable = false)
    private String codigoProveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoJpaEntity producto;

    @Column(name = "precio", nullable = false)
    private Double precio;
}
