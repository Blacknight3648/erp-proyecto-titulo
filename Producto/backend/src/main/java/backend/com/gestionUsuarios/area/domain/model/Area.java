package backend.com.gestionUsuarios.area.domain.model;

import backend.com.gestionUsuarios.role.domain.model.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "areas")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_area")
    @EqualsAndHashCode.Include
    private Long areaId;

    @Column(name = "nombre_area", nullable = false, unique = true, length = 100)
    @NotBlank(message = "El área debe tener un nombre definido")
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    // Roles asociados al área
    @Builder.Default
    @OneToMany(mappedBy = "area", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Role> roles = new HashSet<>();
}
