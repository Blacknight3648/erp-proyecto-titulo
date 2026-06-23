package backend.com.gestionUsuarios.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSummaryDTO {
    private Long usuarioId;
    private String usuarioNombre;
    private String usuarioApellidos;
}
