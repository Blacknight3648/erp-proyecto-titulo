package backend.com.gestionUsuarios.domain.model;

import backend.com.shared.domain.model.Permiso;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Role {

    @EqualsAndHashCode.Include
    private Long id;
    private String nombre;
    private String descripcion;
    private Area area;

    @Builder.Default
    private Set<Permiso> permisos = new HashSet<>();
}
