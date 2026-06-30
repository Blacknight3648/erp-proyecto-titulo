package backend.com.gestionUsuarios.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long usuarioId;
    private String usuarioRun;
    private String usuarioNombre;
    private String usuarioApellidos;
    private String usuarioEmail;
    private String usuarioPassword;
    private String telefono;
    private LocalDate fechaNacimiento;
    private String direccion;
    private String region;
    private String comuna;
    private boolean enabled;

    // Roles completos del usuario
    private Set<RoleDTO> roles;

    // Áreas completas del usuario
    private Set<AreaDTO> areas;
}
