package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfiguracionPlantillaDTO {
    private Long id;
    private String nombrePrenda;
    private Set<String> camposActivos;
    private String customFields;
    private List<PlantillaTelaDTO> plantillaTelas = new ArrayList<>();
    private List<PlantillaAccesorioDTO> plantillaAccesorios = new ArrayList<>();
}
