package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DatoBancarioRequest {

    @NotBlank(message = "El número de cuenta es obligatorio")
    @Size(max = 50, message = "El número de cuenta no puede superar 50 caracteres")
    private String numeroCuenta;

    @NotNull(message = "El ID del banco es obligatorio")
    private Integer bancoId;

    @NotNull(message = "El ID del tipo de cuenta es obligatorio")
    private Integer tipoCuentaId;
}
