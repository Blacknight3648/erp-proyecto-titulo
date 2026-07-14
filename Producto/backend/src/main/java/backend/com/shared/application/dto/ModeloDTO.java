package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ModeloDTO {

    private Integer idModelo;

    @Size(max = 10, message = "El código no puede superar los 10 caracteres")
    private String codigoModelo;

    @NotBlank(message = "El nombre del modelo es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar los 60 caracteres")
    private String nombreModelo;
}
