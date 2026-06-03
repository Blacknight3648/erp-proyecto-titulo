package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** Request para ColorTela. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ColorTelaRequestDTO {

    @NotBlank(message = "El código de color es obligatorio")
    @Size(max = 10)
    private String codigoColor;

    @NotBlank(message = "La descripción del color es obligatoria")
    @Size(max = 40)
    private String descripcionColor;

    /** Indica si el código corresponde a referencia Pantone. */
    private Boolean esPantone = false;
}
