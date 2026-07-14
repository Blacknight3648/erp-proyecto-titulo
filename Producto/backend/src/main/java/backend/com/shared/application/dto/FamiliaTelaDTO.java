package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FamiliaTelaDTO {

    private Integer idFamiliaTela;

    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigoFamilia;

    @NotBlank(message = "El nombre de familia es obligatorio")
    @Size(max = 60)
    private String nombreFamilia;
}
