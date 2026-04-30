package backend.com.gestionUsuarios.role.application.dto;

import backend.com.gestionUsuarios.usuario.application.dto.UserSummaryDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDTO {

    private Long id;
    private String nombre;
    private String descripcion;

    // Id del área a la que pertenece el rol
    private Long areaId;

    // Ids de permisos asociados
    private Set<Long> permisosIds;

    // Usuarios asignados al rol (resumen)
    private Set<UserSummaryDTO> usuarios;
}