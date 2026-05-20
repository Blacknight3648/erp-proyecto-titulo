package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSPlantillaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String nombrePrenda;
    private String genero;

    private Map<String, Object> detallesPrenda;
    private Map<String, String> camposPersonalizados;
    private List<String> camposActivos;

    private List<PlantillaTelaDTO> telas;
    private List<PlantillaAccesorioDTO> accesorios;
    private List<SCOSLogotipoDTO> logotipos;
    private List<SCOSPlantillaMaterialVinculoDTO> vinculos;

    private BigDecimal moPrenda;
    private BigDecimal moCosturaSellada;
    private BigDecimal moAcolchado;
}
