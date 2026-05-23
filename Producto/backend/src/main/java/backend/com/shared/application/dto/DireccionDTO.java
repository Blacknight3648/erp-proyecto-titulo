package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DireccionDTO {

    private Long direccionId;

    @NotBlank(message = "La calle es obligatoria")
    @Size(max = 200, message = "La calle no puede superar 200 caracteres")
    private String calle;

    @NotBlank(message = "El número es obligatorio")
    @Size(max = 20, message = "El número no puede superar 20 caracteres")
    private String numero;

    @Size(max = 20, message = "El departamento no puede superar 20 caracteres")
    private String depto;

    private TipoDireccionDTO tipoDireccion;

    private ComunaDTO comuna;
}