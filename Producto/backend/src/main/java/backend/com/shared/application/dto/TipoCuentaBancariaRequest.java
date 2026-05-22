package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TipoCuentaBancariaRequest {

    @NotBlank(message = "La denominación de la cuenta es obligatoria")
    @Size(max = 100, message = "La denominación no puede superar 100 caracteres")
    private String denominacionCuenta;
}
