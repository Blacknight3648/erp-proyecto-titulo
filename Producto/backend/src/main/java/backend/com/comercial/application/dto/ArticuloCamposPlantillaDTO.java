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

    private String nombreArticulo;        // espejo del Articulo para reads

    /** Lista completa de campos de la plantilla del artículo. Ej: ["forro","cuello"]. */
    private List<String> camposPlantilla;
}
