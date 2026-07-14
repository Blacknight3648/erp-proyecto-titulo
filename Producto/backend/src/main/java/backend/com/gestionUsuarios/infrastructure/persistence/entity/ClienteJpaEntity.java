package backend.com.gestionUsuarios.infrastructure.persistence.entity;

import backend.com.shared.infrastructure.persistence.entity.DireccionJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.GiroJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

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
    private String runCliente;

    @Column(name = "sigla")
    private String sigla;

    @Column(name = "activo", nullable = false)
    private boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_giro")
    private GiroJpaEntity giro;

    // Nombres de columna propios (distintos de los `fk_provee_*` que usa Proveedor sobre
    // las mismas tablas compartidas `direccion`/`contactos`) para que cada dueño tenga su
    // propia FK y no haya colisión ni ambigüedad en el esquema.
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_cliente_direccion")
    @Builder.Default
    private List<DireccionJpaEntity> direccion = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fk_cliente_contacto")
    @Builder.Default
    private List<ContactoJpaEntity> contacto = new ArrayList<>();
}
