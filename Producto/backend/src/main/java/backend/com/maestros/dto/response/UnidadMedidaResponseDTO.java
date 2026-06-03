package backend.com.maestros.dto.response;

import lombok.*;

/**
 * Respuesta pública de una Unidad de Medida.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UnidadMedidaResponseDTO {

    private Integer idUnidadMedida;
    private String nombreUnidad;
    private String abreviatura;
}
