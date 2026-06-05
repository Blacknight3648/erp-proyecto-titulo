package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClasificacionTecnicaDTO {

    private Integer idClasificacionTecnica;

    @NotBlank(message = "El nombre de clasificación es obligatorio")
    @Size(max = 40, message = "No puede superar los 40 caracteres")
    private String nombreClasificacion;
}
