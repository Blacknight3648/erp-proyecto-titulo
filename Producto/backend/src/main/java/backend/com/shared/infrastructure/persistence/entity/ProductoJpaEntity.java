package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "producto", indexes = {
        @Index(name = "idx_producto_codigo", columnList = "codigo_producto")
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProductoJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productoId;

    @Column(length = 50, unique = true, nullable = false)
    private String codigoProducto;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 300)
    private String descripcion;

    @Column(length = 30)
    private String genero;

    @Column(length = 50)
    private String color;
}
