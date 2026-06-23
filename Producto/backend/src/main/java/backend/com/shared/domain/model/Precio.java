package backend.com.shared.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Precio {

    private Integer idPrecio;
    private Articulo articulo;
    private Moneda moneda;
    private String tipoPrecio;
    private BigDecimal valor;
}