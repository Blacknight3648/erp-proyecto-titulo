package backend.com.maestros.dto.response;

import lombok.*;

/**
 * Respuesta pública de una Categoría.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaResponseDTO {

    private Integer idCategoria;
    private String codigoCategoria;
    private String nombreCategoria;
}
