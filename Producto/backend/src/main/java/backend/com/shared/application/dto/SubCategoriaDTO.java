package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoriaDTO {

    private Integer idSubCategoria;

    @NotBlank(message = "El código de subcategoría es obligatorio")
    @Size(max = 15, message = "El código no puede superar los 15 caracteres")
    private String codigoSubcategoria;

    @NotBlank(message = "El nombre de subcategoría es obligatorio")
    @Size(max = 80, message = "El nombre no puede superar los 80 caracteres")
    private String nombreSubCategoria;

    @NotNull(message = "El id de categoría padre es obligatorio")
    @Positive(message = "El id de categoría debe ser positivo")
    private Integer idCategoria;

    private String codigoCategoria;
    private String nombreCategoria;
}
