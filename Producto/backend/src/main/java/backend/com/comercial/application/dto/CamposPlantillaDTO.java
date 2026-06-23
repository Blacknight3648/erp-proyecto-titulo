package backend.com.comercial.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CamposPlantillaDTO {

    private Long idPlantilla;

    @NotBlank(message = "El nombre del campo es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
    private String nombreCampo;
}
