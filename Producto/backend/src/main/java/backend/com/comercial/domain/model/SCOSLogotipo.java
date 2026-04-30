package backend.com.comercial.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class SCOSLogotipo {
    private Long id;
    private String tipo;
    private String nombre;
    private String ubicacion;
    private String color;
    private Double tamano;
    private Integer cantidad;
    private BigDecimal precio;

    public BigDecimal getSubtotal() {
        if (precio == null || cantidad == null) return BigDecimal.ZERO;
        return precio.multiply(BigDecimal.valueOf(cantidad));
    }
}
