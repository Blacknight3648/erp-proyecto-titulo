package backend.com.gestionUsuarios.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AreaDTO {

    private Long areaId;
    private String nombre;
    private String descripcion;
    private Set<RoleDTO> roles;
}
