package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComposicionDTO {

    private Integer idComposicion;

    @NotBlank(message = "El código de composición es obligatorio")
    @Size(max = 10)
    private String codigoComposicion;

    @NotBlank(message = "La descripción de composición es obligatoria")
    @Size(max = 60)
    private String descripcionComposicion;

    @Size(max = 20)
    private String clasificacion;

    @Size(max = 60)
    private String usoTipico;
}
