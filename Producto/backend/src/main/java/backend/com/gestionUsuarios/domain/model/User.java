package backend.com.gestionUsuarios.domain.model;

import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private Long usuarioId;
    private String usuarioRun;
    private String usuarioNombre;
    private String usuarioApellidos;
    private String usuarioEmail;
    private String usuarioPassword;
    private String telefono;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private Set<Area> areas = new HashSet<>();
}
