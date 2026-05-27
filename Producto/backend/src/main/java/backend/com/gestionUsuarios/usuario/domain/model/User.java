package backend.com.gestionUsuarios.usuario.domain.model;

import backend.com.gestionUsuarios.area.domain.model.Area;
import backend.com.gestionUsuarios.role.domain.model.Role;
import backend.com.shared.validations.run.ValidRun;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id_usuario")
        private Long usuarioId;

        @Column(name = "run", length = 12, nullable = false, unique = true)
        @ValidRun
        private String usuarioRun;

        @NotBlank
        @Column(name = "nombre", length = 50, nullable = false)
        private String usuarioNombre;

        @NotBlank
        @Column(name = "apellidos", length = 50, nullable = false)
        private String usuarioApellidos;

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,10}$")
        @Column(name = "email", length = 80, nullable = false, unique = true)
        private String usuarioEmail;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        @NotBlank
        @Column(name = "password", length = 200, nullable = false)
        private String usuarioPassword;

        @Column(name = "telefono", length = 50, nullable = false)
        private String telefono;

        @Builder.Default
        @Column(name = "enabled")
        private boolean enabled = true;

        // Roles del usuario
        @Builder.Default
        @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        @JoinTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
                        "user_id", "role_id" }))
        @JsonIgnoreProperties({ "roles", "area" })
        private Set<Role> roles = new HashSet<>();

        // Áreas a las que pertenece
        @Builder.Default
        @ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
        @JoinTable(name = "usuarios_areas", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "area_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
                        "user_id", "area_id" }))
        @JsonIgnoreProperties({ "roles" })
        private Set<Area> areas = new HashSet<>();
}