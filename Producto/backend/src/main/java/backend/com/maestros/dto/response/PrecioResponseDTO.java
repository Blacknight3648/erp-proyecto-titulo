package backend.com.maestros.dto.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * Respuesta pública de un Precio.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrecioResponseDTO {

    private Integer idPrecio;
    private Integer idArticulo;
    private String codigoArticulo;

    // Moneda resumida
    private Integer idMoneda;
    private String codigoMoneda;
    private String simboloMoneda;

    private String tipoPrecio;
    private BigDecimal valor;
}
