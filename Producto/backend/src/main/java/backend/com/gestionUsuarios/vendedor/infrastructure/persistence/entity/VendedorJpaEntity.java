package backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity;

import backend.com.gestionUsuarios.usuario.domain.model.User;
import backend.com.shared.infrastructure.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * JPA Entity for 'vendedores' table.
 */
@Entity
@Table(name = "vendedores", uniqueConstraints = {
    @UniqueConstraint(name = "uk_vendedor_codigo", columnNames = "codigo_vendedor"),
    @UniqueConstraint(name = "uk_vendedor_usuario", columnNames = "id_usuario")
})
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class VendedorJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vendedor")
    private Long idVendedor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private User usuario;

    @Column(name = "codigo_vendedor", length = 20, nullable = false)
    private String codigoVendedor;

    // The 'activo' field is inherited from BaseEntity
}
