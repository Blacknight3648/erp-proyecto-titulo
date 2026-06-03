package backend.com.maestros.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/** Request para FamiliaTela. */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FamiliaTelaRequestDTO {

    @NotBlank(message = "El código de familia es obligatorio")
    @Size(max = 10)
    private String codigoFamilia;

    @NotBlank(message = "El nombre de familia es obligatorio")
    @Size(max = 60)
    private String nombreFamilia;
}
