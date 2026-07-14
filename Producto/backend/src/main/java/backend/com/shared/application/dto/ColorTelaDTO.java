package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorTelaDTO {

    private Integer idColor;

    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigoColor;

    @NotBlank(message = "La descripción del color es obligatoria")
    @Size(max = 40)
    private String descripcionColor;

    private Boolean esPantone;
}
