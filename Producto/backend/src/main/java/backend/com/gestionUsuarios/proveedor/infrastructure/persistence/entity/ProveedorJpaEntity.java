package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.BaseEntity;
import backend.com.shared.validations.email.ValidEmail;
import backend.com.shared.validations.run.ValidRun;
import backend.com.shared.validations.telefono.ValidPhone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(
        name = "proveedores",
        indexes = {
                @Index(name = "idx_proveedor_rut", columnList = "rutProveedor"),
                @Index(name = "idx_proveedor_email", columnList = "emailProveedor")
        }
)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProveedorJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Long proveedorId;

    @NotBlank(message = "El nombre del proveedor es obligatorio")
    @Size(max = 100)
    @Column(name = "nombreProveedor", unique = true, length = 100, nullable = false)
    private String nombreProveedor;

    @ValidRun
    @Size(max = 12)
    @Column(name = "rutProveedor", length = 12)
    private String rutProveedor;

    @Size(max = 150)
    @Column(name = "direccionProveedor", length = 150)
    private String direccionProveedor;

    @ValidPhone
    @Size(max = 20)
    @Column(name = "telefonoProveedor", length = 20)
    private String telefonoProveedor;

    @ValidEmail
    @Size(max = 150)
    @Column(name = "emailProveedor", length = 150)
    private String emailProveedor;

    @Size(max = 100)
    @Column(name = "contactoProveedor", length = 100)
    private String contactoProveedor;

    @Size(max = 100)
    @Column(name = "categoria", length = 100)
    private String categoria;


}
