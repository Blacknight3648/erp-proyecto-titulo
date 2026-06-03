package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request para crear o actualizar una Unidad de Medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadMedidaRequestDTO {

    @NotBlank(message = "El nombre de la unidad es obligatorio")
    @Size(max = 30, message = "El nombre no puede superar los 30 caracteres")
    private String nombreUnidad;

    @NotBlank(message = "La abreviatura es obligatoria")
    @Size(max = 5, message = "La abreviatura no puede superar los 5 caracteres")
    private String abreviatura;
}
