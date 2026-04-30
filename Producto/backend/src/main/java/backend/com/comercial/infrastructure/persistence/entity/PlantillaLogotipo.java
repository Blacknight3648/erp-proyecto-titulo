package backend.com.comercial.infrastructure.persistence.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaLogotipo {
    private String tipo;
    private String nombre;
    private String ubicacion;
    private String color;
    private Double tamano;
    private Integer cantidad;
    private BigDecimal precio;
}
