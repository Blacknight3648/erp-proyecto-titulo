package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BancoDTO {

    private Integer bancoId;

    @NotBlank(message = "El nombre del banco es obligatorio")
    @Size(max = 150, message = "El nombre del banco no puede superar 150 caracteres")
    private String nombreBanco;

    @NotBlank(message = "El código del banco es obligatorio")
    @Size(max = 20, message = "El código del banco no puede superar 20 caracteres")
    private String codigoBanco;
}