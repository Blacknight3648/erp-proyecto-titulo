package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonedaDTO {

    private Integer idMoneda;

    @NotBlank(message = "El código ISO de moneda es obligatorio")
    @Size(max = 5, message = "El código no puede superar los 5 caracteres")
    private String codigoMoneda;

    @NotBlank(message = "El nombre de moneda es obligatorio")
    @Size(max = 40, message = "El nombre no puede superar los 40 caracteres")
    private String nombreMoneda;

    @Size(max = 5, message = "El símbolo no puede superar los 5 caracteres")
    private String simbolo;
}
