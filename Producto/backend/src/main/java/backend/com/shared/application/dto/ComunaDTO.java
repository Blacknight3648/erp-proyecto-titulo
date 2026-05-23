package backend.com.shared.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComunaDTO {

    private Long comunaId;

    @NotBlank(message = "El nombre de la comuna es obligatorio")
    @Size(max = 100, message = "El nombre de la comuna no puede superar 100 caracteres")
    private String nombreComuna;

    private RegionDTO region;
}