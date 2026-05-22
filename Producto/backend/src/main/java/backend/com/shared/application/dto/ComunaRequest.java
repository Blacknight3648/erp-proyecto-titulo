package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ComunaRequest {

    @NotBlank(message = "El nombre de la comuna es obligatorio")
    @Size(max = 100, message = "El nombre de la comuna no puede superar 100 caracteres")
    private String nombreComuna;

    @NotNull(message = "El ID de la región es obligatorio")
    private Long regionId;
}
