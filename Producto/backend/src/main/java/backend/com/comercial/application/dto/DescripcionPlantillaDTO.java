package backend.com.comercial.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DescripcionPlantillaDTO {

    private Long idDescripcionPlantilla;

    @NotNull(message = "El idSCOS es obligatorio")
    private Long idSCOS;

    @NotNull(message = "El idPlantilla es obligatorio")
    private Long idPlantilla;

    private String nombreCampo;            // espejo de la Plantilla, útil para FE

    private String valorDescripcion;       // "polar", "redondo", etc.

    private Boolean activo;

    @Builder.Default
    private List<SCOSPlantillaMaterialVinculoDTO> vinculos = new ArrayList<>();
}
