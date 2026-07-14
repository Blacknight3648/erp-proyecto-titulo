package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TipoAccesorioDTO {

    private Integer idTipoAccesorio;

    @Size(max = 10, message = "El código no puede superar 10 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre del tipo de accesorio es obligatorio")
    @Size(max = 60, message = "El nombre no puede superar 60 caracteres")
    private String nombre;
}
