package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionDTO {

    private Long regionId;

    @NotBlank(message = "El nombre de la región es obligatorio")
    @Size(max = 100, message = "El nombre de la región no puede superar 100 caracteres")
    private String nombreRegion;

    private PaisDTO pais;
}