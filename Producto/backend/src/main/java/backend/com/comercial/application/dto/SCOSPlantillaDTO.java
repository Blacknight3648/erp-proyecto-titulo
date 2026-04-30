package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SCOSPlantillaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    
    // Nuevos campos técnicos opcionales
    private String nombrePrenda;
    private String forro;
    private String relleno;
    private String gorro;
    private String cuello;
    private String abotonaduraCierre;
    private String cortesAplicaciones;
    private String fuelles;
    private String mangas;
    private String pretinasRuedo;
    private String bolsillos;
    private String cintaDetalle;
    private String logoDetalle;
    private String colorForro;
    private String accesoriosDetalle;
    private String obsModelo;
    private String genero;
    private String customFields;
    private List<String> camposActivos;
    
    private List<PlantillaTelaDTO> telas;
    private List<PlantillaAccesorioDTO> accesorios;
    private List<SCOSLogotipoDTO> logotipos;
    private List<SCOSPlantillaMaterialVinculoDTO> vinculos;

    // Mano de Obra
    private BigDecimal moPrenda;
    private BigDecimal moCosturaSellada;
    private BigDecimal moAcolchado;
}
