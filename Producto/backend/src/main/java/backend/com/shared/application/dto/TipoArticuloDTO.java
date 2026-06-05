package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoArticuloDTO {

    @NotNull(message = "El id es obligatorio")
    private Integer idTipoArticulo;

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20, message = "El código no puede superar 20 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar 60 caracteres")
    private String nombre;
}
