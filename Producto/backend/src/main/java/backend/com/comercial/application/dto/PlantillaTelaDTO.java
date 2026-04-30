package backend.com.comercial.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantillaTelaDTO {
    private String aplicacion;
    private String nombre;
    private String composicion;
    private String color;
    private BigDecimal peso;
    private String unidadMedida;
}
