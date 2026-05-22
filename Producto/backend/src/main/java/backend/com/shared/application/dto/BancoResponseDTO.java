package backend.com.maestros.dto.response;

import lombok.*;

/**
 * Respuesta pública de un Banco.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BancoResponseDTO {

    private Integer idBanco;
    private String nombre;
    private String codigo;
}
