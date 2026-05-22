package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegionRequest {

    @NotBlank(message = "El nombre de la región es obligatorio")
    @Size(max = 100, message = "El nombre de la región no puede superar 100 caracteres")
    private String nombreRegion;

    @NotNull(message = "El ID del país es obligatorio")
    private Integer paisId;
}
