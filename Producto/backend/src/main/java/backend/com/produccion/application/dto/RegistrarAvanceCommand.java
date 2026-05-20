package backend.com.produccion.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegistrarAvanceCommand {

    @PositiveOrZero(message = "La cantidad producida no puede ser negativa")
    private Integer cantidadProducida;

    @PositiveOrZero(message = "La cantidad de merma no puede ser negativa")
    private Integer cantidadMerma;

    @Size(max = 255, message = "El motivo de merma no puede superar 255 caracteres")
    private String motivoMerma;

    @NotBlank(message = "El usuario que registra el avance es obligatorio")
    @Size(max = 100)
    private String usuario;

    @Size(max = 1000)
    private String observacion;
}
