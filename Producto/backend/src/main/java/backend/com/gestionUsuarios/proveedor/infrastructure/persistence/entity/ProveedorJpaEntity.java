package backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.BaseEntity;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.validations.run.ValidRun;
import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.DatoBancarioJpaEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "proveedores", indexes = {
                @Index(name = "idx_proveedor_run", columnList = "run_proveedor"),
                @Index(name = "idx_proveedor_email", columnList = "email_proveedor")
})
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

        @Size(max = 100)
        @Column(name = "sigla", length = 100)
        private String sigla;

        @Size(max = 100)
        @Column(name = "horario_atencion", length = 100)
        private String horarioAtencion;

        @Size(max = 30)
        @Column(name = "tipo_proveedor", length = 30)
        private String tipoProveedor;

        @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "fk_provee_contacto")
        private List<ContactoJpaEntity> contactos;

        @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "fk_provee_direccion")
        private List<DireccionJpaEntity> direcciones;

        @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "fk_provee_dato_bancario")
        private List<DatoBancarioJpaEntity> datosBancarios;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "fk_provee_giro")
        private GiroJpaEntity giro;
}
