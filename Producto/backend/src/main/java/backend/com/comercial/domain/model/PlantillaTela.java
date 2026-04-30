package backend.com.comercial.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class PlantillaTela {
    private String aplicacion;
    private String nombre;
    private String composicion;
    private String color;
    private BigDecimal peso;
    private String unidadMedida;
}
