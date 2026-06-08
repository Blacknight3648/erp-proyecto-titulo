package backend.com.comercial.domain.model;

import lombok.*;

/**
 * Vincula un material (Tela o Accesorio) a una DescripcionPlantilla.
 * Una DescripcionPlantilla puede tener N vínculos si el campo usa varios
 * materiales.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSPlantillaMaterialVinculo {

    private Long id;
    private String materialType;   // "TELA" | "ACCESORIO"
    private Long materialId;       // FK lógica a SCOSTela.idTela o SCOSAccesorio.idAccesorio
    private Integer cantidad;
}
