package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.BaseEntity;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.SiglaJpaEntity;
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
                @Index(name = "idx_proveedor_run", columnList = "run_proveedor"),
                @Index(name = "idx_proveedor_email", columnList = "email_proveedor")
        }
)
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProveedorJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proveedor_id")
    private Long proveedorId;

    @NotBlank(message = "La razón social del proveedor es obligatoria")
    @Size(max = 255)
    @Column(name = "razon_social_proveedor", length = 255, nullable = false)
    private String razonSocialProveedor;

    @ValidRun
    @Size(max = 12)
    @Column(name = "run_proveedor", length = 12, unique = true, nullable = false)
    private String runProveedor;

    @Size(max = 500)
    @Column(name = "direccion_proveedor", length = 500)
    private String direccionProveedor;

    @ValidPhone
    @Size(max = 20)
    @Column(name = "telefono_proveedor", length = 20)
    private String telefonoProveedor;

    @ValidEmail
    @Size(max = 150)
    @Column(name = "email_proveedor", length = 150)
    private String emailProveedor;

    @Size(max = 150)
    @Column(name = "contacto_proveedor", length = 150)
    private String contactoProveedor;

    @Size(max = 30)
    @Column(name = "tipo_proveedor", length = 30)
    private String tipoProveedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_sigla")
    private SiglaJpaEntity sigla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_giro")
    private GiroJpaEntity giro;
}
