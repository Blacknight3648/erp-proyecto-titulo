package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DireccionRequest {

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 150, message = "La calle no puede superar 150 caracteres")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Size(max = 20, message = "El número no puede superar 20 caracteres")
    private String numero;

    @Size(max = 20, message = "El depto no puede superar 20 caracteres")
    private String depto;

    @NotNull(message = "El tipo de dirección es obligatorio")
    private Integer tipoDireccionId;

    @NotNull(message = "La comuna es obligatoria")
    private Long comunaId;
}
