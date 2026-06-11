package backend.com.gestionUsuarios.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    @EqualsAndHashCode.Include
    private Long usuarioId;

    @Column(name = "run", length = 12, nullable = false, unique = true)
    private String usuarioRun;

    @Column(name = "nombre", length = 50, nullable = false)
    private String usuarioNombre;

    @Column(name = "apellidos", length = 50, nullable = false)
    private String usuarioApellidos;

    @Column(name = "email", length = 80, nullable = false, unique = true)
    private String usuarioEmail;

    @Column(name = "password", length = 200, nullable = false)
    private String usuarioPassword;

    @Column(name = "telefono", length = 50, nullable = false)
    private String telefono;

    @Builder.Default
    @Column(name = "enabled")
    private boolean enabled = true;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "usuarios_roles", 
            joinColumns = @JoinColumn(name = "user_id"), 
            inverseJoinColumns = @JoinColumn(name = "role_id"), 
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
    @ToString.Exclude
    private Set<RoleJpaEntity> roles = new HashSet<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "usuarios_areas", 
            joinColumns = @JoinColumn(name = "user_id"), 
            inverseJoinColumns = @JoinColumn(name = "area_id"), 
            uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "area_id"}))
    @ToString.Exclude
    private Set<AreaJpaEntity> areas = new HashSet<>();
}
