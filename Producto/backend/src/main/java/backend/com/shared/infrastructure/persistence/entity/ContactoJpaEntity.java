package backend.com.shared.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "contactos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long contactoId;

    @Column(name = "nombre_contacto", nullable = false, length = 100)
    private String nombreContacto;

    @Column(name = "telefono_contacto", length = 15)
    private String telefonoContacto;

    @Column(name = "email_contacto", length = 100)
    private String emailContacto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_contacto_id", nullable = false)
    private TipoContactoJpaEntity tipoContacto;
}
