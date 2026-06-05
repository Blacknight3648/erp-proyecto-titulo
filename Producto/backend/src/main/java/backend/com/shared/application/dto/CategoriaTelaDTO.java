package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoriaTelaDTO {

    private Integer idCategoriaTela;

    @NotBlank(message = "El código de categoría es obligatorio")
    @Size(max = 10, message = "El código no puede superar los 10 caracteres")
    private String codigoCategoriaTela;

    @NotBlank(message = "El nombre de categoría es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    private String nombreCategoriaTela;
}
