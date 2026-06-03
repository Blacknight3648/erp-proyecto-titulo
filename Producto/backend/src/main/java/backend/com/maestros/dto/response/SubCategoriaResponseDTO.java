package backend.com.maestros.dto.response;

import lombok.*;

/**
 * Respuesta pública de una SubCategoría (incluye datos resumidos del padre).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaResponseDTO {

    private Integer idSubCategoria;
    private String codigoSubcategoria;
    private String nombreSubCategoria;

    // Categoría padre resumida
    private Integer idCategoria;
    private String codigoCategoria;
    private String nombreCategoria;
}
