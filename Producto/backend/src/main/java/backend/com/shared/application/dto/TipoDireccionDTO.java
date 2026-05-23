package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TipoDireccionDTO {

    private Integer tipoDireccionId;

    @NotBlank(message = "La descripción del tipo de dirección es obligatoria")
    @Size(max = 200, message = "La descripción no puede superar 200 caracteres")
    private String descripcion;
}