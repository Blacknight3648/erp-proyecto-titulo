package backend.com.comercial.application.dto;

import lombok.*;

/**
 * DTO para vincular un material (tela/accesorio) a una DescripcionPlantilla.
 * tempId y tempMaterialId son útiles solo durante la construcción del POST
 * desde el frontend (cuando los IDs reales aún no existen).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SCOSPlantillaMaterialVinculoDTO {

    private Long id;
    private String tempId;          // solo FE
    private String materialType;    // "TELA" | "ACCESORIO"
    private Long materialId;
    private String tempMaterialId;  // solo FE
    private Integer cantidad;
}
