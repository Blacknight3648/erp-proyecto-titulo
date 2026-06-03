package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** Request para ClasificacionTecnica. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClasificacionTecnicaRequestDTO {

    @NotBlank(message = "El nombre de clasificación es obligatorio")
    @Size(max = 40, message = "No puede superar los 40 caracteres")
    private String nombreClasificacion;
}
