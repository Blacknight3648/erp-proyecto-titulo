package backend.com.gestionUsuarios.usuario.application.dto;

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
    private Set<String> roles; // nombres de roles
    private Set<String> areas; // nombres de áreas

}
