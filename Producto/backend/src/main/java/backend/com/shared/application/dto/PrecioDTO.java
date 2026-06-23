package backend.com.shared.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrecioDTO {

    private Integer idPrecio;

    @NotNull(message = "El id del artículo es obligatorio")
    @Positive(message = "El id del artículo debe ser positivo")
    private Integer idArticulo;

    private String codigoArticulo;

    @NotNull(message = "El id de moneda es obligatorio")
    @Positive(message = "El id de moneda debe ser positivo")
    private Integer idMoneda;

    private String codigoMoneda;
    private String simboloMoneda;

    @NotBlank(message = "El tipo de precio es obligatorio")
    @Size(max = 10, message = "El tipo de precio no puede superar los 10 caracteres")
    private String tipoPrecio;

    @NotNull(message = "El valor del precio es obligatorio")
    @DecimalMin(value = "0.00", message = "El precio no puede ser negativo")
    @Digits(integer = 16, fraction = 2, message = "Formato de precio inválido")
    private BigDecimal valor;
}
