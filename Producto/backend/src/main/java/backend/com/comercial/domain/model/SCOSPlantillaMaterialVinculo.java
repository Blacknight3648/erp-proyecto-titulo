package backend.com.comercial.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SCOSPlantillaMaterialVinculo {
    private Long id;
    private String tempId;
    private String fieldName;
    private String materialType; // "TELA" or "ACCESORIO"
    private Long materialId;
    private String tempMaterialId;
    private Integer cantidad;
}
