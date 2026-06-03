package backend.com.maestros.dto.response;

import lombok.*;

/**
 * Respuesta pública de una Moneda.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonedaResponseDTO {

    private Integer idMoneda;
    private String codigoMoneda;
    private String nombreMoneda;
    private String simbolo;
}
