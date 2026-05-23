package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaisDTO {

    private Long idPais;

    @NotBlank(message = "El nombre del país es obligatorio")
    @Size(max = 100, message = "El nombre del país no puede superar 100 caracteres")
    private String nombrePais;
}