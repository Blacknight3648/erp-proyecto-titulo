package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaDTO {

    private Integer idCategoria;

    @NotBlank(message = "El código de categoría es obligatorio")
    @Size(max = 10, message = "El código no puede superar los 10 caracteres")
    private String codigoCategoria;

    @NotBlank(message = "El nombre de categoría es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    private String nombreCategoria;
}
