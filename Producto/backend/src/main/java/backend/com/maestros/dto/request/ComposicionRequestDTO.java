package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** Request para Composicion textil. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ComposicionRequestDTO {

    @NotBlank(message = "El código de composición es obligatorio")
    @Size(max = 10)
    private String codigoComposicion;

    @NotBlank(message = "La descripción de composición es obligatoria")
    @Size(max = 60)
    private String descripcionComposicion;

    /** Ej: "NATURAL", "SINTETICO", "MIXTO". */
    @Size(max = 20)
    private String clasificacion;

    @Size(max = 60)
    private String usoTipico;
}
