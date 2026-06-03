package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Request para crear o actualizar una SubCategoría.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaRequestDTO {

    @NotBlank(message = "El código de subcategoría es obligatorio")
    @Size(max = 15, message = "El código no puede superar los 15 caracteres")
    private String codigoSubcategoria;

    @NotBlank(message = "El nombre de subcategoría es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    private String nombreSubCategoria;

    @NotNull(message = "El id de categoría padre es obligatorio")
    @Positive(message = "El id de categoría debe ser positivo")
    private Integer idCategoria;
}
