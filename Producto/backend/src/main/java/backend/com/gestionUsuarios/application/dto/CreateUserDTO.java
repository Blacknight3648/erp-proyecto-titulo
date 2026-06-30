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
public class CreateUserDTO {

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
    private Set<String> roles; // nombres de roles
    private Set<String> areas; // nombres de áreas
    private Boolean enabled;
    // El ADMIN debe confirmar que cuenta con el consentimiento del colaborador
    // para poder cambiarle la contraseña.
    private Boolean passwordChangeConsent;
}
