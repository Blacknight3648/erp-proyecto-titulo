package backend.com.comercial.domain.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Valor concreto que llena un campo de plantilla en una SolicitudCostos.
 * Ej: SCOS=1, plantilla=Forro, valorDescripcion="polar".
 *
 * Puede tener varios vínculos a materiales (telas o accesorios) si el campo
 * usa más de uno (ej: una "Capucha" hecha con 2 telas distintas).
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DescripcionPlantilla {

    private Long idDescripcionPlantilla;
    private Long idSCOS;
    private CamposPlantilla plantilla;
    private String valorDescripcion;
    private Boolean activo;

    @Builder.Default
    private List<SCOSPlantillaMaterialVinculo> vinculos = new ArrayList<>();
}
