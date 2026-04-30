package backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity;

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

    @Column(name = "nombre_cliente", nullable = false)
    @NotBlank(message = "El nombre del cliente no puede estar vacío")
    private String nombreCliente;

    @Column(name = "apellido_cliente")
    private String apellidoCliente;

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

    @Column(name = "segmento")
    private String segmento;

    @Column(name = "contacto")
    private String contacto;

    @Column(name = "activo")
    private boolean activo;
}
