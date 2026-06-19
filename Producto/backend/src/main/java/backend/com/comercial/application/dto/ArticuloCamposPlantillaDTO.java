package backend.com.comercial.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticuloCamposPlantillaDTO {

    private Long idModeloPlantilla;

    @NotNull(message = "El idArticulo es obligatorio")
    private Integer idArticulo;

    @NotNull(message = "El idPlantilla es obligatorio")
    private Long idPlantilla;

    private String nombreCampo;  // espejo desde Plantilla para reads
}
