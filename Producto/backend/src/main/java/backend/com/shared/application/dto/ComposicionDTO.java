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

    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigoComposicion;

    @NotBlank(message = "La descripción de composición es obligatoria")
    @Size(max = 60)
    private String descripcionComposicion;

    @Size(max = 20)
    private String clasificacion;

    @Size(max = 60)
    private String usoTipico;
}
