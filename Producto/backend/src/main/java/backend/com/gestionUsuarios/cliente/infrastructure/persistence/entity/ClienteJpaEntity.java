package backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.validations.email.ValidEmail;
import backend.com.shared.validations.run.ValidRun;
import backend.com.shared.validations.telefono.ValidPhone;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "razon_social", nullable = false)
    @NotBlank(message = "La razón social del cliente no puede estar vacía")
    private String razonSocial;

    @Column(name = "run_cliente", unique = true, nullable = false)
    @ValidRun
    private String runCliente;

    @Column(name = "correo_cliente")
    @ValidEmail
    @Email(message = "Debe ser un correo válido")
    private String correoCliente;

    @Column(name = "telefono_cliente")
    @ValidPhone
    private String telefonoCliente;

    @Column(name = "direccion_cliente")
    private String direccionCliente;

    @Column(name = "contacto_cliente")
    private String contactoCliente;

    @Column(name = "sigla")
    private String sigla;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_giro")
    private GiroJpaEntity giro;

    @Column(name = "activo", nullable = false)
    private boolean activo;
}
