package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SCOSPlantillaMaterialVinculoDTO {
    private Long id;
    private String tempId;
    private String fieldName;
    private String materialType; // "TELA" or "ACCESORIO"
    private Long materialId;
    private String tempMaterialId;
    private Integer cantidad;
}
