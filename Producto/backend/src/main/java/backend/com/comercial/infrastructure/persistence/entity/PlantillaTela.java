package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaTela {
    private String aplicacion;
    private String nombre;
    private String composicion;
    private String color;
    @Column(precision = 10, scale = 4)
    private BigDecimal peso;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;
}
