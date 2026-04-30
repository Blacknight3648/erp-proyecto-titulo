package backend.com.shared.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermisoDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String modulo;
}
